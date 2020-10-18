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

    static final String DATABASE_NAME = "signs.db";
    static final int DATABASE_VERSION = 2;
    static final String EQUAL_SIGN = " = ";
    static final String QUESTION_MARK = "?";
    static final String LIKE = " LIKE ?";
    private static final String ASCENDING = " ASC";
    static final String BOOLEAN_TRUE = "1";
    static final String OR = " OR ";

    static abstract class SignTable implements BaseColumns {

        static final String TABLE_NAME = "signs";
        static final String COLUMN_NAME_SIGN_NAME = "name";
        static final String COLUMN_NAME_SIGN_NAME_DE = "name_de";
        static final String COLUMN_NAME_MNEMONIC = "mnemonic";
        static final String COLUMN_NAME_TAG1 = "tag1";
        static final String COLUMN_NAME_TAG2 = "tag2";
        static final String COLUMN_NAME_TAG3 = "tag3";
        static final String COLUMN_NAME_LEARNING_PROGRESS = "learning_progress";
        static final String COLUMN_NAME_STARRED = "starred";
        static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME_SIGN_NAME, COLUMN_NAME_SIGN_NAME_DE,
                COLUMN_NAME_MNEMONIC, COLUMN_NAME_TAG1, COLUMN_NAME_TAG2, COLUMN_NAME_TAG3, COLUMN_NAME_LEARNING_PROGRESS, COLUMN_NAME_STARRED};
        static final String NAME_LOCALE_DE_LIKE = "LOWER(" + COLUMN_NAME_SIGN_NAME_DE + ") LIKE LOWER(?)";
        static final String TAG1_LOCALE_DE_LIKE = "LOWER(" + COLUMN_NAME_TAG1 + ") LIKE LOWER(?)";
        static final String TAG2_LOCALE_DE_LIKE = "LOWER(" + COLUMN_NAME_TAG2 + ") LIKE LOWER(?)";
        static final String TAG3_LOCALE_DE_LIKE = "LOWER(" + COLUMN_NAME_TAG3 + ") LIKE LOWER(?)";
        static final String IS_STARRED = COLUMN_NAME_STARRED + EQUAL_SIGN + QUESTION_MARK;
        static final String ORDER_BY_NAME_DE_ASC = COLUMN_NAME_SIGN_NAME_DE + ASCENDING;
        static final String ORDER_BY_LEARNING_PROGRESS_ASC = COLUMN_NAME_LEARNING_PROGRESS + ASCENDING;
    }

}
