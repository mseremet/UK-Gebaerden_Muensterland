package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

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
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void openNavigationDrawer() {
        Log.d(MainActivityTest.class.getSimpleName(), "Open Navigation Drawer");
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

    @After
    public void checkNavigationDrawerIsClosed() {
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
        Log.d(MainActivityTest.class.getSimpleName(), "Reset orientation");
        onView(isRoot()).perform(orientationPortrait());
    }

    @Test
    public void testAllMenuItemsArePresent() {
        onView(withText(R.string.browse_signs)).check(matches(allOf(isDisplayed(), isEnabled())));
        onView(withText(R.string.train_signs)).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText(R.string.about_signs)).check((matches(allOf(isDisplayed(), isEnabled()))));
        // #54 Disabled because the settings view is not used right now.
        // onView(withText(R.string.settings)).check((matches(allOf(isDisplayed(), isEnabled()))));
        pressBack(); // close navigation drawer
    }

    @Test
    public void clickBrowseSignsButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.browse_signs), R.string.sign_browser);
    }

    @Test
    public void clickTrainSignsButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs), R.string.sign_trainer_passive);
    }

    @Test
    public void clickAboutSignsButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.about_signs), R.string.about_signs);
    }

    // #54 Disabled because the settings view is not used right now.
    //    @Test
    //    public void clickSettingsButton() {
    //        clickNavigationButtonAndCheckToolbarTitle((R.string.settings), R.string.settings);
    //    }

    @Test
    public void testBackNavigation() {
        // TODO Check if working with additional orientation changes.
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs), R.string.sign_trainer_passive);
        onView(isRoot()).perform(orientationPortrait());
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.about_signs), R.string.about_signs);
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.about_signs), R.string.about_signs);
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs), R.string.sign_trainer_passive);
        pressBack();
        pressBack();
        checkToolbarTitle(R.string.sign_browser);


    }

    private void clickNavigationButtonAndCheckToolbarTitle(final int navigationButtonTextId, final int toolbarTitleId) {
        final String navigationButtonText = getStringResource(navigationButtonTextId);
        Log.d(MainActivityTest.class.getSimpleName(), "beforeClick");
        onView(withText(navigationButtonText)).perform(click());
        Log.d(MainActivityTest.class.getSimpleName(), "afterClick");
        checkToolbarTitle(toolbarTitleId);
        Log.d(MainActivityTest.class.getSimpleName(), "beforeOrientationLandscape");
        onView(isRoot()).perform(orientationLandscape());
        Log.d(MainActivityTest.class.getSimpleName(), "afterOrientationLandscape");
        checkToolbarTitle(toolbarTitleId);
    }

    private void checkToolbarTitle(int toolbarTitleId) {
        final String toolbarTitle = getStringResource(toolbarTitleId);
        onView(allOf(withText(toolbarTitle), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
