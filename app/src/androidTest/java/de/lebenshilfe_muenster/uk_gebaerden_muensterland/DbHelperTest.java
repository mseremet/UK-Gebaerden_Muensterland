package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

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
public class DbHelperTest {

    public static final Sign FOOTBALL = new Sign.Builder().setId(0).setName("football").setNameLocaleDe("Fußball")
            .setMnemonic("Kick a ball").setStarred(false).setLearningProgress(0).create();
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    final SignDAO signDAO = new SignDAO(mainActivityActivityTestRule.launchActivity(null));
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void openDatabase() {
        signDAO.open();
        signDAO.delete();
    }

    @After
    public void closeDatabase() {
        signDAO.close();
    }

    @Test
    public void testCreatedSignEqualsInsertedSign() {
        final Sign createdFootball = signDAO.create(FOOTBALL);
        assertThat(createdFootball, is(equalTo(FOOTBALL)));
    }

    @Test
    public void testDuplicateSignViolatesTableConstraint() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(allOf(startsWith("Inserting sign"), endsWith("a database error!")));
        signDAO.create(FOOTBALL);
        signDAO.create(FOOTBALL);
    }

    @Test
    public void testDeleteEmptiesTheTable() {
        signDAO.create(FOOTBALL);
        signDAO.delete();
        int numberOfSignsLeft = signDAO.read().size();
        if (0 > numberOfSignsLeft) {
            fail("Deleted all signs, but reading returns a list with size: " + numberOfSignsLeft);
        }
    }

    @Test
    public void testUpdateChangesLearningProgress() {
        final Sign createdSign = signDAO.create(FOOTBALL);
        createdSign.increaseLearningProgress();
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.getLearningProgress(), is(equalTo(createdSign.getLearningProgress())));
    }

    @Test
    public void testUpdateChangesStarred() {
        final Sign createdSign = signDAO.create(FOOTBALL);
        createdSign.setStarred(true);
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.isStarred(), is(equalTo(true)));
    }

    @Test
    public void testUpdateThrowsExceptionOnIllegalId() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(allOf(startsWith("Updating sign"), endsWith("no rows!")));
        final Sign illegalSign = new Sign.Builder().setId(Integer.MAX_VALUE).setName("football").setNameLocaleDe("Fußball")
                .setMnemonic("Kick a ball").setStarred(false).setLearningProgress(0).create();
         signDAO.update(illegalSign);
    }

}
