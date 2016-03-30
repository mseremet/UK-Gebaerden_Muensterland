package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anyOf;

/**
 * Created by mtonhaeuser on 26.03.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SignTrainerTest {

    public static final String TAG = SignTrainerTest.class.getSimpleName();

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void navigateToSignTrainer() {
        onView(isRoot()).perform(orientationPortrait());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        final String navigationButtonText = mainActivityActivityTestRule.getActivity().getResources().getString(R.string.train_signs);
        final String toolbarTitle = mainActivityActivityTestRule.getActivity().getResources().getString(R.string.sign_trainer);
        onView(withText(navigationButtonText)).perform(click());
        onView(allOf(withText(toolbarTitle), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
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
    public void checkQuestionTextIsPresent() {
        onView(withId((R.id.signTrainerQuestionText))).check(matches(isDisplayed()));
    }

    @Test
    public void checkVideoViewTextIsPresent() {
        onView((withContentDescription(anyOf(containsString(getStringResource(R.string.videoIsLoading)),
                Matchers.containsString(getStringResource(R.string.videoIsPlaying)))))).check(matches(isDisplayed()));
        onView(withText(getStringResource(R.string.solveQuestion))).check(matches(isDisplayed()));
        onView(isRoot()).perform(orientationLandscape()); // trigger configuration change
        onView((withContentDescription(anyOf(containsString(getStringResource(R.string.videoIsLoading)),
                Matchers.containsString(getStringResource(R.string.videoIsPlaying)))))).check(matches(isDisplayed()));
        onView(withText(getStringResource(R.string.solveQuestion))).check(matches(isDisplayed()));
    }


    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
