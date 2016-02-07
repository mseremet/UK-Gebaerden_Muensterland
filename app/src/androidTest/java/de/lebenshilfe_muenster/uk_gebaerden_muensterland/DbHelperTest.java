package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.apache.commons.lang3.time.StopWatch;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
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

    private static final Sign FOOTBALL = new Sign.Builder().setId(0).setName("football").setNameLocaleDe("Fußball")
            .setMnemonic("Faust tritt in Handfläche").setStarred(false).setLearningProgress(0).create();
    private static final Sign MAMA = new Sign.Builder().setId(0).setName("mama").setNameLocaleDe("Mama")
            .setMnemonic("Wange streicheln").setStarred(false).setLearningProgress(0).create();
    private static final Sign PAPA = new Sign.Builder().setId(0).setName("papa").setNameLocaleDe("Papa")
            .setMnemonic("Schnurrbart").setStarred(false).setLearningProgress(0).create();
    private static final Sign TEST_SIGN = new Sign.Builder().setId(0).setName("test_sign").setNameLocaleDe("test_sign_de")
            .setMnemonic("test_sign_mnemonic").setStarred(false).setLearningProgress(0).create();
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private final SignDAO signDAO = SignDAO.getInstance(mainActivityActivityTestRule.launchActivity(null));
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void openDatabase() {
        signDAO.open();
    }

    @After
    public void closeDatabase() {
        signDAO.close();
    }

    @Test
    public void testCreatedSignEqualsInsertedSign() {
        final Sign sign = signDAO.create(TEST_SIGN);
        assertThat(sign, is(equalTo(TEST_SIGN)));
        signDAO.delete(sign);
    }

    @Test
    public void testDuplicateSignViolatesTableConstraint() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(allOf(startsWith("Inserting sign"), endsWith("a database error!")));
        signDAO.create(FOOTBALL);
    }

    // @Test - disabled because the app is shipped with an existing database and this test is very slow.
    public void testCreatingManySigns() {
        final List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            final String name = "sign_" + i;
            signs.add(new Sign.Builder().setId(0).setName(name).setNameLocaleDe(name + "_de")
                    .setMnemonic(name + "_mnemonic").setStarred(false).setLearningProgress(0).create());

        }
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        signDAO.create(signs);
        stopWatch.stop();
        Log.d(DbHelperTest.class.getSimpleName(), "Creating the signs took " + stopWatch.getTime() + " milliseconds");
        stopWatch.reset();
        stopWatch.start();
        final List<Sign> signsFromDb = signDAO.read();
        stopWatch.stop();
        Log.d(DbHelperTest.class.getSimpleName(), "Reading the signs took " + stopWatch.getTime() + " milliseconds");
        signsFromDb.remove(FOOTBALL);
        signsFromDb.remove(MAMA);
        signsFromDb.remove(PAPA);
        assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[signs.size()])));
        signDAO.delete(signs);
    }

    @Test
    public void testReadReturnsList() {
        List<Sign> signs = new ArrayList<>();
        signs.add(FOOTBALL);
        signs.add(PAPA);
        signs.add(MAMA);
        List<Sign> signsFromDb = signDAO.read();
        assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[signs.size()])));
    }

    @Test
    public void testReadWithNameLocaleDeReturnsSingleResult() {
        List<Sign> signs = new ArrayList<>();
        signs.add(MAMA);
        List<Sign> signsFromDb = signDAO.read("mam");
        assertThat(signsFromDb, containsInAnyOrder(signs.toArray(new Sign[signs.size()])));
    }

    @Test
    public void testReadWithIllegalNameLocaleDeReturnsNoResult() {
        List<Sign> signsFromDb = signDAO.read("foobar");
        assertThat(signsFromDb, is(empty()));
    }


    @Test
    public void testDelete() {
        final Sign sign = signDAO.create(TEST_SIGN);
        final int numberOfSignsBefore = signDAO.read().size();
        signDAO.delete(sign);
        final int numberOfSignsAfter = signDAO.read().size();
        if (numberOfSignsAfter != (numberOfSignsBefore -1)) {
            fail("Deleted sign, but reading returns a list with the same size");
        }
    }

    @Test
    public void testUpdateChangesLearningProgress() {
        final Sign createdSign = signDAO.create(TEST_SIGN);
        createdSign.increaseLearningProgress();
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.getLearningProgress(), is(equalTo(createdSign.getLearningProgress())));
        signDAO.delete(createdSign);
    }

    @Test
    public void testUpdateChangesStarred() {
        final Sign createdSign = signDAO.create(TEST_SIGN);
        createdSign.setStarred(true);
        final Sign updatedSign = signDAO.update(createdSign);
        assertThat(updatedSign.getId(), is(equalTo(createdSign.getId())));
        assertThat(updatedSign.isStarred(), is(equalTo(true)));
        signDAO.delete(createdSign);
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
