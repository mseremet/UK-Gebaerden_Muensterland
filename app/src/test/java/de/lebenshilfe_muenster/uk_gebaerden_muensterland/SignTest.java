package de.lebenshilfe_muenster.uk_gebaerden_muensterland;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

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
public class SignTest {

    public static final String FOOTBALL = "football";
    public static final String KICK_A_BALL = "Kick a ball";

    @Test(expected = NullPointerException.class)
    public void testNameCannotBeNull() {
        new Sign(null, KICK_A_BALL, false, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameCannotBeEmpty() {
        new Sign(StringUtils.EMPTY, KICK_A_BALL, false, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testMnemonicCannotBeNull() {
        new Sign(FOOTBALL, null, false, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMnemonicCannotBeEmpty() {
        new Sign(FOOTBALL, StringUtils.EMPTY, false, 0);
    }

    @Test
    public void testGetName() {
        final Sign football = new Sign(FOOTBALL, KICK_A_BALL, false, 0);
        assertThat(football.getName(), is(equalTo(FOOTBALL)));
    }

    @Test
    public void testGetMnemonic() {
        final Sign football = new Sign(FOOTBALL, KICK_A_BALL, false, 0);
        assertThat(football.getMnemonic(),is(equalTo(KICK_A_BALL)));
    }

    @Test
    public void testIsStarred() {
        final Sign football = new Sign(FOOTBALL, KICK_A_BALL, true, 0);
        assertThat(football.isStarred(), is(equalTo(true)));
    }

    @Test
    public void testGetLearningProgress() {
        final Sign football = new Sign(FOOTBALL, KICK_A_BALL, true,10);
        assertThat(football.getLearningProgress(), is(equalTo(10)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLearningProgressUpperBoundary() {
        new Sign(FOOTBALL, KICK_A_BALL, true,11);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetLearningProgressLowerBoundary() {
        new Sign(FOOTBALL, KICK_A_BALL, true,-11);
    }

    @Test
    public void testObjectsAreEqual() {
        final Sign footballOne = new Sign(FOOTBALL, KICK_A_BALL, false, 0);
        final Sign footballTwo = new Sign(FOOTBALL, KICK_A_BALL, false, 0);
        assertThat(footballOne, is(equalTo(footballTwo)));
    }

    @Test
    public void testToStringContainsFields() {
        final Sign football = new Sign(FOOTBALL, KICK_A_BALL, false, 0);
        assertThat(football.toString(),allOf(containsString(FOOTBALL),containsString(KICK_A_BALL),containsString("0"),containsString("false")));
    }

}
