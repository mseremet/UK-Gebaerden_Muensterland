package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView signQuestionText;
    private Button solveQuestionButton;
    private Sign currentSign = null;
    private LoadRandomSignTask loadRandomSignTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.trainer_fragment, container, false);
        this.videoView = (VideoView) view.findViewById(R.id.signTrainerVideoView);
        this.signQuestionText = (TextView) view.findViewById(R.id.signTrainerQuestionText);
        this.solveQuestionButton = (Button) view.findViewById(R.id.signTrainerSolveQuestionButton);
        this.progressBar = (ProgressBar) view.findViewById(R.id.signTrainerVideoLoadingProgressBar);
        this.videoView.setContentDescription(getActivity().getString(R.string.videoIsLoading));
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
                return; // Don't depend on any code below to have been executed.
            }
        }
        this.loadRandomSignTask = new LoadRandomSignTask(getActivity());
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
            if (status.equals(AsyncTask.Status.PENDING)|| status.equals(AsyncTask.Status.RUNNING)) {
                this.loadRandomSignTask.cancel(INTERRUPT_IF_RUNNING);
            }
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        if (null != this.currentSign) {
            outState.putParcelable(KEY_CURRENT_SIGN, this.currentSign);
        }
    }

    private void setupVideoView(final Sign sign) {
        final MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(this.videoView);
        mediaController.hide();
        this.videoView.setMediaController(mediaController);
        final int identifier = getActivity().getResources().getIdentifier(sign.getName(), RAW, getActivity().getPackageName());
        if (0 == identifier) {
            this.signQuestionText.setText(R.string.videoCouldNotBeLoaded);
            this.progressBar.setVisibility(View.GONE);
            this.videoView.setVisibility(View.GONE);
            // TODO: Show button next sign
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
            }
        }

    }
}

