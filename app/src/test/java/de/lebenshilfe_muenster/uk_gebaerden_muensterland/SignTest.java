package de.lebenshilfe_muenster.uk_gebaerden_muensterland;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
public class SignTest {

    private static final String FOOTBALL = "football";
    private static final String KICK_A_BALL = "Kick a ball";
    private static final String FUSSBALL = "Fußball";

    @Test
    public void testGetId() {
        final Sign sign = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertThat(sign.getId(), is(equalTo(0)));
    }

    @Test(expected = NullPointerException.class)
    public void testNameCannotBeNull() {
        new Sign.Builder().setId(0).setName(null).setNameLocaleDe(FUSSBALL).setMnemonic(KICK_A_BALL)
                .setStarred(false).setLearningProgress(0).create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameCannotBeEmpty() {
        new Sign.Builder().setId(0).setName(StringUtils.EMPTY).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
    }

    @Test(expected = NullPointerException.class)
    public void testMnemonicCannotBeNull() {
        new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL).setMnemonic(null)
                .setStarred(false).setLearningProgress(0).create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMnemonicCannotBeEmpty() {
        new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(StringUtils.EMPTY).setStarred(false).setLearningProgress(0).create();
    }

    @Test
    public void testGetName() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertName(football);
    }

    @Test
    public void testGetNameLocaleDe() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertNameLocaleDe(football);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameLocaleDeCannotBeEmpty() {
        new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(StringUtils.EMPTY)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
    }

    @Test
    public void testGetMnemonic() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertMnemonic(football);
    }

    @Test
    public void testIsStarred() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(0).create();
        assertThat(football.isStarred(), is(equalTo(true)));
    }

    @Test
    public void testSetStarred() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(0).create();
        football.setStarred(false);
        assertThat(football.isStarred(), is(equalTo(false)));
    }

    @Test
    public void testGetLearningProgress() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(5).create();
        assertThat(football.getLearningProgress(), is(equalTo(5)));
    }

    @Test
    public void testIncreaseLearningProgress() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(2).create();
        football.increaseLearningProgress();
        assertThat(football.getLearningProgress(), is(equalTo(3)));
    }

    @Test
    public void testIncreaseLearningProgressDoesNotViolateBoundary() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(5).create();
        football.increaseLearningProgress();
        assertThat(football.getLearningProgress(), is(equalTo(5)));
    }

    @Test
    public void testDecreaseLearningProgress() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(2).create();
        football.decreaseLearningProgress();
        assertThat(football.getLearningProgress(), is(equalTo(1)));
    }

    @Test
    public void testDecreaseLearningProgressDoesNotViolateBoundary() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(-5).create();
        football.decreaseLearningProgress();
        assertThat(football.getLearningProgress(), is(equalTo(-5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLearningProgressUpperBoundary() {
        new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(6).create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLearningProgressLowerBoundary() {
        new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(true).setLearningProgress(-6).create();
    }

    @Test
    public void testObjectsAreEqual() {
        final Sign footballOne = new Sign.Builder().setId(0).setName(FOOTBALL)
                .setNameLocaleDe(FUSSBALL).setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        final Sign footballTwo = new Sign.Builder().setId(0).setName(FOOTBALL)
                .setNameLocaleDe(FUSSBALL).setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertThat(footballOne, is(equalTo(footballTwo)));
    }

    @Test
    public void testToStringContainsFields() {
        final Sign football = new Sign.Builder().setId(0).setName(FOOTBALL).setNameLocaleDe(FUSSBALL)
                .setMnemonic(KICK_A_BALL).setStarred(false).setLearningProgress(0).create();
        assertThat(football.toString(), allOf(containsString("id"), containsString(FOOTBALL), containsString(FUSSBALL),
                containsString(KICK_A_BALL), containsString("0"), containsString("false")));
    }

    @Test
    public void testBuilderWorksWithoutIdStarredAndLearningProgressParams() {
        Sign football = new Sign.Builder().setName(FOOTBALL).setNameLocaleDe(FUSSBALL).setMnemonic(KICK_A_BALL).create();
        assertName(football);
        assertNameLocaleDe(football);
        assertMnemonic(football);
    }

    @Test (expected = NullPointerException.class)
    public void testBuilderDoesNotWorkWithoutNameLocalizedNameAndMnemonic() {
        new Sign.Builder().setName(FOOTBALL).create();
    }

    private void assertName(Sign football) {
        assertThat(football.getName(), is(equalTo(FOOTBALL)));
    }

    private void assertNameLocaleDe(Sign football) {
        assertThat(football.getNameLocaleDe(), is(equalTo(FUSSBALL)));
    }

    private void assertMnemonic(Sign football) {
        assertThat(football.getMnemonic(), is(equalTo(KICK_A_BALL)));
    }
}
