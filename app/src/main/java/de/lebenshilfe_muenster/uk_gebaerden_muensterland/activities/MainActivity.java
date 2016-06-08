package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.about_signs.AboutSignsFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.settings.SettingsFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.AbstractSignTrainerFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.SignTrainerActiveFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.SignTrainerPassiveFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SignBrowserFragment.OnSignClickedListener, AbstractSignTrainerFragment.OnToggleLearningModeListener {

    private static final String SIGN_BROWSER_TAG = "sign_browser_tag";
    private static final String SIGN_TRAINER_ACTIVE_TAG = "sign_trainer_active_tag";
    private static final String SIGN_TRAINER_PASSIVE_TAG = "sign_trainer_passive_tag";
    private static final String ABOUT_SIGNS_TAG = "about_signs_tag";
    private static final String SETTINGS_TAG = "settings_tag";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_TOOLBAR_TITLE = "main_activity_toolbar_title";
    private String actionBarTitle = StringUtils.EMPTY;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate " + hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupToolbar();
        setupNavigationView();
        restoreInstanceStateOrShowDefault(savedInstanceState);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
                    /**
                     * Necessary because of API 15 Drawer Layout bug.
                     See https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/28
                     */
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                        drawerLayout.bringChildToFront(drawerView);
                        drawerLayout.requestLayout();
                    }
                };
        this.toggle = actionBarDrawerToggle;
        drawerLayout.setDrawerListener(this.toggle);
    }

    private void setupNavigationView() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void restoreInstanceStateOrShowDefault(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            showSignBrowser();
        } else {
            setActionBarTitle(savedInstanceState.getString(KEY_TOOLBAR_TITLE));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.toggle.syncState();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TOOLBAR_TITLE, this.actionBarTitle);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed " + hashCode());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemsSelected " + hashCode());
        int id = item.getItemId();
        if (R.id.nav_sign_browser == id) {
            showSignBrowser();
        } else if (R.id.nav_sign_trainer == id) {
            showSignTrainer(LearningMode.PASSIVE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        this.toggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignSelected(Sign sign) {
        Log.d(TAG, "onSignSelected: " + sign.getName() + StringUtils.SPACE + hashCode());
        showSignVideo(sign);
    }

    @Override
    public void toggleLearningMode(LearningMode learningMode) {
        Log.d(TAG, "toggleLearningMode() learningMode: " + learningMode + StringUtils.SPACE + hashCode());
        showSignTrainer(learningMode);
    }

    // TODO: https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/7
    private void setFragment(Fragment fragment, String fragmentTag) {
        Log.d(TAG, "setFragment: " + fragmentTag + StringUtils.SPACE + hashCode());
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setActionBarTitle(String actionBarTitle) {
        Log.d(TAG, "setActionBarTitle: " + actionBarTitle + StringUtils.SPACE + hashCode());
        Validate.notNull(getSupportActionBar(), "SupportActionBar is null. Should be set in onCreate() method.");
        this.actionBarTitle = actionBarTitle;
        getSupportActionBar().setTitle(this.actionBarTitle);
    }

    private void showSignBrowser() {
        Log.d(TAG, "showSignBrowser() " + hashCode());
        final SignBrowserFragment signBrowserFragment = new SignBrowserFragment();
        setFragment(signBrowserFragment, SIGN_BROWSER_TAG);
        setActionBarTitle(getString(R.string.sign_browser));
    }

    private void showSignVideo(Sign sign) {
        Log.d(TAG, "showSignVideo() " + hashCode());
        final Intent intent = new Intent(this, LevelOneActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(LevelOneActivity.FRAGMENT_TO_SHOW, SignVideoFragment.class.getSimpleName());
        bundle.putParcelable(SignVideoFragment.SIGN_TO_SHOW, sign);
        intent.putExtra(LevelOneActivity.EXTRA, bundle);
        startActivity(intent);
    }

    private void showSettings() {
        Log.d(TAG, "showSettings() " + hashCode());
        setFragment(new SettingsFragment(), SETTINGS_TAG);
        setActionBarTitle(getString(R.string.settings));
    }

    private void showAboutSigns() {
        Log.d(TAG, "showAboutSigns() " + hashCode());
        setFragment(new AboutSignsFragment(), ABOUT_SIGNS_TAG);
        setActionBarTitle(getString(R.string.about_signs));
    }

    private void showSignTrainer(LearningMode learningMode) {
        Log.d(TAG, "showSignTrainer() learningMode: " + learningMode + StringUtils.SPACE + hashCode());
        if (LearningMode.ACTIVE == learningMode) {
            setFragment(new SignTrainerActiveFragment(), SIGN_TRAINER_ACTIVE_TAG);
            setActionBarTitle(getString(R.string.sign_trainer_active));
        } else if (LearningMode.PASSIVE == learningMode) {
            setFragment(new SignTrainerPassiveFragment(), SIGN_TRAINER_PASSIVE_TAG);
            setActionBarTitle(getString(R.string.sign_trainer_passive));
        } else {
            throw new NotImplementedException(String.format("LearningMode %s not yet implemented.", learningMode));
        }
    }


}
