package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.about_signs.AboutSignsFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.AbstractSignTrainerFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.SignTrainerActiveFragment;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.SignTrainerPassiveFragment;

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
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SignBrowserFragment.OnSignBrowserSignClickedListener, AbstractSignTrainerFragment.OnToggleLearningModeListener {

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

    @SuppressWarnings("deprecation")
    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Validate.notNull(drawerLayout);
        this.toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /**
             * Necessary because of API 15 Drawer Layout bug.
             * See https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/28
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.setDrawerListener(this.toggle);
    }

    private void setupNavigationView() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Validate.notNull(navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void restoreInstanceStateOrShowDefault(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            showSignBrowser();
        } else {
            final String toolbarTitle = savedInstanceState.getString(KEY_TOOLBAR_TITLE);
            Validate.notNull(toolbarTitle, "Toolbar title is empty in saved instance state bundle.");
            setActionBarTitle(toolbarTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate " + hashCode());
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
        Validate.notNull(drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (1 < getSupportFragmentManager().getBackStackEntryCount()) {
            popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemsSelected " + hashCode());
        int id = item.getItemId();
        if (R.id.nav_sign_browser == id) {
            showSignBrowser();
        } else if (R.id.nav_sign_trainer_passive == id) {
            showSignTrainer(LearningMode.PASSIVE);
        } else if (R.id.nav_sign_trainer_active == id) {
            showSignTrainer(LearningMode.ACTIVE);
        } else if (R.id.nav_sign_info == id) {
            showAboutSigns();
        }
        // #54 Settings view currently deactivated.
        //        else if (R.id.nav_sign_settings == id) {
        //            showSettings();
        //        }
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Validate.notNull(drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + hashCode());
        this.toggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignBrowserSignSelected(Sign sign) {
        Log.d(TAG, "onSignBrowserSignSelected: " + sign.getName() + StringUtils.SPACE + hashCode());
        showSignVideo(sign);
    }

    @Override
    public void toggleLearningMode(LearningMode learningMode) {
        Log.d(TAG, "toggleLearningMode() learningMode: " + learningMode + StringUtils.SPACE + hashCode());
        showSignTrainer(learningMode);
    }

    private void setFragment(Fragment fragment, String actionBarTitle) {
        Log.d(TAG, "setFragment: " + actionBarTitle + StringUtils.SPACE + hashCode());
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, actionBarTitle);
        transaction.addToBackStack(actionBarTitle);
        transaction.commit();
    }

    private void popBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        final FragmentManager.BackStackEntry previousFragment = fragmentManager.getBackStackEntryAt(backStackEntryCount - 2);
        final String previousFragmentActionBarTitle = previousFragment.getName();
        setActionBarTitle(previousFragmentActionBarTitle);
        fragmentManager.popBackStack();
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
        setFragment(signBrowserFragment, getString(R.string.sign_browser));
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

    // #54 Disabled because the settings view is not used right now.
    //    private void showSettings() {
    //        Log.d(TAG, "showSettings() " + hashCode());
    //        setFragment(new SettingsFragment(), SETTINGS_TAG);
    //        setActionBarTitle(getString(R.string.settings));
    //    }

    private void showAboutSigns() {
        Log.d(TAG, "showAboutSigns() " + hashCode());
        setFragment(new AboutSignsFragment(), getString(R.string.about_signs));
        setActionBarTitle(getString(R.string.about_signs));
    }

    private void showSignTrainer(LearningMode learningMode) {
        Log.d(TAG, "showSignTrainer() learningMode: " + learningMode + StringUtils.SPACE + hashCode());
        if (LearningMode.ACTIVE == learningMode) {
            setFragment(new SignTrainerActiveFragment(), getString(R.string.sign_trainer_active));
            setActionBarTitle(getString(R.string.sign_trainer_active));
        } else if (LearningMode.PASSIVE == learningMode) {
            setFragment(new SignTrainerPassiveFragment(), getString(R.string.sign_trainer_passive));
            setActionBarTitle(getString(R.string.sign_trainer_passive));
        } else {
            throw new NotImplementedException(String.format("LearningMode %s not yet implemented.", learningMode));
        }
    }


}
