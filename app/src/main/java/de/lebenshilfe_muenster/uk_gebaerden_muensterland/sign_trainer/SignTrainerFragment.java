package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.apache.commons.lang3.Validate;

import java.text.DecimalFormat;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_video_view.AbstractSignVideoFragment;

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
public class SignTrainerFragment extends AbstractSignVideoFragment {

    private static final String TAG = SignTrainerFragment.class.getSimpleName();
    private static final String KEY_CURRENT_SIGN = "KEY_CURRENT_SIGN";
    private static final boolean INTERRUPT_IF_RUNNING = true;
    private static final String KEY_ANSWER_VISIBLE = "KEY_ANSWER_VISIBLE";
    private TextView signQuestionText;
    private Button solveQuestionButton;
    private Sign currentSign = null;
    private LoadRandomSignTask loadRandomSignTask;
    private TextView signAnswerTextView;
    private TextView signMnemonicTextView;
    private TextView signLearningProgressTextView;
    private TextView signHowHardWasQuestionTextView;
    private TextView signTrainerExplanationTextView;
    private Button questionWasEasyButton;
    private Button questionWasFairButton;
    private Button questionWasHardButton;
    private View[] questionViews;
    private View[] answerViews;

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
        this.signHowHardWasQuestionTextView = (TextView) view.findViewById(R.id.signTrainerHowHardWasTheQuestion);
        this.signTrainerExplanationTextView = (TextView) view.findViewById(R.id.signTrainerExplanation);
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
        this.questionViews = new View[]{this.signQuestionText, this.videoView, this.solveQuestionButton};
        this.answerViews = new View[]{this.signAnswerTextView, this.signMnemonicTextView,
                this.signLearningProgressTextView, this.signHowHardWasQuestionTextView, this.signTrainerExplanationTextView,
                this.questionWasEasyButton, this.questionWasFairButton, this.questionWasHardButton};
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
                if (!isSetupVideoViewSuccessful(this.currentSign, SOUND.OFF, CONTROLS.HIDE)) {
                    handleVideoCouldNotBeLoaded();
                    return;
                }
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
            this.loadRandomSignTask.execute(this.currentSign);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart " + hashCode());
        super.onStart();
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

    private void handleVideoCouldNotBeLoaded() {
        this.signQuestionText.setText(R.string.videoCouldNotBeLoaded);
        toggleAnswerTextAndButtonsVisibility(View.INVISIBLE);
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
        this.signHowHardWasQuestionTextView.setText(getString(R.string.howHardWasTheQuestion));
        this.signTrainerExplanationTextView.setText(getString(R.string.signTrainerExplanation));
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
            for (final View questionView : this.questionViews) {
                questionView.setVisibility(View.GONE);
            }
        } else {
            for (final View questionView : this.questionViews) {
                questionView.setVisibility(View.VISIBLE);
            }
        }
        for (final View answerView: this.answerViews) {
            answerView.setVisibility(visibility);
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
                SignTrainerFragment.this.signQuestionText.setText(R.string.noSignWasFound);
            } else {
                SignTrainerFragment.this.currentSign = result;
                if (!isSetupVideoViewSuccessful(SignTrainerFragment.this.currentSign, SOUND.OFF, CONTROLS.HIDE)) {
                    handleVideoCouldNotBeLoaded();
                    return;
                }
                SignTrainerFragment.this.toggleAnswerTextAndButtonsVisibility(View.INVISIBLE);
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

