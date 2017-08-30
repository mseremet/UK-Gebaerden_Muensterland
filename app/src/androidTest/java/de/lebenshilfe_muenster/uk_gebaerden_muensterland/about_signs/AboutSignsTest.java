package de.lebenshilfe_muenster.uk_gebaerden_muensterland.about_signs;

import android.support.annotation.NonNull;
import android.support.test.espresso.web.webdriver.Locator;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static android.support.test.espresso.web.webdriver.DriverAtoms.findElement;
import static android.support.test.espresso.web.webdriver.DriverAtoms.getText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.matchers.JUnitMatchers.containsString;

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
@SuppressWarnings("unused")
@RunWith(AndroidJUnit4.class)
public class AboutSignsTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void navigateToAboutSigns() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
        final String navigationButtonText = getStringResource(R.string.about_signs);
        final String toolbarTitle = getStringResource(R.string.about_signs);
        onView(withText(navigationButtonText)).perform(click());
        onView(allOf(withText(toolbarTitle), withParent((withId(R.id.toolbar))))).check(matches(isDisplayed()));
    }

    @Test
    public void checkWebViewIsPresent() {
        onView(withId(R.id.aboutSignsWebView)).check(matches(isDisplayed()));
    }

    @Test
    public void checkWebViewHtmlIsProperlyRendered() {
        onWebView().withElement(findElement(Locator.ID, "firstHeader")).check(webMatches(getText(), containsString("Was sind die UK-Gebärden Münsterland?")));
        onWebView().withElement(findElement(Locator.ID, "firstParagraph")).check(webMatches(getText(), containsString("UK steht für \"Unterstützte Kommunikation\"")));
        onWebView().withElement(findElement(Locator.ID, "lastHeader")).check(webMatches(getText(), containsString("Welche Open-Source-Bibliotheken wurden benutzt?")));
    }

    @NonNull
    private String getStringResource(int id) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(id);
    }

}
