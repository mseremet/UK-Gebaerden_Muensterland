package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
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
public class SignBrowserTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkSignBrowserIsDisplayedOnAppStartup() {
        onView(withText(R.string.sign_browser)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNavigationDrawerButtonIsPresent() {
        onView(withContentDescription(R.string.navigation_drawer_open)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNavigationDrawerIsClosed() {
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void checkToolbarIsPresent() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignRecyclerViewIsDisplayed() {
        onView(withId(R.id.signRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignRecyclerViewHasListElements() {
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText("foo"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText("bar"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText("baz"))))).check(matches(isDisplayed()));
    }

}
