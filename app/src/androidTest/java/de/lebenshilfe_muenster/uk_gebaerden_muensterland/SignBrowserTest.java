package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
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

    public static final String FOO = "foo";
    public static final String BAR = "bar";
    public static final String BAZ = "baz";
    public static final String FOO_MNEMONIC = "foo mnemonic";
    public static final String BAR_MNEMONIC = "bar mnemonic";
    public static final String BAZ_MNEMONIC = "baz mnemonic";
    public static final String STARRED = "Starred";
    public static final String PROGRESS = "Progress";
    public static final String SIGN_ICON = "signIcon";

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
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(FOO))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(BAR))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(BAZ))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignHasMnemonic() {
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(FOO_MNEMONIC))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(BAR_MNEMONIC))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(BAZ_MNEMONIC))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignHasStarredInformation() {
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)),hasSibling(withText(FOO)), withText(containsString(STARRED)))).check(matches(isChecked()));
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)),hasSibling(withText(BAR)), withText(containsString(STARRED)))).check(matches(isNotChecked()));
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)),hasSibling(withText(BAZ)), withText(containsString(STARRED)))).check(matches(isNotChecked()));
    }

    @Test
    public void checkSignHasLearningProgressInformation() {
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(FOO)),hasDescendant(withText(containsString(PROGRESS))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(BAR)),hasDescendant(withText(containsString("5"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(BAZ)),hasDescendant(withText(containsString("-3"))))).check(matches(isDisplayed()));
    }

}
