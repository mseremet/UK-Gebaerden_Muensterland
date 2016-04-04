package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.apache.commons.lang3.Validate;

import java.text.DecimalFormat;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignTrainerUIFragment extends Fragment {

    private static final String TAG = SignTrainerUIFragment.class.getSimpleName();
    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String SLASH = "/";
    private static final String RAW = "raw";
    private static final String KEY_CURRENT_SIGN = "KEY_CURRENT_SIGN";
    private static final boolean INTERRUPT_IF_RUNNING = true;
    private static final String KEY_ANSWER_VISIBLE = "KEY_ANSWER_VISIBLE";
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView signQuestionText;
    private Button solveQuestionButton;
    private Sign currentSign = null;
    private LoadRandomSignTask loadRandomSignTask;
    private TextView signAnswerTextView;
    private TextView signMnemonicTextView;
    private TextView signLearningProgressTextView;
    private Button questionWasEasyButton;
    private Button questionWasFairButton;
    private Button questionWasHardButton;
    private boolean answerTextAndButtonsVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.trainer_fragment, container, false);
        this.videoView = (VideoView) view.findViewById(R.id.signTrainerVideoView);
        this.signQuestionText = (TextView) view.findViewById(R.id.signTrainerQuestionText);
        this.solveQuestionButton = (Button) view.findViewById(R.id.signTrainerSolveQuestionButton);
        this.solveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnSolveQuestionButton();
            }
        });
        this.signAnswerTextView = (TextView) view.findViewById(R.id.signTrainerAnswer);
        this.signMnemonicTextView = (TextView) view.findViewById(R.id.signTrainerMnemonic);
        this.signLearningProgressTextView = (TextView) view.findViewById(R.id.signTrainerLearningProgress);
        this.questionWasEasyButton = (Button) view.findViewById(R.id.signTrainerEasyButton);
        this.questionWasEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnQuestionWasEasyButton();
            }
        });
        this.questionWasFairButton = (Button) view.findViewById(R.id.signTrainerFairButton);
        this.questionWasFairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnQuestionWasFairButton();
            }
        });
        this.questionWasHardButton = (Button) view.findViewById(R.id.signTrainerHardButton);
        this.questionWasHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnQuestionWasHardButton();
            }
        });
        this.progressBar = (ProgressBar) view.findViewById(R.id.signTrainerVideoLoadingProgressBar);
        this.videoView.setContentDescription(getActivity().getString(R.string.videoIsLoading));
        toggleAnswerTextAndButtonsVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " + hashCode());
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            final Sign parcelledSign = savedInstanceState.getParcelable(KEY_CURRENT_SIGN);
            if (null != parcelledSign) {
                this.currentSign = parcelledSign;
                setupVideoView(this.currentSign);
            }
            final Boolean answerVisible = savedInstanceState.getBoolean(KEY_ANSWER_VISIBLE);
            Validate.notNull(answerVisible, "AnswerVisible should always be non-null in savedInstance bundle.");
            if (answerVisible && (null != this.currentSign)) {
                toggleAnswerTextAndButtonsVisibility(View.VISIBLE);
                setAnswerTextViews();
            } else {
                toggleAnswerTextAndButtonsVisibility(View.INVISIBLE);
            }
        } else {
            this.loadRandomSignTask = new LoadRandomSignTask(getActivity());
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart " + hashCode());
        super.onStart();
        if (null != this.loadRandomSignTask) {
            this.loadRandomSignTask.execute(this.currentSign);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause " + hashCode());
        if (null != this.loadRandomSignTask) {
            final AsyncTask.Status status = this.loadRandomSignTask.getStatus();
            if (status.equals(AsyncTask.Status.PENDING) || status.equals(AsyncTask.Status.RUNNING)) {
                this.loadRandomSignTask.cancel(INTERRUPT_IF_RUNNING);
            }
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_VISIBLE, this.answerTextAndButtonsVisible);
        if (null != this.currentSign) {
            outState.putParcelable(KEY_CURRENT_SIGN, this.currentSign);
        }
    }

    private void handleClickOnSolveQuestionButton() {
        Log.d(TAG, "handleClickOnSolveQuestionButton " + hashCode());
        toggleAnswerTextAndButtonsVisibility(View.VISIBLE);
        setAnswerTextViews();
    }

    private void setAnswerTextViews() {
        this.signAnswerTextView.setText(this.currentSign.getNameLocaleDe());
        this.signMnemonicTextView.setText(this.currentSign.getMnemonic());
        final DecimalFormat decimalFormat = new DecimalFormat(" 0;-0");
        this.signLearningProgressTextView.setText(getString(R.string.learningProgress) + ": " +
                decimalFormat.format(this.currentSign.getLearningProgress()));
    }

    private void handleClickOnQuestionWasEasyButton() {
        Log.d(TAG, "handleClickOnQuestionWasEasyButton " + hashCode());
        this.currentSign.increaseLearningProgress();
        new UpdateLearningProgressTask(getActivity()).execute(this.currentSign);
        new LoadRandomSignTask(getActivity()).execute(this.currentSign);
    }

    private void handleClickOnQuestionWasFairButton() {
        Log.d(TAG, "handleClickOnQuestionWasFairButton " + hashCode());
        new LoadRandomSignTask(getActivity()).execute(this.currentSign);
    }

    private void handleClickOnQuestionWasHardButton() {
        Log.d(TAG, "handleClickOnQuestionWasHardButton " + hashCode());
        this.currentSign.decreaseLearningProgress();
        new UpdateLearningProgressTask(getActivity()).execute(this.currentSign);
        new LoadRandomSignTask(getActivity()).execute(this.currentSign);
    }

    private void toggleAnswerTextAndButtonsVisibility(int visibility) {
        if (View.VISIBLE == visibility) {
            this.answerTextAndButtonsVisible = true;
        } else if (View.INVISIBLE == visibility) {
            this.answerTextAndButtonsVisible = false;
        } else {
            throw new IllegalArgumentException("Visibility can either be View.VISIBLE or VIEW.INVISIBLE, but was: " + visibility);
        }
        if (this.answerTextAndButtonsVisible) {
            this.solveQuestionButton.setEnabled(false);
        } else {
            this.solveQuestionButton.setEnabled(true);
        }
        this.signAnswerTextView.setVisibility(visibility);
        this.signMnemonicTextView.setVisibility(visibility);
        this.signLearningProgressTextView.setVisibility(visibility);
        this.questionWasEasyButton.setVisibility(visibility);
        this.questionWasFairButton.setVisibility(visibility);
        this.questionWasHardButton.setVisibility(visibility);
    }

    private void setupVideoView(final Sign sign) {
        final MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(mediaController);
        final int identifier = getActivity().getResources().getIdentifier(sign.getName(), RAW, getActivity().getPackageName());
        if (0 == identifier) {
            Snackbar.make(getView(), R.string.videoCouldNotBeLoaded, Snackbar.LENGTH_SHORT);
            new LoadRandomSignTask(getActivity()).execute(this.currentSign);
        } else {
            this.videoView.setVideoURI(Uri.parse(ANDROID_RESOURCE + getActivity().getPackageName() + SLASH + identifier));
            this.videoView.requestFocus();
            this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    SignTrainerUIFragment.this.progressBar.setVisibility(View.GONE);
                    mp.setVolume(0f, 0f);
                    SignTrainerUIFragment.this.videoView.seekTo(0);
                    SignTrainerUIFragment.this.videoView.start();
                    SignTrainerUIFragment.this.videoView.setContentDescription(getActivity()
                            .getString(R.string.videoIsPlaying) + ": " + sign.getName());
                }
            });
            this.videoView.setMediaController(null);
        }
    }

    /**
     * Reads a random sign from the database. Will return null if the task is cancelled. The current
     * sign can be provided as a parameter or be null, if there is no current sign.
     */
    private class LoadRandomSignTask extends AsyncTask<Sign, Void, Sign> {

        private final Context context;

        public LoadRandomSignTask(Context context) {
            this.context = context;
        }

        @Override
        protected Sign doInBackground(Sign... params) {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "doInBackground " + hashCode());
            Validate.inclusiveBetween(0, 1, params.length, "Only null or one sign as a parameter allowed.");
            if (isCancelled()) {
                return null;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            Sign sign;
            if (1 == params.length && null != params[0]) { // current sign provided via parameters
                sign = signDAO.readRandomSign(params[0]);
            } else {
                sign = signDAO.readRandomSign(null);
            }
            signDAO.close();
            return sign;
        }


        @Override
        protected void onPostExecute(Sign result) {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "onPostExecute " + hashCode());
            if (null == result) {
                SignTrainerUIFragment.this.signQuestionText.setText(R.string.noSignWasFound);
            } else {
                SignTrainerUIFragment.this.currentSign = result;
                setupVideoView(SignTrainerUIFragment.this.currentSign);
                SignTrainerUIFragment.this.toggleAnswerTextAndButtonsVisibility(View.INVISIBLE);
            }
        }

    }

    /**
     * Update the learning progress for a sign in the database.
     */
    private class UpdateLearningProgressTask extends AsyncTask<Sign, Void, Void> {

        private final Context context;

        public UpdateLearningProgressTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Sign... params) {
            Log.d(UpdateLearningProgressTask.class.getSimpleName(), "doInBackground " + hashCode());
            Validate.exclusiveBetween(0, 2, params.length, "Exactly one sign as a parameter allowed.");
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            signDAO.update(params[0]);
            signDAO.close();
            return null;
        }
    }
}

