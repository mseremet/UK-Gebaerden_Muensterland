package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationLandscape;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by mtonhaeuser on 26.03.2016.
 */
@RunWith(AndroidJUnit4.class)
public class SignTrainerPassiveTest extends AbstractSignTrainerTest {

    @Before
    public void navigateToSignTrainerPassive() {
        navigateToSignTrainerPassiveInternal();
    }

    @Test
    public void checkNavigationDrawerButtonIsPresent() {
        checkNavigationDrawerButtonIsPresentInternal();
    }

    @Test
    public void checkNavigationDrawerIsClosed() {
        checkNavigationDrawerIsClosedInternal();
    }

    @Test
    public void checkToolbarIsPresent() {
        checkToolbarIsPresentInternal();
    }

    @Test
    public void checkQuestionTextIsPresent() {
        checkQuestionTextIsPresentInternal();
    }

    @Test
    public void checkAnswerButtonsAreNotPresent() {
        checkAnswerButtonsAreNotPresentInternal();
    }

    @Test
    public void checkSignTrainerIsWorkingCorrectly() {
        checkVideoIsLoadingInternal();
        checkSolveButtonIsDisplayedInternal(matches(isDisplayed()));
        // trigger configuration change and check state afterwards
        onView(isRoot()).perform(orientationLandscape());
        checkVideoIsLoadingInternal();
        // click solve button
        onView(withText(getStringResource(R.string.solveQuestion))).check(matches(isDisplayed())).perform(click());
        checkStateAfterSolveButtonClicked(getStringResource(R.string.signQuestion));
        onView(withId(R.id.signTrainerVideoView)).check(matches((not(isDisplayed()))));
        // trigger configuration change and check state afterwards
        onView(isRoot()).perform(orientationPortrait());
        checkStateAfterSolveButtonClicked(getStringResource(R.string.signQuestion));
        onView(withId(R.id.signTrainerVideoView)).check(matches((not(isDisplayed()))));
        // click on answer button
        onView(withText(getStringResource(R.string.questionWasFair))).perform(click());
        checkVideoIsLoadingInternal();
        checkStateAfterAnswerButtonClicked();
        onView(withId(R.id.signTrainerVideoView)).check(matches((isDisplayed())));
    }

}
