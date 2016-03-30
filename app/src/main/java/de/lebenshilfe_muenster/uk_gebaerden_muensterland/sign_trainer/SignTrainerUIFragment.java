package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.net.Uri;
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
public class SignTrainerUIFragment extends Fragment implements SignTrainerTaskFragment.TaskCallbacks {

    private static final String TAG = SignTrainerUIFragment.class.getSimpleName();
    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String SLASH = "/";
    private static final String RAW = "raw";
    private static final String TAG_TASK_FRAGMENT = "SIGN_TRAINER_TASK_FRAGMENT";
    private static final String KEY_CURRENT_SIGN = "KEY_CURRENT_SIGN";
    private VideoView videoView;
    private ProgressBar progressBar;
    private TextView signQuestionText;
    private Button solveQuestionButton;
    private Sign currentSign = null;
    private SignTrainerTaskFragment signTrainerTaskFragment;

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
        final FragmentManager fm = getActivity().getFragmentManager();
        // FIXME
//        final FragmentManager fm = getChildFragmentManager();
        this.signTrainerTaskFragment = (SignTrainerTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (null == this.signTrainerTaskFragment) {
            this.signTrainerTaskFragment = new SignTrainerTaskFragment();
            this.signTrainerTaskFragment.setTargetFragment(this, 0);
            final FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(signTrainerTaskFragment, TAG_TASK_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart " + hashCode());
        super.onStart();
        if (!this.signTrainerTaskFragment.isRunning()) {
            this.signTrainerTaskFragment.start(getActivity(), this.currentSign);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause " + hashCode());
        super.onPause();
        if (this.signTrainerTaskFragment.isRunning()) {
            this.signTrainerTaskFragment.cancel();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        if (null != this.currentSign) {
            outState.putParcelable(KEY_CURRENT_SIGN, this.currentSign);
        }
    }

    @Override
    public void onPreExecute() {/*no-op*/}

    @Override
    public void onProgressUpdate(int percent) {/*no-op*/}

    @Override
    public void onCancelled() {/*no-op*/}

    @Override
    public void onPostExecute(final Sign result) {
        Log.d(TAG, "onPostExecute() " + hashCode());
        if (null == result) {
            this.signQuestionText.setText(R.string.noSignWasFound);
        } else {
            this.currentSign = result;
            setupVideoView(this.currentSign);
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
}

