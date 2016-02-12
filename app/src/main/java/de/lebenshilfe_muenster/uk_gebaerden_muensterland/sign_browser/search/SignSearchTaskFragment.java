package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignSearchTaskFragment extends Fragment {

    private TaskCallbacks taskCallbacks;
    private SearchSignsTask searchSignsTask;
    private boolean running;

    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute(List<Sign> result);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof SignSearchTaskFragment.TaskCallbacks)) {
            throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
        }
        taskCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancel();
    }

    /**
     * Start the background task.
     * @param context i. e. an Activity
     * @param query the string to match the name_locale_de against
     */
    public void start(Context context, String query) {
        if (!running) {
            searchSignsTask = new SearchSignsTask(context);
            searchSignsTask.execute(query);
            running = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (running) {
            searchSignsTask.cancel(false);
            searchSignsTask = null;
            running = false;
        }
    }

    /**
     * Returns the current state of the background task.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * The first string parameter is the query to search in the name_locale_de for.
     */
    private class SearchSignsTask extends AsyncTask<String, Void, List<Sign>> {

        private final Context context;

        public SearchSignsTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            taskCallbacks.onPreExecute();
            running = true;
        }

        @Override
        protected List<Sign> doInBackground(String... params) {
            final List<Sign> signs = new ArrayList<>();
            if (isCancelled()) {
                return signs;
            }
            if (1 == params.length) {
                final SignDAO signDAO = SignDAO.getInstance(this.context);
                signDAO.open();
                signs.addAll(signDAO.read(params[0]));
                signDAO.close();
            }
            return signs;
        }

        @Override
        protected void onProgressUpdate(Void... ignore) {
            taskCallbacks.onProgressUpdate(0);
        }

        @Override
        protected void onCancelled() {
            taskCallbacks.onCancelled();
            running = false;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            taskCallbacks.onPostExecute(result);
            running = false;
        }

    }

}
