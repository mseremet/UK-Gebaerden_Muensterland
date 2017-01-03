package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.AbstractSignBrowserTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAMA;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.TestConstants.MAMA_MNEMONIC;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
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
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
@RunWith(AndroidJUnit4.class)
public class SignVideoTest {


    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(isRoot()).perform(orientationPortrait());
        navigateToSignVideoUIFragment();
    }

    @Test
    public void checkToolbarTitle() {
        onView(allOf(withText(getStringResource(R.string.sign_viewer)), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkVideoIsLoadingAndPlaying() {
        videoIsLoadingAndPlaying();
    }

    @Test
    public void checkVideoResumesPlayingAfterOrientationChange() {
        onView(isRoot()).perform(orientationLandscape());
        videoIsLoadingAndPlaying();
    }

    @Test
    public void checkUpButtonNavigatesToSignBrowser() {
        checkUpButtonNavigatesToSignBrowserInternal();
        // do it twice because this did not work properly
        navigateToSignVideoUIFragment();
        checkUpButtonNavigatesToSignBrowserInternal();
        // check hamburger menu is present
        onView(withContentDescription(R.string.navigation_drawer_open)).check(matches(isDisplayed()));
        // do it again to check whether it works after an orientation change
        navigateToSignVideoUIFragment();
        onView(isRoot()).perform(orientationLandscape());
        checkUpButtonNavigatesToSignBrowserInternal();
    }

    @Test
    public void checkBackButtonNavigatesToSignBrowser() {
        pressBack();
        onView(ViewMatchers.withText(getStringResource(R.string.sign_browser))).check(matches(isDisplayed()));
    }

    @Test
    public void checkManualBackButtonNavigatesToSignBrowser() {
        onView(ViewMatchers.withText(getStringResource(R.string.back_to_sign_browser))).check(matches(isDisplayed())).perform(click());
        onView(ViewMatchers.withText(getStringResource(R.string.sign_browser))).check(matches(isDisplayed()));
    }

    /**
     * see this <a href="https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/14">Github issue</a>.
     */
    @Test
    public void checkOrientationChangeDoesNotCauseIllegalStateException() {
        onView(isRoot()).perform(orientationLandscape());
        checkUpButtonNavigatesToSignBrowserInternal();
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        onView(withText(getStringResource(R.string.browse_signs))).perform(click());
        navigateToSignVideoUIFragment();
        checkVideoIsLoadingAndPlaying();
    }

    @Test
    public void checkControlInfoTextIsPresent() {
        onView(withText(getStringResource(R.string.clickVideoToShowControls))).check(matches(isDisplayed()));
    }

    /**
     * This test is disabled by default because it is meant to check the error message which is
     * displayed when a video cannot be loaded. This is difficult to mock. It can be tested by
     * deleting the 'afterwards.mp4' (Dann/Danach) video in the res/raw folder and running the test.
     * See this <a href="https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/65">Github issue</a>.
     */
    @Test
    public void checkErrorMessageIsDisplayedWhenVideoCannotBeLoaded() {
        // setup - navigate back to sign browser as @before method navigates to sign viewer
        checkUpButtonNavigatesToSignBrowserInternal();
        // do the test
        final String missingSignName = "Dann/Danach";
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(AbstractSignBrowserTest.getHolderForSignWithName(missingSignName)));
        onView(allOf(withText(missingSignName))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(getStringResource(R.string.videoError)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signVideoView))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.clickVideoToShowControls))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.signVideoExceptionMessage), withText(getStringResource(R.string.ASVF_1)))).check(matches(isDisplayed()));
        onView(withId(R.id.signVideoLoadingProgressCircle)).check(matches(not((isDisplayed()))));
        onView(allOf(withId(R.id.backToSignBrowserButton), withText(getStringResource(R.string.back_to_sign_browser)))).check(matches(isDisplayed()));
    }


    private void navigateToSignVideoUIFragment() {
        onView(withId(R.id.signRecyclerView)).perform(scrollToHolder(AbstractSignBrowserTest.getHolderForSignWithName(MAMA)));
        onView(allOf(withText(MAMA))).check(matches(isDisplayed())).perform(click());
    }

    private void checkUpButtonNavigatesToSignBrowserInternal() {
        onView(withContentDescription(getStringResource(R.string.navigate_up))).perform(click());
        onView(ViewMatchers.withText(R.string.sign_browser)).check(matches(isDisplayed()));
    }


    private void videoIsLoadingAndPlaying() {
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signVideoView), withContentDescription(anyOf(containsString(getStringResource(R.string.videoIsLoading)),
                containsString(getStringResource(R.string.videoIsPlaying)))))).check(matches(isDisplayed()));
        // while the video is loading, the mnemonic is not shown completely to the user.
        onView(allOf(withId(R.id.signVideoMnemonic), withText(MAMA_MNEMONIC))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
