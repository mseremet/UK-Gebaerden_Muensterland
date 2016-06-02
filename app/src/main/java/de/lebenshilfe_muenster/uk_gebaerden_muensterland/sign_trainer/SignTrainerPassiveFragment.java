package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.apache.commons.lang3.Validate;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;

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
public class SignTrainerPassiveFragment extends AbstractSignTrainerFragment {

    private static final String TAG = SignTrainerPassiveFragment.class.getSimpleName();
    private static final String KEY_CURRENT_SIGN = "KEY_CURRENT_SIGN";
    private static final boolean INTERRUPT_IF_RUNNING = true;
    private static final String KEY_ANSWER_VISIBLE = "KEY_ANSWER_VISIBLE";
    private Button solveQuestionButton;
    private LoadRandomSignTask loadRandomSignTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.trainer_passive_fragment, container, false);
        setHasOptionsMenu(true);
        this.videoView = (VideoView) view.findViewById(R.id.signTrainerVideoView);
        this.signQuestionText = (TextView) view.findViewById(R.id.signTrainerQuestionText);
        this.solveQuestionButton = (Button) view.findViewById(R.id.signTrainerSolveQuestionButton);
        this.solveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnSolveQuestionButton();
            }
        });
        initializeAnswerViews(view);
        this.progressBar = (ProgressBar) view.findViewById(R.id.signTrainerVideoLoadingProgressBar);
        this.videoView.setContentDescription(getActivity().getString(R.string.videoIsLoading));
        this.questionViews = new View[]{this.signQuestionText, this.videoView, this.solveQuestionButton};
        this.answerViews = new View[]{this.signAnswerTextView, this.signMnemonicTextView,
                this.signLearningProgressTextView, this.signHowHardWasQuestionTextView, this.signTrainerExplanationTextView,
                this.questionWasEasyButton, this.questionWasFairButton, this.questionWasHardButton};
        setVisibility(this.questionViews, View.VISIBLE);
        setVisibility(this.answerViews, View.GONE);
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
                setVisibility(this.questionViews, View.GONE);
                setVisibility(this.answerViews, View.VISIBLE);
                setAnswerTextViews();
            } else {
                setVisibility(this.questionViews, View.VISIBLE);
                setVisibility(this.answerViews, View.GONE);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu " + hashCode());
        inflater.inflate(R.menu.options_sign_trainer, menu);
        final MenuItem item = menu.findItem(R.id.action_toggle_learning_mode);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        if (null != this.answerViews) {
            Validate.notEmpty(this.answerViews, "AnswerViews should always contain at least one view!");
            final boolean answerVisible = View.VISIBLE == this.answerViews[0].getVisibility();
            outState.putBoolean(KEY_ANSWER_VISIBLE, answerVisible);
        } else {
            outState.putBoolean(KEY_ANSWER_VISIBLE, Boolean.FALSE);
        }
        if (null != this.currentSign) {
            outState.putParcelable(KEY_CURRENT_SIGN, this.currentSign);
        }
    }

    @Override
    protected void handleClickOnSolveQuestionButton() {
        Log.d(TAG, "handleClickOnSolveQuestionButton " + hashCode());
        setVisibility(this.questionViews, View.GONE);
        setVisibility(this.answerViews, View.VISIBLE);
        setAnswerTextViews();
    }

}

