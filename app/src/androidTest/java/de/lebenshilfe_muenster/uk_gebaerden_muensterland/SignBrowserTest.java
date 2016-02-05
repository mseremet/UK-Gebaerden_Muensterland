package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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

    public static final String ENTER = "\n";
    public static final String MAM = "mam";
    public static final String PAP = "paP";
    private static final String MAMA = "Mama";
    private static final String PAPA = "Papa";
    private static final String FOOTBALL = "Fußball";
    private static final String MAMA_MNEMONIC = "Wange streicheln";
    private static final String PAPA_MNEMONIC = "Schnurrbart";
    private static final String FOOTBALL_MNEMONIC = "Faust tritt in Handfläche";
    private static final String STARRED = "Starred";
    private static final String PROGRESS_0 = "0";
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
    public void checkSearchIsPresent() {
        onView(withId(R.id.action_search)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSearchingForSignsWorks() {
        // Search from sign browser
        onView(withId(R.id.action_search)).check(matches(isDisplayed())).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(isDisplayed())).perform(typeText(MAM + ENTER));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(MAMA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(PAPA))))).check(doesNotExist());
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(FOOTBALL))))).check(doesNotExist());
        onView(allOf(withText(getStringResource(R.string.search_results) + StringUtils.SPACE + MAM),
                withParent((withId(android.support.design.R.id.action_bar))))).check(matches(isDisplayed()));
        // Search from the list of results again
        onView(withId(R.id.action_search)).check(matches(isDisplayed())).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(isDisplayed())).perform(typeText(PAP + ENTER));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(MAMA))))).check(doesNotExist());
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(PAPA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(FOOTBALL))))).check(doesNotExist());
        onView(allOf(withText(getStringResource(R.string.search_results) + StringUtils.SPACE + PAP),
                withParent((withId(android.support.design.R.id.action_bar))))).check(matches(isDisplayed()));
        // Navigate back to the sign browser -- Up button is only accessible via a localized content description ('Nach oben')
        onView(withContentDescription(getStringResource(R.string.navigate_up))).perform(click());
        onView(withText(R.string.sign_browser)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignRecyclerViewIsDisplayed() {
        onView(withId(R.id.signRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignRecyclerViewHasListElements() {
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(MAMA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(PAPA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(FOOTBALL))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignHasMnemonic() {
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(MAMA_MNEMONIC))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(PAPA_MNEMONIC))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(containsString(FOOTBALL_MNEMONIC))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignHasStarredInformation() {
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(MAMA)), withText(containsString(STARRED)))).check(matches(isNotChecked()));
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(PAPA)), withText(containsString(STARRED)))).check(matches(isNotChecked()));
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(FOOTBALL)), withText(containsString(STARRED)))).check(matches(isNotChecked()));
    }

    @Test
    public void checkSignStarredInformationCanBePersisted() {
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(MAMA)), withText(containsString(STARRED)))).check(matches(isNotChecked())).perform(click());
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(MAMA)), withText(containsString(STARRED)))).check(matches(isChecked()));
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.train_signs)).perform(click());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.browse_signs)).perform(click());
        onView(allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(MAMA)), withText(containsString(STARRED)))).check(matches(isChecked())).perform(click());
    }

    @Test
    public void checkSignHasLearningProgressInformation() {
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(MAMA)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(PAPA)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(FOOTBALL)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
