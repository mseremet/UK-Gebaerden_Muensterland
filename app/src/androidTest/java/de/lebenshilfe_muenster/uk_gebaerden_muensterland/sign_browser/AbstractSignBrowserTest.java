package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.apache.commons.lang3.Validate;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity;

import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

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
public abstract class AbstractSignBrowserTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public static Matcher<RecyclerView.ViewHolder> getHolderForSignWithName(final String signNameLocaleDe) {
        Validate.notEmpty(signNameLocaleDe);
        return new BaseMatcher<RecyclerView.ViewHolder>() {

            @Override
            public boolean matches(Object item) {
                Validate.isInstanceOf(SignBrowserAdapter.ViewHolder.class, item);
                final SignBrowserAdapter.ViewHolder holder = (SignBrowserAdapter.ViewHolder) item;
                boolean matches = false;
                if (!(null == holder.txtSignName)) {
                    matches = ((signNameLocaleDe.equals(holder.txtSignName.getText().toString()))
                            && (View.VISIBLE == holder.txtSignName.getVisibility()));
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with sign name: " + signNameLocaleDe);
            }
        };
    }

    @NonNull
    protected String getStringResource(int stringResourceId) {
        return mainActivityActivityTestRule.getActivity().getResources().getString(stringResourceId);
    }

    @NonNull
    protected Matcher<View> getSignWithName(String name) {
        return allOf(withParent(withId(R.id.signBrowserSingleRow)), hasSibling(withText(name)),
                withContentDescription(containsString(getStringResource(R.string.starredButton))));
    }
}
