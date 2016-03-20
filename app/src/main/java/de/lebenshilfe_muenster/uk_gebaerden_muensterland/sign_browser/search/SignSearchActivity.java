package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.video.SignSearchVideoActivity;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoUIFragment;

public class SignSearchActivity extends AppCompatActivity implements SignSearchTaskFragment.TaskCallbacks {

    public static final String QUERY = "sign_browser_search_query";
    private static final String TAG_TASK_FRAGMENT = "sign_browser_search_task_fragment";
    private static final String TAG = SignSearchActivity.class.getSimpleName();
    private SignSearchTaskFragment signSearchTaskFragment;
    private String query = StringUtils.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        if (null != savedInstanceState) {
            this.query = savedInstanceState.getString(QUERY);
        } else {
            final Intent intent = getIntent();
            this.query = intent.getStringExtra(SearchManager.QUERY);
            Validate.notNull(this.query, "The query supplied to this activity is null!");
        }
        setupRecyclerView();
        setupSupportActionBar();
        this.signSearchTaskFragment = (SignSearchTaskFragment) getFragmentManager().findFragmentByTag(TAG_TASK_FRAGMENT);
        if (null == this.signSearchTaskFragment) {
            initSignSearchTaskFragment();
        }
    }

    private void initSignSearchTaskFragment() {
        Log.d(TAG, "initSignSearchTaskFragment() " + this.hashCode());
        this.signSearchTaskFragment = new SignSearchTaskFragment();
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(signSearchTaskFragment, TAG_TASK_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView() " + this.hashCode());
        final RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        recyclerView.setHasFixedSize(true); // performance fix
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SignSearchAdapter(new ArrayList<Sign>(), this));
    }

    private void setupSupportActionBar() {
        Log.d(TAG, "setupSupportActionBar() " + this.hashCode());
        final ActionBar supportActionBar = getSupportActionBar();
        Validate.notNull(supportActionBar,"SupportActionBar is null. Should have been set in onCreate()." );
        supportActionBar.setTitle(getResources().getString(R.string.search_results) + StringUtils.SPACE + this.query);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() " + this.hashCode());
        super.onStart();
            if (!this.signSearchTaskFragment.isRunning()) {
                this.signSearchTaskFragment.start(this, query);
            }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()" + this.hashCode());
        super.onPause();
        if (signSearchTaskFragment.isRunning()) {
            this.signSearchTaskFragment.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()" + this.hashCode());
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
        Log.d(TAG, "onSaveInstanceState() " + this.hashCode());
        super.onSaveInstanceState(outState);
        outState.putString(QUERY, this.query);
    }

    public void onTxtSignNameClicked(Sign sign) {
        Log.d(TAG, "onTxtSignNameClicked() " + this.hashCode());
        final Intent intent = new Intent(this, SignSearchVideoActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(SignSearchActivity.QUERY, this.query);
        bundle.putParcelable(SignVideoUIFragment.SIGN_TO_SHOW, sign);
        intent.putExtra(SignSearchVideoActivity.EXTRA, bundle);
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        Log.d(TAG, "onPreExecute " + this.hashCode());
        /*no-op*/
    }

    @Override
    public void onProgressUpdate(int percent) {
        Log.d(TAG, "onProgressUpdate " + this.hashCode());
        /*no-op*/
    }

    @Override
    public void onCancelled() {
        Log.d(TAG, "onCancelled " + this.hashCode());
        /*no-op*/
    }

    @Override
    public void onPostExecute(List<Sign> result) {
        Log.d(TAG, "onPostExecute " + this.hashCode());
        // FIXME: After savedInstance has been called, this.recyclerview is null here, despite being
        // FIXME: set in the onCreated() method. Therefore a findViewById is necessary.
        final RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.signSearchRecyclerView);
        Validate.notNull(mRecyclerView, "RecyclerView is null.");
        mRecyclerView.swapAdapter(new SignSearchAdapter(result, this), false);
    }
}
