package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.describedAs;
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
public class MenuActivityTest {

    @Rule
    public ActivityTestRule<MenuActivity> menuActivityTestRule = new ActivityTestRule<>(MenuActivity.class);

    @Test(timeout = 3000)
    public void testAllMenuItemsArePresent() {
        openNavigationDrawer();
        onView(withText("Browse signs")).check(matches(allOf(isDisplayed(), isEnabled())));
        onView(withText("Train signs")).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText("About signs")).check((matches(allOf(isDisplayed(), isEnabled()))));
        onView(withText("Settings")).check((matches(allOf(isDisplayed(), isEnabled()))));
        closeNavigationDrawer();
    }

    @Test(timeout = 3000)
    public void clickBrowseSignsButton() {
        openNavigationDrawer();
        onView(withText("Browse signs")).perform(click());
        onView(withText("Sign Browser")).check(matches(isDisplayed()));
    }

    private void openNavigationDrawer() {
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

    private void closeNavigationDrawer() {
        pressBack();
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
    }

}
