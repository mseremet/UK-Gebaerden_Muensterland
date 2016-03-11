package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;

public class SignSearchActivity extends AppCompatActivity implements SignSearchTaskFragment.TaskCallbacks {

    private static final java.lang.String KEY_QUERY = "sign_browser_search_query";
    private static final String TAG_TASK_FRAGMENT = "sign_browser_search_task_fragment";
    private SignSearchTaskFragment signSearchTaskFragment;
    private String query = StringUtils.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        if (null != savedInstanceState) {
            this.query = savedInstanceState.getString(KEY_QUERY);
        } else {
            final Intent intent = getIntent();
            if (!(Intent.ACTION_SEARCH.equals(intent.getAction()))) {
                return;
            }
            this.query = intent.getStringExtra(SearchManager.QUERY);
        }
        setupRecyclerView();
        setupSupportActionBar();
        final FragmentManager fm = getFragmentManager();
        this.signSearchTaskFragment = (SignSearchTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (null == signSearchTaskFragment) {
            signSearchTaskFragment = new SignSearchTaskFragment();
            final FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(signSearchTaskFragment, TAG_TASK_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    private void setupRecyclerView() {
        final RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        recyclerView.setHasFixedSize(true); // performance fix
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SignSearchAdapter(new ArrayList<Sign>()));
    }

    private void setupSupportActionBar() {
        final ActionBar supportActionBar = getSupportActionBar();
        if (null == supportActionBar) {
            throw new IllegalStateException("SupportActionBar is null. Should have been set in " +
                    "onCreate().");
        }
        supportActionBar.setTitle(getResources().getString(R.string.search_results) + StringUtils.SPACE + this.query);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!signSearchTaskFragment.isRunning()) {
            this.signSearchTaskFragment.start(this, query);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (signSearchTaskFragment.isRunning()) {
            this.signSearchTaskFragment.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_sign_browser_search, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_QUERY, this.query);
    }

    @Override
    public void onPreExecute() {/*no-op*/}

    @Override
    public void onProgressUpdate(int percent) {/*no-op*/}

    @Override
    public void onCancelled() {/*no-op*/}

    @Override
    public void onPostExecute(List<Sign> result) {
        // FIXME: After savedInstance has been called, this.recyclerview is null here, despite being
        // FIXME: set in the onCreated() method. Therefore a findViewById is necessary.
        final RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        if (null == mRecyclerView) {
            throw new IllegalStateException("mRecyclerView is null");
        }
        mRecyclerView.swapAdapter(new SignSearchAdapter(result), false);
    }

}
