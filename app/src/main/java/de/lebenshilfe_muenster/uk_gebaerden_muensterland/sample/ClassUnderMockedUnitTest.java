package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sample;

import android.content.Context;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;

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
public class ClassUnderMockedUnitTest {

    final Context context;

    public ClassUnderMockedUnitTest(Context context) {
        this.context = context;
    }

    public String getAppName() {
        return this.context.getString(R.string.app_name);
    }
}

