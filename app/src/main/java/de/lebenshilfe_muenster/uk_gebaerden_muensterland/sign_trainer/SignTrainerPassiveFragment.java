package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.Validate;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_video_view.VideoSetupException;

import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.AbstractSignTrainerFragment.OnToggleLearningModeListener.LearningMode;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.trainer_passive_fragment, container, false);
        setHasOptionsMenu(true);
        initializeQuestionViews(view);
        initializeExceptionViews(view);
        initializeAnswerViews(view);
        initializeVideoViews(view);
        this.questionViews = new View[]{this.signQuestionText, this.videoView, this.solveQuestionButton};
        this.answerViews = new View[]{this.signAnswerTextView, this.signMnemonicTextView,
                this.signLearningProgressTextView, this.signHowHardWasQuestionTextView, this.signTrainerExplanationTextView,
                this.questionWasEasyButton, this.questionWasFairButton, this.questionWasHardButton};
        this.exceptionViews = new View[]{this.signTrainerExceptionMessageTextView};
        setVisibility(this.questionViews, View.VISIBLE);
        setQuestionTextViews(getString(R.string.signQuestion));
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
                try {
                    setupVideoView(this.currentSign, SOUND.OFF, CONTROLS.SHOW);
                } catch (VideoSetupException ex) {
                    handleVideoCouldNotBeLoaded(ex);
                }
            }
            final Boolean answerVisible = savedInstanceState.getBoolean(KEY_ANSWER_VISIBLE);
            Validate.notNull(answerVisible, "AnswerVisible should always be non-null in savedInstance bundle.");
            if (answerVisible && (null != this.currentSign)) {
                setVisibility(this.questionViews, View.GONE);
                setVisibility(this.exceptionViews, View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + hashCode());
        if (R.id.action_toggle_learning_mode == item.getItemId()) {
            this.onToggleLearningModeListener.toggleLearningMode(LearningMode.ACTIVE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void handleClickOnSolveQuestionButton() {
        Log.d(TAG, "handleClickOnSolveQuestionButton " + hashCode());
        setVisibility(this.questionViews, View.GONE);
        setVisibility(this.exceptionViews, View.GONE);
        setVisibility(this.answerViews, View.VISIBLE);
        setAnswerTextViews();
    }

    @Override
    protected void handleLoadRandomSignTaskOnPostExecute() {
        setVisibility(SignTrainerPassiveFragment.this.questionViews, View.VISIBLE);
        setQuestionTextViews(getString(R.string.signQuestion));
        setVisibility(SignTrainerPassiveFragment.this.answerViews, View.GONE);
        try {
            setupVideoView(this.currentSign, SOUND.OFF, CONTROLS.SHOW);
        } catch (VideoSetupException ex) {
            handleVideoCouldNotBeLoaded(ex);
        }
    }

    @Override
    protected void handleVideoCouldNotBeLoaded(VideoSetupException videoSetupException) {
        this.signQuestionText.setText(R.string.videoError);
        this.signTrainerExceptionMessageTextView.setText(videoSetupException.getMessage());
        setVisibility(this.questionViews, View.VISIBLE); // also needed for the solve button
        setVisibility(this.exceptionViews, View.VISIBLE);
        setVisibility(this.answerViews, View.GONE);
    }
}

