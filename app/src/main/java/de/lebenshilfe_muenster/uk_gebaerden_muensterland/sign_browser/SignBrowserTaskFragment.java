package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
public class SignBrowserTaskFragment extends Fragment {

    private static final String TAG = SignBrowserTaskFragment.class.getSimpleName();
    private TaskCallbacks taskCallbacks;
    private LoadSignsTask loadSignsTask;
    private boolean running;

    interface TaskCallbacks {
        @SuppressWarnings("EmptyMethod")
        void onPreExecute();
        @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
        void onProgressUpdate(int percent);
        @SuppressWarnings("EmptyMethod")
        void onCancelled();
        void onPostExecute(List<Sign> result);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach");
        super.onAttach(activity);
        if (!(getTargetFragment() instanceof TaskCallbacks)) {
            throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
        }
        taskCallbacks = (TaskCallbacks) getTargetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        cancel();
    }

    /**
     * Start the background task.
     * @param context i. e. an Activity
     * @param loadStarredOnly whether all or just the ones which are starred should be loaded.
     */
    public void start(Context context, boolean loadStarredOnly) {
        Log.d(TAG, "start");
        if (!running) {
            loadSignsTask = new LoadSignsTask(context);
            loadSignsTask.execute(loadStarredOnly);
            running = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        Log.d(TAG, "cancel");
        if (running) {
            loadSignsTask.cancel(false);
            loadSignsTask = null;
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
     * If the first parameter Boolean is true, only starred signs will be loaded.
     */
    private class LoadSignsTask extends AsyncTask<Boolean, Void, List<Sign>> {

        private final Context context;

        public LoadSignsTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.d(LoadSignsTask.class.getSimpleName(), "onPreExecute");
            taskCallbacks.onPreExecute();
            running = true;
        }

        @Override
        protected List<Sign> doInBackground(Boolean... params) {
            Log.d(LoadSignsTask.class.getSimpleName(), "doInBackground");
            List<Sign> signs = new ArrayList<>();
            if (isCancelled()) {
                return signs;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            // TODO: Replace with a read method which filters on database level
            signs = signDAO.read();
            signDAO.close();
            if (1 == params.length) {
                if (params[0]) {
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
        protected void onProgressUpdate(Void... ignore) {
            taskCallbacks.onProgressUpdate(0);
        }

        @Override
        protected void onCancelled() {
            Log.d(LoadSignsTask.class.getSimpleName(), "onCancelled");
            taskCallbacks.onCancelled();
            running = false;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            Log.d(LoadSignsTask.class.getSimpleName(), "onPostExecute");
            taskCallbacks.onPostExecute(result);
            running = false;
        }

    }

}
