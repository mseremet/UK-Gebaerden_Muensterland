package de.lebenshilfe_muenster.uk_gebaerden_muensterland.about_signs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

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
public class AboutSignsFragment extends Fragment {

    private static final String TAG = AboutSignsFragment.class.getSimpleName();
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.about_signs_fragment, container, false);
        this.webView = (WebView) view.findViewById(R.id.aboutSignsWebView);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDefaultTextEncodingName("UTF-8");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " + hashCode());
        super.onActivityCreated(savedInstanceState);
        this.webView.loadUrl("file:///android_res/raw/about_signs.html");
    }

}