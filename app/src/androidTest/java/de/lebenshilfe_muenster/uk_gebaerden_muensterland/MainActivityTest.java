package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
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
@SuppressWarnings({"WeakerAccess", "unused"})
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

//    @After // deactivate this when looking for bugs as it hides exceptions in test methods.
//    public void checkNavigationDrawerIsClosed() {
//        Log.d(MainActivityTest.class.getSimpleName(), "checkNavigationDrawerIsClosed");
//        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
//    }

    @Test
    public void testAllMenuItemsArePresent() {
        onView(withText(R.string.browse_signs)).check(matches(allOf(isDisplayed(), isEnabled())));
        onView(withText(R.string.train_signs_passive)).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText(R.string.train_signs_active)).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText(R.string.about_signs)).check((matches(allOf(isDisplayed(), isEnabled()))));
        // #54 Disabled because the settings view is not used right now.
        // onView(withText(R.string.settings)).check((matches(allOf(isDisplayed(), isEnabled()))));
        pressBack(); // close navigation drawer because there are no fragments in the back stack.
    }

    @Test
    public void clickBrowseSignsButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.browse_signs), R.string.sign_browser);
    }

    @Test
    public void clickTrainSignsPassiveButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_passive), R.string.sign_trainer_passive);
    }

    @Test
    public void clickTrainSignsActiveButton() {
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_active), R.string.sign_trainer_active);
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
        // one fragment in the back stack
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_passive), R.string.sign_trainer_passive);
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        checkNoneOfTheMenuItemsAreChecked();
        // one fragment in the back stack
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.about_signs), R.string.about_signs);
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        checkNoneOfTheMenuItemsAreChecked();
        // two fragments in the back stack
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.about_signs), R.string.about_signs);
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_passive), R.string.sign_trainer_passive);
        pressBack();
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        checkNoneOfTheMenuItemsAreChecked();
        // three fragments in the back stack
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_passive), R.string.sign_trainer_passive);
        onView(withId(R.id.action_toggle_learning_mode)).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText(getStringResource(R.string.sign_trainer_active)), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
        openNavigationDrawer();
        clickNavigationButtonAndCheckToolbarTitle((R.string.train_signs_passive), R.string.sign_trainer_passive);
        pressBack();
        pressBack();
        checkToolbarTitle(R.string.sign_trainer_passive);
        checkNoneOfTheMenuItemsAreChecked();
        pressBack();
        checkToolbarTitle(R.string.sign_browser);
        checkNoneOfTheMenuItemsAreChecked();
    }

    private void clickNavigationButtonAndCheckToolbarTitle(final int navigationButtonTextId, final int toolbarTitleId) {
        final String navigationButtonText = getStringResource(navigationButtonTextId);
        onView(withText(navigationButtonText)).perform(click());
        checkToolbarTitle(toolbarTitleId);
        Log.d(MainActivityTest.class.getSimpleName(), "Setting orientation to landscape.");
        onView(isRoot()).perform(orientationLandscape());
        checkToolbarTitle(toolbarTitleId);
        Log.d(MainActivityTest.class.getSimpleName(), "Setting orientation to portrait.");
        onView(isRoot()).perform(orientationPortrait());
    }

    private void checkToolbarTitle(int toolbarTitleId) {
        final String toolbarTitle = getStringResource(toolbarTitleId);
        onView(allOf(withText(toolbarTitle), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
    }

    private void checkNoneOfTheMenuItemsAreChecked() {
        openNavigationDrawer();
        onView(withText(R.string.browse_signs)).check(matches(allOf(isDisplayed(), not(isChecked()))));
        onView(withText(R.string.train_signs_passive)).check(matches(allOf(isDisplayed(), not(isChecked()))));
        onView(withText(R.string.about_signs)).check(matches(allOf(isDisplayed(), not(isChecked()))));
        pressBack(); // close navigation drawer
    }


    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
