package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video;

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

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
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
public class SignVideoFragment extends AbstractSignVideoFragment {

    public static final String SIGN_TO_SHOW = "sign_to_show";
    private static final String TAG = SignVideoFragment.class.getSimpleName();
    private TextView signVideoName;
    private TextView signVideoMnemonic;
    @SuppressWarnings("FieldCanBeLocal")
    private Button backToSignBrowserButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View view = inflater.inflate(R.layout.video_fragment_view, container, false);
        this.signVideoName = (TextView) view.findViewById(R.id.signVideoName);
        this.videoView = (VideoView) view.findViewById(R.id.signVideoView);
        this.signVideoMnemonic = (TextView) view.findViewById(R.id.signVideoMnemonic);
        this.backToSignBrowserButton = (Button) view.findViewById(R.id.backToSignBrowserButton);
        this.backToSignBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        this.progressBar = (ProgressBar) view.findViewById(R.id.signVideoLoadingProgressBar);
        this.videoView.setContentDescription(getActivity().getString(R.string.videoIsLoading));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        final Sign sign = getArguments().getParcelable(SIGN_TO_SHOW);
        Validate.notNull(sign, "No sign to show provided via fragment arguments.");
        this.signVideoName.setText(sign.getNameLocaleDe());
        this.signVideoMnemonic.setText(sign.getMnemonic());
        if (!isSetupVideoViewSuccessful(sign, SOUND.ON, CONTROLS.SHOW)) {
            this.signVideoName.setText(getString(R.string.videoCouldNotBeLoaded));
            this.signVideoMnemonic.setVisibility(View.INVISIBLE);
        }
    }

}

