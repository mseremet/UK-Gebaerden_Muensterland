package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.MainActivity;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

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
public class SignVideoTest {

    public static final String MAMA_NAME = "mama";
    private static final String MAMA = "Mama";
    private static final String MAMA_MNEMONIC = "Wange streicheln";

    static {
        Looper.prepare();
    }

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(isRoot()).perform(orientationPortrait());
        onView(allOf(withText(MAMA), withParent(withId(R.id.signBrowserSingleRow)))).check(matches(isDisplayed())).perform(click());
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
    public void checkBackToBrowseButtonIsPresent() {
        // TODO
    }

    // See https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/14
    @Test
    public void checkOrientationChangeDoesNotCauseIllegalStateException() {
        onView(isRoot()).perform(orientationLandscape());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        onView(withText(getStringResource(R.string.browse_signs))).perform(click());
        onView(allOf(withText(MAMA), withParent(withId(R.id.signBrowserSingleRow)))).check(matches(isDisplayed())).perform(click());
        checkVideoIsLoadingAndPlaying();
    }


    private void videoIsLoadingAndPlaying() {
        onView(withId(R.id.signVideoLoadingProgressBar)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.signVideoView), withContentDescription((containsString(getStringResource(R.string.videoIsLoading))))))
                .check(matches(isDisplayed()));
        // FIXME: Doesn't work, code in Runnable is not executed.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onView(withId(R.id.signVideoLoadingProgressBar)).check(matches((not(isDisplayed()))));
                onView(allOf(withId(R.id.signVideoView),
                        withContentDescription(allOf(containsString(getStringResource(R.string.videoIsPlaying)), containsString(MAMA_NAME)))))
                        .check(matches(isDisplayed()));
                onView(allOf(withId(R.id.signVideoMnemonic), withText(MAMA_MNEMONIC))).check(matches(isDisplayed()));
            }
        }, 3000);
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
