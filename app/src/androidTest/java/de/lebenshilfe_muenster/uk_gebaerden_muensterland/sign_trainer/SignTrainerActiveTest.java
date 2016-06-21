package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import org.junit.Before;
import org.junit.Test;

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
@SuppressWarnings("unused")
public class SignTrainerActiveTest extends  AbstractSignTrainerTest {

    @Before
    public void navigateToSignTrainerActive() {
        navigateToSignTrainerPassiveInternal();
        onView(withId(R.id.action_toggle_learning_mode)).check(matches(isDisplayed())).perform(click());
        onView(allOf(withText(getStringResource(R.string.sign_trainer_active)), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
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
        checkSignQuestionTextAndDetailIsDisplayed();
        checkSolveButtonIsDisplayedInternal(matches(isDisplayed()));
        // trigger configuration change and check state afterwards
        onView(isRoot()).perform(orientationLandscape());
        checkSignQuestionTextAndDetailIsDisplayed();
        checkSolveButtonIsDisplayedInternal(matches(isDisplayed()));
        // click solve button
        onView(withText(getStringResource(R.string.solveQuestion))).check(matches(isDisplayed())).perform(click());
        checkStateAfterSolveButtonClicked(getStringResource(R.string.howDoesThisSignLookLike));
        checkVideoIsLoadingInternal();
        // trigger configuration change and check state afterwards
        onView(isRoot()).perform(orientationPortrait());
        checkStateAfterSolveButtonClicked(getStringResource(R.string.howDoesThisSignLookLike));
        checkVideoIsLoadingInternal();
        // click on answer button
        onView(withText(getStringResource(R.string.questionWasFair))).perform(click());
        checkStateAfterAnswerButtonClicked();
    }

    private void checkSignQuestionTextAndDetailIsDisplayed() {
        onView(withText(getStringResource(R.string.howDoesThisSignLookLike))).check(matches(isDisplayed()));
        onView(withContentDescription(getStringResource(R.string.signTrainerQuestionTextDetail))).check(matches(isDisplayed()));
    }

}
