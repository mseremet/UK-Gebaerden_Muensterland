package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public abstract class AbstractSignTrainerFragment extends AbstractSignVideoFragment {
    private static final String TAG = AbstractSignTrainerFragment.class.getSimpleName();
    protected Sign currentSign = null;
    protected TextView signAnswerTextView;
    protected TextView signMnemonicTextView;
    protected TextView signLearningProgressTextView;
    protected TextView signHowHardWasQuestionTextView;
    protected TextView signTrainerExplanationTextView;
    protected Button questionWasEasyButton;
    protected Button questionWasFairButton;
    protected Button questionWasHardButton;
    protected TextView signQuestionText;
    protected View[] questionViews;
    protected View[] answerViews;

    protected void initializeAnswerViews(View view) {
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
    }

    protected void handleVideoCouldNotBeLoaded() {
        this.signQuestionText.setText(R.string.videoCouldNotBeLoaded);
        setVisibility(this.questionViews, View.VISIBLE);
        setVisibility(this.answerViews, View.GONE);
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

    protected void setVisibility(View[] views, int visibility) {
        Validate.notNull(views, "View array is null!");
        if (View.VISIBLE != visibility && View.INVISIBLE != visibility && View.GONE != visibility) {
            throw new IllegalArgumentException("Visibility can either be View.VISIBLE, VIEW.INVISIBLE or View.GONE, but was: " + visibility);
        }
        for (View view: views) {
            view.setVisibility(visibility);
        }
    }

    protected void setAnswerTextViews() {
        this.signAnswerTextView.setText(this.currentSign.getNameLocaleDe());
        this.signMnemonicTextView.setText(this.currentSign.getMnemonic());
        final DecimalFormat decimalFormat = new DecimalFormat(" 0;-0");
        this.signLearningProgressTextView.setText(getString(R.string.learningProgress) + ": " +
                decimalFormat.format(this.currentSign.getLearningProgress()));
        this.signHowHardWasQuestionTextView.setText(getString(R.string.howHardWasTheQuestion));
        this.signTrainerExplanationTextView.setText(getString(R.string.signTrainerExplanation));
    }

    protected abstract void handleClickOnSolveQuestionButton();

    /**
     * Reads a random sign from the database. Will return null if the task is cancelled. The current
     * sign can be provided as a parameter or be null, if there is no current sign.
     */
    protected class LoadRandomSignTask extends AsyncTask<Sign, Void, Sign> {

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
                AbstractSignTrainerFragment.this.signQuestionText.setText(R.string.noSignWasFound);
            } else {
                AbstractSignTrainerFragment.this.currentSign = result;
                if (!isSetupVideoViewSuccessful(AbstractSignTrainerFragment.this.currentSign, SOUND.OFF, CONTROLS.HIDE)) {
                    handleVideoCouldNotBeLoaded();
                    return;
                }
                setVisibility(AbstractSignTrainerFragment.this.questionViews, View.VISIBLE);
                setVisibility(AbstractSignTrainerFragment.this.answerViews, View.GONE);
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
            Validate.isTrue(1 ==  params.length, "Exactly one sign as a parameter allowed.");
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            signDAO.update(params[0]);
            signDAO.close();
            return null;
        }
    }
}
