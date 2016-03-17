package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

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
public class SignParcelableTest {

    private static final int ID = 0;
    private static final String NAME = "football";
    private static final String NAME_LOCALE_DE = "Fußball";
    private static final String MNEMONIC = "Faust tritt in Handfläche";
    private static final boolean STARRED = false;
    private static final int PROGRESS = 3;
    private static final Sign FOOTBALL = new Sign.Builder().setId(ID).setName(NAME).setNameLocaleDe(NAME_LOCALE_DE)
            .setMnemonic(MNEMONIC).setStarred(STARRED).setLearningProgress(PROGRESS).create();

    @Test
    public void testParcelableIsImplementedCorrect() {
        final Parcel parcel = Parcel.obtain();
        FOOTBALL.writeToParcel(parcel, FOOTBALL.describeContents());
        parcel.setDataPosition(0);
        final Sign createdFromParcel = Sign.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel,is(notNullValue()));
        assertThat(createdFromParcel.getId(), is(ID));
        assertThat(createdFromParcel.getName(), is(NAME));
        assertThat(createdFromParcel.getNameLocaleDe(), is(NAME_LOCALE_DE));
        assertThat(createdFromParcel.getMnemonic(), is(MNEMONIC));
        assertThat(createdFromParcel.isStarred(), is(STARRED));
        assertThat(createdFromParcel.getLearningProgress(), is(PROGRESS));
    }

}
