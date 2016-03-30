package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

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
public class SignBrowserUIFragment extends Fragment implements SignBrowserTaskFragment.TaskCallbacks {

    private static final String TAG = SignBrowserUIFragment.class.getSimpleName();
    private static final String KEY_SHOW_STARRED_ONLY = "sign_browser_show_starred_only";
    private static final String TAG_TASK_FRAGMENT = "sign_browser_task_fragment";

    private boolean showStarredOnly = false;
    private SignBrowserTaskFragment signBrowserTaskFragment;
    private OnSignClickedListener onSignClickedListener = null;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach " + hashCode());
        super.onAttach(context);
        try {
            this.onSignClickedListener = (OnSignClickedListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSignClickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.browser_fragment, container, false);
        setHasOptionsMenu(true);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.signRecyclerView);
        recyclerView.setHasFixedSize(true); // performance fix
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SignBrowserAdapter(this, getActivity(), new ArrayList<Sign>()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " + hashCode());
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.showStarredOnly = savedInstanceState.getBoolean(KEY_SHOW_STARRED_ONLY);
        }
        final FragmentManager fm = getActivity().getFragmentManager();
//        final FragmentManager fm = getChildFragmentManager();
        this.signBrowserTaskFragment = (SignBrowserTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (null == this.signBrowserTaskFragment) {
            this.signBrowserTaskFragment = new SignBrowserTaskFragment();
            this.signBrowserTaskFragment.setTaskCallbacks(this);
            this.signBrowserTaskFragment.setTargetFragment(this, 0);
            final FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(signBrowserTaskFragment, TAG_TASK_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart " + hashCode());
        super.onStart();
        if (!this.signBrowserTaskFragment.isRunning()) {
            this.signBrowserTaskFragment.start(getActivity(), this.showStarredOnly);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause " + hashCode());
        super.onPause();
        if (this.signBrowserTaskFragment.isRunning()) {
            this.signBrowserTaskFragment.cancel();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop " + hashCode());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy " + hashCode());
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach " + hashCode());
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu " + hashCode());
        inflater.inflate(R.menu.options_sign_browser, menu);
        final MenuItem item = menu.findItem(R.id.action_toggle_starred);
        if (this.showStarredOnly) {
            item.setIcon(R.drawable.ic_sign_browser_grade_checked);
        } else {
            item.setIcon(R.drawable.ic_sign_browser_grade);
        }
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + hashCode());
        if (item.getItemId() == R.id.action_toggle_starred) {
            if (!this.showStarredOnly) {
                this.showStarredOnly = true;
                item.setIcon(R.drawable.ic_sign_browser_grade_checked);
            } else {
                this.showStarredOnly = false;
                item.setIcon(R.drawable.ic_sign_browser_grade);
            }
            signBrowserTaskFragment.start(getActivity(), this.showStarredOnly);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SHOW_STARRED_ONLY, this.showStarredOnly);
    }

    public void onTxtSignNameClicked(Sign sign) {
        Log.d(TAG, "onTxtSignNameClicked " + hashCode());
        Validate.notNull(this.onSignClickedListener, "Parent activity has to implement the OnSignClickedListener");
        this.onSignClickedListener.onSignSelected(sign);
    }

    // Callback methods from SignBrowserTaskFragment
    @Override
    public void onPreExecute() {/*no-op*/}

    @Override
    public void onProgressUpdate(int percent) {/*no-op*/}

    @Override
    public void onCancelled() {/*no-op*/}

    @Override
    public void onPostExecute(List<Sign> result) {
        Log.d(TAG, "onPostExecute " + hashCode());
        // FIXME: After savedInstance has been called, this.recyclerView is null here, despite being
        // FIXME: set in the onActivityCreated() method. Therefore a findViewById is necessary.
        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.signRecyclerView);
        Validate.notNull(recyclerView, "RecyclerView is null");
        recyclerView.swapAdapter(new SignBrowserAdapter(this, getActivity(), result), true);
    }

    // Has to implemented by parent activity.
    public interface OnSignClickedListener {
        void onSignSelected(Sign sign);
    }


}

