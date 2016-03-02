package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.apache.commons.lang3.StringUtils;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.about_signs.AboutSignsFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.settings.SettingsFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserUIFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoUIFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.SignTrainerFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SignBrowserUIFragment.OnSignClickedListener {

    private static final String SIGN_BROWSER_TAG = "sign_browser_tag";
    private static final String SIGN_VIDEO_TAG = "sign_video_tag";
    private static final String SIGN_TRAINER_TAG = "sign_trainer_tag";
    private static final String ABOUT_SIGNS_TAG = "about_signs_tag";
    private static final String SETTINGS_TAG = "settings_tag";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_TOOLBAR_TITLE = "main_activity_toolbar_title";
    private String toolbarTitle = StringUtils.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (null == savedInstanceState) {
            showSignBrowser();
        } else {
            setActionBarTitle(savedInstanceState.getString(KEY_TOOLBAR_TITLE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance");
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TOOLBAR_TITLE, this.toolbarTitle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemsSelected");
        int id = item.getItemId();
        if (R.id.nav_sign_browser == id) {
            showSignBrowser();
        } else if (R.id.nav_sign_trainer == id) {
            showSignTrainer();
        } else if (R.id.nav_sign_info == id) {
            showAboutSigns();
        } else if (R.id.nav_sign_settings == id) {
            showSettings();
        }
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSignSelected(Sign sign) {
        Log.d(TAG, "onSignSelected: " + sign.getName());
        showSignVideo(sign);
    }

    // TODO: https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/7
    private void setFragment(Fragment fragment, String fragmentTag) {
        Log.d(TAG, "setFragment: " + fragmentTag);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setActionBarTitle(String actionBarTitle) {
        Log.d(TAG, "setActionBarTitle: " + actionBarTitle);
        final ActionBar supportActionBar = getSupportActionBar();
        if (null == supportActionBar) {
            throw new IllegalStateException("SupportActionBar is null. Should be set in onCreate() method.");
        } else {
            this.toolbarTitle = actionBarTitle;
            supportActionBar.setTitle(this.toolbarTitle);
        }
    }

    private void showSignBrowser() {
        Log.d(TAG, "showSignBrowser()");
        final SignBrowserUIFragment signBrowserUIFragment = new SignBrowserUIFragment();
        setFragment(signBrowserUIFragment, SIGN_BROWSER_TAG);
        setActionBarTitle(getString(R.string.sign_browser));
    }

    private void showSignVideo(Sign sign) {
        final SignVideoUIFragment signVideoUIFragment = new SignVideoUIFragment();
        final Bundle args = new Bundle();
        args.putParcelable(SignVideoUIFragment.SIGN_TO_SHOW, sign);
        signVideoUIFragment.setArguments(args);
        setFragment(signVideoUIFragment, SIGN_VIDEO_TAG);
        setActionBarTitle(StringUtils.EMPTY);
    }

    private void showSettings() {
        setFragment(new SettingsFragment(), SETTINGS_TAG);
        setActionBarTitle(getString(R.string.settings));
    }

    private void showAboutSigns() {
        setFragment(new AboutSignsFragment(), ABOUT_SIGNS_TAG);
        setActionBarTitle(getString(R.string.about_signs));
    }

    private void showSignTrainer() {
        setFragment(new SignTrainerFragment(), SIGN_TRAINER_TAG);
        setActionBarTitle(getString(R.string.sign_trainer));
    }
}
