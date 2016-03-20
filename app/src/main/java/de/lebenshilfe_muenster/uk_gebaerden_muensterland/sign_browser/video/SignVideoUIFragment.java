package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SignVideoUIFragment extends Fragment {

    public static final String SIGN_TO_SHOW = "sign_to_show";
    private static final String TAG = SignVideoUIFragment.class.getSimpleName();
    private static final String VIDEO_PLAYBACK_POSITION = "VIDEO_PLAYBACK_POSITION";
    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String SLASH = "/";
    private static final String RAW = "raw";
    private VideoView videoView;
    private ProgressBar progressBar;
    private int position;
    private TextView signVideoName;
    private TextView signVideoMnemonic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.video_fragment_view, container, false);
        this.signVideoName = (TextView) view.findViewById(R.id.signVideoName);
        this.videoView = (VideoView) view.findViewById(R.id.signVideoView);
        this.signVideoMnemonic = (TextView) view.findViewById(R.id.signVideoMnemonic);
        this.progressBar = (ProgressBar) view.findViewById(R.id.signVideoLoadingProgressBar);
        this.videoView.setContentDescription(getActivity().getString(R.string.videoIsLoading));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            this.position = savedInstanceState.getInt(VIDEO_PLAYBACK_POSITION);
        }
        final Sign sign = getArguments().getParcelable(SIGN_TO_SHOW);
        if (null == sign) {
            throw new IllegalArgumentException("No sign to show provided via fragment arguments.");
        }
        this.signVideoName.setText(sign.getNameLocaleDe());
        this.signVideoMnemonic.setText(sign.getMnemonic());
        final MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(this.videoView);
        mediaController.hide();
        this.videoView.setMediaController(mediaController);
        final int identifier = getActivity().getResources().getIdentifier(sign.getName(), RAW, getActivity().getPackageName());
        if (0 == identifier) {
            this.signVideoName.setText(R.string.videoCouldNotBeLoaded);
            this.progressBar.setVisibility(View.GONE);
            this.videoView.setVisibility(View.GONE);
            return;
        }
        this.videoView.setVideoURI(Uri.parse(ANDROID_RESOURCE + getActivity().getPackageName() + SLASH + identifier));
        this.videoView.requestFocus();
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                SignVideoUIFragment.this.progressBar.setVisibility(View.GONE);
                SignVideoUIFragment.this.videoView.seekTo(position);
                SignVideoUIFragment.this.videoView.start();
                SignVideoUIFragment.this.videoView.setContentDescription(getActivity().getString(R.string.videoIsPlaying) + ": " + sign.getName());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
        if (null != this.videoView) {
            this.videoView.pause();
            savedInstanceState.putInt(VIDEO_PLAYBACK_POSITION, this.videoView.getCurrentPosition());
        }

    }

}

