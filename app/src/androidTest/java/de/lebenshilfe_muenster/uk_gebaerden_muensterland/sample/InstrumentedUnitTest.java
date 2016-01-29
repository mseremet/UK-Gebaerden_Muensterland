/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sample;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sample.ClassUnderInstrumentedUnitTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests that the parcelable interface is implemented correctly.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class InstrumentedUnitTest {

    private static final String TEST_STRING = "This is a string";
    private static final long TEST_LONG = 12345678L;
    private ClassUnderInstrumentedUnitTest mClassUnderInstrumentedUnitTest;

    @Before
    public void createLogHistory() {
        mClassUnderInstrumentedUnitTest = new ClassUnderInstrumentedUnitTest();
    }

    @Test
    public void logHistory_ParcelableWriteRead() {
        // Set up the Parcelable object to send and receive.
        mClassUnderInstrumentedUnitTest.addEntry(TEST_STRING, TEST_LONG);

        // Write the data
        Parcel parcel = Parcel.obtain();
        mClassUnderInstrumentedUnitTest.writeToParcel(parcel, mClassUnderInstrumentedUnitTest.describeContents());

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0);

        // Read the data
        ClassUnderInstrumentedUnitTest createdFromParcel = ClassUnderInstrumentedUnitTest.CREATOR.createFromParcel(parcel);
        List<Pair<String, Long>> createdFromParcelData = createdFromParcel.getData();

        // Verify that the received data is correct.
        assertThat(createdFromParcelData.size(), is(1));
        assertThat(createdFromParcelData.get(0).first, is(TEST_STRING));
        assertThat(createdFromParcelData.get(0).second, is(TEST_LONG));
    }
}
