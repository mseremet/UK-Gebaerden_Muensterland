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

    /**
     * Persist a sign.
     *
     * @param sign a Sign, which has not been persisted yet.
     * @return the persisted sign, <code>null</code> if persisting failed.
     */
    public Sign create(Sign sign) {
        Log.d(CLASS_NAME, "Creating sign: " + sign);
        this.database.beginTransaction();
        Sign createdSign = null;
        try {
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
            if (-1 == insertId) {
                throw new IllegalStateException(MessageFormat.format("Inserting sign: {0} failed due to" +
                        " a database error!", sign));
            }
            createdSign = readSingleSign(insertId);
            this.database.setTransactionSuccessful();
            Log.d(CLASS_NAME, "Created sign: " + createdSign);
        } finally {
            this.database.endTransaction();
        }
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

    public Sign update(Sign sign) {
        Log.d(CLASS_NAME, "Updating sign: " + sign);
        this.database.beginTransaction();
        Sign updatedSign = null;
        try {
            final ContentValues values = new ContentValues();
            values.put(DbContract.SignTable.COLUMN_NAME_LEARNING_PROGRESS, sign.getLearningProgress());
            values.put(DbContract.SignTable.COLUMN_NAME_STARRED, sign.isStarred());
            final String selection = DbContract.SignTable._ID + " LIKE ?";
            final String[] selectionArgs = {String.valueOf(sign.getId())};
            int rowsUpdated = this.database.update(
                    DbContract.SignTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if (0 == rowsUpdated) {
                throw new IllegalStateException(MessageFormat.format("Updating sign {0} updated no rows!", sign));
            }if (1 > rowsUpdated) {
                throw new IllegalStateException(MessageFormat.format("Updating sign {0} updated more than " +
                        "one row. {1} rows were updated.", sign, rowsUpdated));
            }
            updatedSign = readSingleSign(sign.getId());
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
        return updatedSign;
    }

    /**
     * For <strong>testing</strong> purposes only!
     */
    public void delete() {
        Log.d(CLASS_NAME, "Deleting all signs");
        this.database.beginTransaction();
        try {
            this.database.delete(DbContract.SignTable.TABLE_NAME, null, null);
            this.database.setTransactionSuccessful();
        } finally {
            this.database.endTransaction();
        }
    }

    private Sign readSingleSign(long id) {
        final Sign createdSign;
        final Cursor cursor = this.database.query(DbContract.SignTable.TABLE_NAME,
                DbContract.SignTable.ALL_COLUMNS, DbContract.SignTable._ID + DbContract.EQUAL_SIGN + id, null,
                null, null, null);
        if (0 == cursor.getCount()) {
            throw new IllegalStateException(MessageFormat.format("Querying for sign with id: {1} " +
                    "yielded no results!", id));
        }
        cursor.moveToFirst();
        createdSign = cursorToSign(cursor);
        cursor.close();
        return createdSign;
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
