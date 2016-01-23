package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> menuActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // TODO: Check that the sign browser is displayed on app startup

    @Test(timeout = 5000)
    public void testAllMenuItemsArePresent() {
        openNavigationDrawer();
        onView(withText(R.string.browse_signs)).check(matches(allOf(isDisplayed(), isEnabled())));
        onView(withText(R.string.train_signs)).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText(R.string.about_signs)).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText(R.string.settings)).check((matches(allOf(isDisplayed(), isEnabled()))));
        closeNavigationDrawer();
    }

    @Test(timeout = 3000)
    public void clickBrowseSignsButton() {
        openNavigationDrawer();
        onView(withText(R.string.browse_signs)).perform(click());
        onView(withText(R.string.sign_browser)).check(matches(isDisplayed()));
    }

    // TODO: Add tests for clicking the other navigation buttons here.

    private void openNavigationDrawer() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

    private void closeNavigationDrawer() {
        pressBack();
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
    }

}
