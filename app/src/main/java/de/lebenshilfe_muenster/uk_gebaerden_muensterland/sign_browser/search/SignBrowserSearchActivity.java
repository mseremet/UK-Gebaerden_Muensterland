package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

public class SignBrowserSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AsyncTask<String, Void, List<Sign>> loadSignsByNameTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_browser_search);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchForSignWithName(query);
            final ActionBar supportActionBar = getSupportActionBar();
            if (null == supportActionBar) {
                throw new IllegalStateException("SupportActionBar is null. Should have been set in " +
                        "MainActivity.onCreate().");
            } else {
                supportActionBar.setTitle(getResources().getString(R.string.search_results) + StringUtils.SPACE + query);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_sign_browser_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onPause() {
        if (null != this.loadSignsByNameTask) {
            final AsyncTask.Status status = this.loadSignsByNameTask.getStatus();
            if (status.equals(AsyncTask.Status.PENDING)
                    || status.equals(AsyncTask.Status.RUNNING)) {
                this.loadSignsByNameTask.cancel(true);
            }
        }
        super.onPause();
    }

    private void searchForSignWithName(String query) {
        this.recyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        this.recyclerView.setHasFixedSize(true); // performance fix
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(new SignBrowserSearchAdapter(new ArrayList<Sign>()));
        this.loadSignsByNameTask = new LoadSignsByNameTask(this).execute(query);
    }

    private class LoadSignsByNameTask extends AsyncTask<String, Void, List<Sign>> {

        private final Context context;

        public LoadSignsByNameTask(Context context) {
            this.context = context;
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
        protected void onPostExecute(List<Sign> result) {
            if (null != SignBrowserSearchActivity.this) {
                SignBrowserSearchActivity.this.recyclerView.swapAdapter(new SignBrowserSearchAdapter(result), true);
            }
        }
    }
}
