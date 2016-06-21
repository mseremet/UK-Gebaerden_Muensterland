package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_video_view;

import android.app.Fragment;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
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
public abstract class AbstractSignVideoFragment extends Fragment {

    private static final double MAXMIMUM_VIDEO_HEIGHT_ON_LANDSCAPE = 0.4;
    private static final double MAXIMUM_VIDEO_WIDTH_ON_PORTRAIT = 0.8;
    private final static String TAG = AbstractSignVideoFragment.class.getSimpleName();
    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String SLASH = "/";
    private static final String RAW = "raw";
    protected VideoView videoView;
    protected ProgressBar progressBar;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isSetupVideoViewSuccessful(final Sign sign, final SOUND sound, final CONTROLS controls) {
        initializeMediaController();
        final String mainActivityPackageName = getActivity().getPackageName();
        final int signIdentifier = getActivity().getResources().getIdentifier(sign.getName(), RAW, mainActivityPackageName);
        if (0 == signIdentifier) {
            return false;
        }
        final Uri uri = Uri.parse(ANDROID_RESOURCE + mainActivityPackageName + SLASH + signIdentifier);
        if (!isVideoViewDimensionSetToMatchVideoMetadata(this.videoView, uri)) {
            return false;
        }
        this.videoView.setVideoURI(uri);
        this.videoView.requestFocus();
        this.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                AbstractSignVideoFragment.this.progressBar.setVisibility(View.GONE);
                if (sound.equals(SOUND.OFF)) {
                    mp.setVolume(0f, 0f);
                }
                AbstractSignVideoFragment.this.videoView.start();
                AbstractSignVideoFragment.this.videoView.setContentDescription(getActivity()
                        .getString(R.string.videoIsPlaying) + ": " + sign.getName());
                Log.d(TAG, String.format("Actual width: %s, Actual height: %s",
                        AbstractSignVideoFragment.this.videoView.getWidth(),
                        AbstractSignVideoFragment.this.videoView.getHeight()));
                // Set the MediaController to null so the controls are not 'popping up'
                // when the video plays for the first time.
                AbstractSignVideoFragment.this.videoView.setMediaController(null);
            }
        });
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (controls.equals(CONTROLS.SHOW)) {
                    initializeMediaController();
                }
            }
        });
        return true;
    }

    private void initializeMediaController() {
        final MediaController mediaController = new MediaController(getActivity(), false);
        mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(mediaController);
    }

    private boolean isVideoViewDimensionSetToMatchVideoMetadata(VideoView videoView, Uri uri) {
        String metadataVideoWidth;
        String metadataVideoHeight;
        try {
            final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(getActivity(), uri);
            metadataVideoWidth = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            metadataVideoHeight = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            metaRetriever.release();
            Validate.notEmpty(metadataVideoWidth);
            Validate.notEmpty(metadataVideoHeight);
        } catch (NullPointerException | IllegalArgumentException ex) {
            return false;
        }
        final double videoWidth = Double.valueOf(metadataVideoWidth);
        final double videoHeight = Double.valueOf(metadataVideoHeight);
        final double videoRatio = videoWidth / videoHeight;
        Log.d(TAG, String.format("videoWidth: %s, videoHeight: %s, videoRatio: %s", videoWidth, videoHeight, videoRatio));
        boolean isOrientationPortrait = Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, String.format("displayHeight: %s, displayWidth: %s", displayHeight, displayWidth));
        final double desiredVideoWidth, desiredVideoHeight;
        if (isOrientationPortrait) {
            desiredVideoWidth = displayWidth * MAXIMUM_VIDEO_WIDTH_ON_PORTRAIT;
            desiredVideoHeight = 1 / (videoRatio / desiredVideoWidth);
            Log.d(TAG, String.format("OrientationPortrait: desiredVideoWidth: %s, desiredVideoHeight: %s", desiredVideoWidth, desiredVideoHeight));
        } else { // orientation is Landscape
            desiredVideoHeight = displayHeight * MAXMIMUM_VIDEO_HEIGHT_ON_LANDSCAPE;
            desiredVideoWidth = desiredVideoHeight * videoRatio;
            Log.d(TAG, String.format("OrientationLandscape: desiredVideoWidth: %s, desiredVideoHeight: %s", desiredVideoWidth, desiredVideoHeight));
        }
        final ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = (int) desiredVideoWidth;
        layoutParams.height = (int) desiredVideoHeight;
        return true;
    }

    public enum SOUND {ON, OFF}

    public enum CONTROLS {SHOW, HIDE}

}
