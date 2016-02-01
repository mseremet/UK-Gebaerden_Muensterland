package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
public class SignDatabaseHelperTest {

    private static final String FOOTBALL = "football";
    private static final String KICK_A_BALL = "Kick a ball";
    private static final String FUSSBALL = "Fußball";


    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    final SignDAO signDAO = new SignDAO(mainActivityActivityTestRule.launchActivity(null));

    @Before
    public void openDatabase() {
        signDAO.open();
    }

    @After
    public void closeDatabase() {
        signDAO.close();
    }

    @Test
    public void createSign() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        final Sign createdFootball = signDAO.create(football);
        assertThat(createdFootball, is(equalTo(football)));
    }


}
