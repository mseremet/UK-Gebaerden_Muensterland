package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

/**
 * Created by mtonhaeuser on 26.03.2016.
 */
public class SignTrainerTaskFragment extends Fragment {

    private static final String TAG = SignTrainerTaskFragment.class.getSimpleName();
    private TaskCallbacks taskCallbacks;
    private LoadRandomSignTask loadRandomSignTask;
    private boolean running;

    interface TaskCallbacks {
        @SuppressWarnings("EmptyMethod")
        void onPreExecute();

        @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
        void onProgressUpdate(int percent);

        @SuppressWarnings("EmptyMethod")
        void onCancelled();

        void onPostExecute(Sign result);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach " + hashCode());
        super.onAttach(context);
        Validate.isInstanceOf(TaskCallbacks.class, getTargetFragment(), "Target fragment must implement the TaskCallbacks interface.");
        taskCallbacks = (TaskCallbacks) getTargetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate " + hashCode());
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy " + hashCode());
        super.onDestroy();
        cancel();
    }

    /**
     * Start the background task.
     *
     * @param context     i. e. an Activity
     * @param currentSign
     */
    public void start(Context context, Sign currentSign) {
        Log.d(TAG, "start " + hashCode());
        if (!running) {
            loadRandomSignTask = new LoadRandomSignTask(context);
            loadRandomSignTask.execute(currentSign);
            running = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        Log.d(TAG, "cancel " + hashCode());
        if (running) {
            loadRandomSignTask.cancel(false);
            loadRandomSignTask = null;
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
     * Reads a random sign from the database. Will return null if the task is cancelled. The current
     * sign can be provided as a parameter or be null, if there is no current sign.
     */
    private class LoadRandomSignTask extends AsyncTask<Sign, Void, Sign> {

        private final Context context;

        public LoadRandomSignTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "onPreExecute " + hashCode());
            taskCallbacks.onPreExecute();
            running = true;
        }

        @Override
        protected Sign doInBackground(Sign... params) {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "doInBackground " + hashCode());
            Validate.inclusiveBetween(0, 1, params.length, "Only null or one sign as a parameter allowed.");
            if (isCancelled()) {
                return null;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            Sign sign;
            if (1 == params.length && null != params[0]) { // current sign provided via parameters
                sign = signDAO.readRandomSign(params[0]);
            } else {
                sign = signDAO.readRandomSign(null);
            }
            signDAO.close();
            return sign;
        }

        @Override
        protected void onProgressUpdate(Void... ignore) {
            Log.d(TAG, "onProgressUpdate " + hashCode());
            taskCallbacks.onProgressUpdate(0);
        }

        @Override
        protected void onCancelled() {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "onCancelled " + hashCode());
            taskCallbacks.onCancelled();
            running = false;
        }

        @Override
        protected void onPostExecute(Sign result) {
            Log.d(LoadRandomSignTask.class.getSimpleName(), "onPostExecute " + hashCode());
            taskCallbacks.onPostExecute(result);
            running = false;
        }

    }

}
