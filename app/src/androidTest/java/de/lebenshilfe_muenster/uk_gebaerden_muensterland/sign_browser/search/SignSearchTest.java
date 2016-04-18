package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.ENTER;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.FOOTBALL;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAM;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAMA;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.PAP;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.PAPA;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static org.hamcrest.Matchers.allOf;

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
public class SignSearchTest {

    private static final String TAG = SignSearchTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkToolbarTitleIsEmpty() {
        performSearch(MAM);
        onView(allOf(withText(MAMA), withParent(withId(R.id.signSearchSingleRow)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText("SignSearchVideoActivity"), withParent((withId(R.id.toolbar))))).check(doesNotExist());
    }

    @Test
    public void checkSearchingForSignsWorks() {
        Log.d(TAG, "Search from sign browser");
        performSearch(MAM);
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(MAMA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(PAPA))))).check(doesNotExist());
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(FOOTBALL))))).check(doesNotExist());
        checkActivityTitle(MAM);
        Log.d(TAG, "trigger configuration change");
        onView(isRoot()).perform(orientationLandscape());
        checkActivityTitle(MAM);
        Log.d(TAG, "Search from the list of results again");
        performSearch(PAP);
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(MAMA))))).check(doesNotExist());
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(PAPA))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signSearchRecyclerView), hasDescendant((withText(FOOTBALL))))).check(doesNotExist());
        checkActivityTitle(PAP);
        Log.d(TAG, "trigger configuration change");
        onView(isRoot()).perform(orientationLandscape());
        checkActivityTitle(PAP);
        navigateUp();
        onView(withText(R.string.sign_browser)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNavigatingToVideosAndReturningWorks() {
        performSearch(MAM);
        onView(allOf(withText(MAMA), withParent(withId(R.id.signSearchSingleRow)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
        navigateUp();
        checkActivityTitle(MAM);
        performSearch(PAP);
        onView(allOf(withText(PAPA), withParent(withId(R.id.signSearchSingleRow)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(PAPA))).check(matches(isDisplayed()));
        onView(isRoot()).perform(orientationLandscape());
        navigateUp();
        checkActivityTitle(PAP);
    }

    private void performSearch(String query) {
        Log.d(TAG, "Perform search");
        onView(withId(R.id.action_search)).check(matches(isDisplayed())).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(isDisplayed())).perform(typeText(query + ENTER));
    }

    private void checkActivityTitle(String query) {
        onView(allOf(withText(getStringResource(R.string.search_results) + StringUtils.SPACE + query),
                withParent((withId(android.support.design.R.id.action_bar))))).check(matches(isDisplayed()));
    }

    /**
     * Navigate back to the sign browser -- Up button is only accessible via a localized content description ('Nach oben')
     */
    private void navigateUp() {
        Log.d(TAG, "navigateUp()" );
        onView(withContentDescription(getStringResource(R.string.navigate_up))).perform(click());
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }


}
