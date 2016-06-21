package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Rule;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anyOf;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
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
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSignTrainerTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @NonNull
    protected String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

    protected void navigateToSignTrainerPassiveInternal() {
        onView(isRoot()).perform(orientationPortrait());
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        final String navigationButtonText = mainActivityActivityTestRule.getActivity().getResources().getString(R.string.train_signs);
        final String toolbarTitle = mainActivityActivityTestRule.getActivity().getResources().getString(R.string.sign_trainer_passive);
        onView(withText(navigationButtonText)).perform(click());
        onView(allOf(withText(toolbarTitle), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
    }

    protected void checkNavigationDrawerButtonIsPresentInternal() {
        onView(withContentDescription(R.string.navigation_drawer_open)).check(matches(isDisplayed()));
    }

    protected void checkNavigationDrawerIsClosedInternal() {
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
    }

    protected void checkToolbarIsPresentInternal() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    protected void checkQuestionTextIsPresentInternal() {
        onView(withId((R.id.signTrainerQuestionText))).check(matches(isDisplayed()));
    }

    protected void checkAnswerButtonsAreNotPresentInternal() {
        onView(withText(getStringResource(R.string.questionWasEasy))).check(matches(not(isDisplayed())));
        onView(withText(getStringResource(R.string.questionWasFair))).check(matches(not(isDisplayed())));
        onView(withText(getStringResource(R.string.questionWasHard))).check(matches(not(isDisplayed())));
    }

    protected void checkSolveButtonIsDisplayedInternal(ViewAssertion matches) {
        onView(withText(getStringResource(R.string.solveQuestion))).check(matches);
    }

    protected void checkVideoIsLoadingInternal() {
        onView((withContentDescription(anyOf(containsString(getStringResource(R.string.videoIsLoading)),
                Matchers.containsString(getStringResource(R.string.videoIsPlaying)))))).check(matches(isDisplayed()));
    }

    protected void checkStateAfterSolveButtonClicked(String questionText) {
        checkSolveButtonIsDisplayedInternal(matches((Matchers.not(isDisplayed()))));
        onView(withText(questionText)).check(matches((Matchers.not(isDisplayed()))));
        onView(withText(getStringResource(R.string.questionWasEasy))).check(matches(isCompletelyDisplayed()));
        onView(withText(getStringResource(R.string.questionWasFair))).check(matches(isCompletelyDisplayed()));
        onView(withText(getStringResource(R.string.questionWasHard))).check(matches(isCompletelyDisplayed()));
        onView(withContentDescription(getStringResource(R.string.answer))).check(matches(isCompletelyDisplayed()));
        onView(withContentDescription(getStringResource(R.string.trainerMnemonic))).check(matches(isCompletelyDisplayed()));
        onView(withContentDescription(getStringResource(R.string.learningProgress))).check(matches(isCompletelyDisplayed()));
        onView(withContentDescription(getStringResource(R.string.howHardWasTheQuestion))).check(matches(isCompletelyDisplayed()));
        onView(withContentDescription(getStringResource(R.string.signTrainerExplanation))).check(matches(isCompletelyDisplayed()));
    }

    protected void checkStateAfterAnswerButtonClicked() {
        checkSolveButtonIsDisplayedInternal(matches(isEnabled()));
        checkAnswerButtonsAreNotPresentInternal();
        onView(withContentDescription(getStringResource(R.string.answer))).check(matches(Matchers.not(isDisplayed())));
        onView(withContentDescription(getStringResource(R.string.trainerMnemonic))).check(matches(Matchers.not(isDisplayed())));
        onView(withContentDescription(getStringResource(R.string.learningProgress))).check(matches(Matchers.not(isDisplayed())));
        onView(withContentDescription(getStringResource(R.string.howHardWasTheQuestion))).check(matches(Matchers.not(isDisplayed())));
        onView(withContentDescription(getStringResource(R.string.signTrainerExplanation))).check(matches(Matchers.not(isDisplayed())));
    }
}
