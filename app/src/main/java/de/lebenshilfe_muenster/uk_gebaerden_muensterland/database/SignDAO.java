package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.Sign;

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
public class SignDAO {

    public static final String CLASS_NAME = SignDAO.class.getName();
    private final DbHelper dbHelper;
    private SQLiteDatabase database;

    public SignDAO(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        Log.d(CLASS_NAME, "Opening database.");
        this.database = dbHelper.getWritableDatabase();
    }

    public void close() {
        Log.d(CLASS_NAME, "Closing database.");
        this.dbHelper.close();
    }

    public Sign create(Sign sign) {
        Log.d(CLASS_NAME, "Creating sign: " + sign);
        final ContentValues values = new ContentValues();
        values.put(DbContract.SignTable.COLUMN_NAME_SIGN_NAME, sign.getName());
        values.put(DbContract.SignTable.COLUMN_NAME_SIGN_NAME_DE, sign.getNameLocaleDe());
        values.put(DbContract.SignTable.COLUMN_NAME_MNEMONIC, sign.getMnemonic());
        if (sign.isStarred()) {
            values.put(DbContract.SignTable.COLUMN_NAME_STARRED, 1);
        } else {
            values.put(DbContract.SignTable.COLUMN_NAME_STARRED, 0);
        }
        values.put(DbContract.SignTable.COLUMN_NAME_LEARNING_PROGRESS, sign.getLearningProgress());
        final long insertId = this.database.insert(DbContract.SignTable.TABLE_NAME, null,
                values);
        final Cursor cursor = this.database.query(DbContract.SignTable.TABLE_NAME,
                DbContract.SignTable.ALL_COLUMNS, DbContract.SignTable._ID + DbContract.EQUAL_SIGN + insertId, null,
                null, null, null);
        if (0 == cursor.getCount()) {
            throw new IllegalStateException(MessageFormat.format("Inserted sign: {0} with id: {1}, " +
                    "but querying the table with this id yielded  no results!",sign, insertId));
        }
        cursor.moveToFirst();
        final Sign createdSign = cursorToSign(cursor);
        cursor.close();
        Log.d(CLASS_NAME, "Created sign: " + createdSign);
        return createdSign;
    }

    public List<Sign> read() {
        Log.d(CLASS_NAME, "Reading all signs.");
        final List<Sign> signs = new ArrayList<>();
        final Cursor cursor = database.query(DbContract.SignTable.TABLE_NAME,
                DbContract.SignTable.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final Sign sign = cursorToSign(cursor);
            signs.add(sign);
            cursor.moveToNext();
        }
        cursor.close();
        return signs;
    }

    private Sign cursorToSign(Cursor cursor) {
        final Sign.Builder signBuilder = new Sign.Builder();
        signBuilder.setId(cursor.getInt(cursor.getColumnIndex(DbContract.SignTable._ID)));
        signBuilder.setName(cursor.getString(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_SIGN_NAME)));
        signBuilder.setNameLocaleDe(cursor.getString(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_SIGN_NAME_DE)));
        signBuilder.setMnemonic(cursor.getString(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_MNEMONIC)));
        signBuilder.setLearningProgress(cursor.getInt(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_LEARNING_PROGRESS)));
        final long starred = cursor.getLong(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_STARRED));
        if (1 == starred) {
            signBuilder.setStarred(true);
        } else {
            signBuilder.setStarred(false);
        }
        return signBuilder.create();
    }
}
