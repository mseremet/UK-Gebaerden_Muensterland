package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_browser, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.recyclerView = (RecyclerView) getActivity().findViewById(R.id.signRecyclerView);
        if (null == this.recyclerView) {
            throw new IllegalStateException("Recycler view not found by MainActivity. Should have " +
                    "been created by fragment's onCreateView() method.");
        }
        this.recyclerView.setHasFixedSize(true); // performance fix
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(new SignBrowserAdapter(new ArrayList<Sign>()));
        new LoadSignsTask().execute();
    }

    private class LoadSignsTask extends AsyncTask<Void, Void, List<Sign>> {

        @Override
        protected List<Sign> doInBackground(Void... params) {
            final SignDAO signDAO = SignDAO.getInstance(getActivity());
            signDAO.open();
            final List<Sign> signs = signDAO.read();
            signDAO.close();
            return signs;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            recyclerView.swapAdapter(new SignBrowserAdapter(result),true);
        }
    }

}

