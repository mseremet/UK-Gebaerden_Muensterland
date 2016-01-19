package de.lebenshilfe_muenster.uk_gebaerden_muensterland;


import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Copyright (c) 2016 MatthiasTon
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

    @Test(expected = NullPointerException.class)
    public void testNameCannotBeNull() {
        new Sign(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNameCannotBeEmpty() {
        new Sign(StringUtils.EMPTY);
    }

    @Test
    public void testObjectsAreEqual() {
        final Sign footballOne = new Sign(SignTestFixture.FOOTBALL);
        final Sign footballTwo = new Sign(SignTestFixture.FOOTBALL);
        assertThat(footballOne, is(equalTo(footballTwo)));
    }

    @Test
    public void testToStringContainsName() {
        final Sign football = new Sign(SignTestFixture.FOOTBALL);
        assertThat(football.toString(),containsString(SignTestFixture.FOOTBALL));
    }

    @Test
    public void testGetName() {
        final Sign football = new Sign(SignTestFixture.FOOTBALL);
        assertThat(SignTestFixture.FOOTBALL, is(equalTo(football.getName())));
    }
}
