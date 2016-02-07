package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;
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
public class SignBrowserFragment extends Fragment {

    public static final String SHOW_STARRED_ONLY = "SHOW_STARRED_ONLY";
    public static final String CLASS_NAME = SignBrowserFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private boolean showStarredOnly = false;
    private AsyncTask<Boolean, Void, List<Sign>> loadSignsTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_browser, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        this.recyclerView = (RecyclerView) getActivity().findViewById(R.id.signRecyclerView);
        this.recyclerView.setHasFixedSize(true); // performance fix
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(new SignBrowserAdapter(new ArrayList<Sign>(), null));
        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        this.showStarredOnly = sharedPref.getBoolean(SHOW_STARRED_ONLY, false);
        this.loadSignsTask = new LoadSignsTask(getActivity()).execute(this.showStarredOnly);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        if (item.getItemId() == R.id.action_toggle_starred) {
            if (!this.showStarredOnly) {
                this.showStarredOnly = true;
                item.setIcon(R.drawable.ic_sign_browser_grade_checked);
            } else {
                this.showStarredOnly = false;
                item.setIcon(R.drawable.ic_sign_browser_grade);
            }
            final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHOW_STARRED_ONLY, this.showStarredOnly);
            editor.commit();
            this.loadSignsTask = new LoadSignsTask(getActivity()).execute(this.showStarredOnly);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (null != this.loadSignsTask) {
            final AsyncTask.Status status = this.loadSignsTask.getStatus();
            if (status.equals(AsyncTask.Status.PENDING)
                    || status.equals(AsyncTask.Status.RUNNING)) {
                this.loadSignsTask.cancel(true);
            }
        }
        super.onPause();
    }

    /**
     * If the first parameter Boolean is true, only starred signs will be loaded.
     */
    private class LoadSignsTask extends AsyncTask<Boolean, Void, List<Sign>> {

        private final Context context;

        public LoadSignsTask(Context context) {
             this.context = context;
        }

        @Override
        protected List<Sign> doInBackground(Boolean... params) {
            List<Sign> signs = new ArrayList<>();
            if (isCancelled()) {
                return signs;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            signs = signDAO.read();
            signDAO.close();
            if (1 == params.length) {
                if (params[0].booleanValue()) {
                    final List<Sign> signsToRemove = new ArrayList<>();
                    for (Sign sign : signs) {
                        if (!sign.isStarred()) {
                            signsToRemove.add(sign);
                        }
                    }
                    signs.removeAll(signsToRemove);
                }
            }
            return signs;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            if (null != SignBrowserFragment.this) {
                SignBrowserFragment.this.recyclerView.swapAdapter(new SignBrowserAdapter(result, this.context), false);
            }
        }
    }
}

