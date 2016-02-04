package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.provider.BaseColumns;

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
class DbContract {

    public static final String DATABASE_NAME = "signs.db";
    public static final int DATABASE_VERSION = 1;
    public static final String EQUAL_SIGN = " = ";
    public static final String QUESTION_MARK = "?";
    private static final String ASCENDING = " ASC";

    /**
     * Empty default constructor
     */
    @SuppressWarnings("unused")
    public DbContract() {
    }

    public static abstract class SignTable implements BaseColumns {

        public static final String TABLE_NAME = "signs";
        public static final String COLUMN_NAME_SIGN_NAME = "name";
        public static final String COLUMN_NAME_SIGN_NAME_DE = "name_de";
        public static final String COLUMN_NAME_MNEMONIC = "mnemonic";
        public static final String COLUMN_NAME_LEARNING_PROGRESS = "learning_progress";
        public static final String COLUMN_NAME_STARRED = "starred";
        public static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME_SIGN_NAME, COLUMN_NAME_SIGN_NAME_DE,
                COLUMN_NAME_MNEMONIC, COLUMN_NAME_LEARNING_PROGRESS, COLUMN_NAME_STARRED};
        public static final String ORDER_BY_NAME_DE_ASC = COLUMN_NAME_SIGN_NAME_DE + ASCENDING;
    }

}
