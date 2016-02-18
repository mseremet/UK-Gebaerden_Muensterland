package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.util.OrientationChangeAction.orientationPortrait;
import static org.hamcrest.CoreMatchers.allOf;

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

    private static final String ENTER = "\n";
    private static final String MAM = "mam";
    private static final String PAP = "paP";
    private static final String MAMA = "Mama";
    private static final String PAPA = "Papa";
    private static final String FOOTBALL = "Fußball";
    private static final String MAMA_MNEMONIC = "Wange streicheln";
    private static final String PAPA_MNEMONIC = "Schnurrbart";
    private static final String FOOTBALL_MNEMONIC = "Faust tritt in Handfläche";
    private static final String STARRED = "Starred";
    private static final String PROGRESS_0 = "0";

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void changeOrientationToPortrait() {
        onView(isRoot()).perform(orientationPortrait());
    }

    @Test
    public void checkClickingOnSignNameNavigatesToDetailsView() {
        onView(allOf(withText(MAMA), withParent(withId(R.id.signBrowserSingleRow)))).check(matches(isDisplayed())).perform(click());
        onView(allOf(withId(R.id.signVideoName), withText(MAMA))).check(matches(isDisplayed()));
    }

    @NonNull
    private String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

}
