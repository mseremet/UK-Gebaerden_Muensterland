package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.FOOTBALL;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAMA;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.PAPA;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.PROGRESS_0;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * Copyright ( ) 2016 Matthias Tonhäuser
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
public class SignBrowserTest extends AbstractSignBrowserTest {

    @Before
    public void changeOrientationToPortrait() {
        onView(isRoot()).perform(orientationPortrait());
    }

    @Test
    public void checkSignBrowserIsDisplayedOnAppStartup() {
        onView(ViewMatchers.withText(R.string.sign_browser)).check(matches(isDisplayed()));
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
    public void checkToggleStarredButtonsIsPresent() {
        onView(withId(R.id.action_toggle_starred)).check(matches(isDisplayed()));
    }

    @Test
    public void checkTogglingStarredSignsWorks() {
        try {
            // check toggle on works
            onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
            onView(getSignWithName(MAMA)).check(matches(isNotChecked())).perform(click());
            onView(withId(R.id.action_toggle_starred)).check(matches(isDisplayed())).perform(click());
            checkOnlyStarredSignsAreShown();
            // check toggle state is persisted
            onView(isRoot()).perform(orientationLandscape()); // trigger configuration change
            checkOnlyStarredSignsAreShown();
            // check toggle off works
            onView(withId(R.id.action_toggle_starred)).check(matches(isDisplayed())).perform(click());
            checkSignRecyclerViewHasListElements();
        } finally {
            // reset
            onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
            onView(getSignWithName(MAMA)).check(matches(isChecked())).perform(click());
        }
    }

    @Test
    public void checkSignRecyclerViewIsDisplayed() {
        onView(withId(R.id.signRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignRecyclerViewHasListElements() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(MAMA))))).check(matches(isDisplayed()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(PAPA)));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(PAPA))))).check(matches(isDisplayed()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(FOOTBALL)));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(FOOTBALL))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignHasStarredInformation() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
        onView(getSignWithName(MAMA)).check(matches(isNotChecked()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(PAPA)));
        onView(getSignWithName(PAPA)).check(matches(isNotChecked()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(FOOTBALL)));
        onView(getSignWithName(FOOTBALL)).check(matches(isNotChecked()));
    }

    @Test
    public void checkSignStarredInformationCanBePersisted() {
        try {
            onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
            onView(getSignWithName(MAMA)).check(matches(isNotChecked())).perform(click());
            onView(getSignWithName(MAMA)).check(matches(isChecked()));
            onView(isRoot()).perform(orientationLandscape()); // trigger configuration change
        } finally {
            // reset
            onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
            onView(getSignWithName(MAMA)).check(matches(isChecked())).perform(click());
        }
    }

    @Test
    public void checkSignHasLearningProgressInformation() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(MAMA)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(PAPA)));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(PAPA)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(FOOTBALL)));
        onView(allOf(withId(R.id.signBrowserSingleRow), hasDescendant(withText(FOOTBALL)), hasDescendant(withText(containsString(PROGRESS_0))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkClickingOnSignNameNavigatesToVideoView() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
        onView(allOf(withText(MAMA))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
    }

    @Test
    public void checkClickingOnVideoIconNavigatesToVideoView() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(getHolderForSignWithName(MAMA)));
        onView(allOf(withContentDescription(R.string.signIconDescription), hasSibling(withText(MAMA)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
    }

    private void checkOnlyStarredSignsAreShown() {
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(MAMA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(PAPA))))).check(doesNotExist());
        onView(allOf(withId(R.id.signRecyclerView), hasDescendant((withText(FOOTBALL))))).check(doesNotExist());
    }

}
