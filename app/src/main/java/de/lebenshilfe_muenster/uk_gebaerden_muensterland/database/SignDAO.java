package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignDAO {

    private static final String CLASS_NAME = SignDAO.class.getSimpleName();
    private static SignDAO instance;
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    /**
     * Private constructor
     */
    private SignDAO(Context context) {
        this.openHelper = new DbHelper(context);
    }

    /**
     * Singleton instance of the SignDAO
     */
    public static synchronized SignDAO getInstance(Context context) {
        if (null == instance) {
            instance = new SignDAO(context);
        }
        return instance;
    }

    public void open() throws SQLException {
        Log.d(CLASS_NAME, "Opening database.");
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        Log.d(CLASS_NAME, "Closing database.");
        if (null != this.database) {
            this.openHelper.close();
        }
    }

    /**
     * Persist a list of signs. For <strong>testing</strong> purposes only.
     *
     * @param signs a list of signs, which hast not been persisted yet.
     * @return a list of persisted signs.
     */
    @SuppressWarnings("UnusedReturnValue")
    public List<Sign> create(List<Sign> signs) {
        final List<Sign> createdSigns = new ArrayList<>();
        for (Sign sign : signs) {
            createdSigns.add(create(sign));
        }
        return createdSigns;
    }

    /**
     * Persist a sign. For <strong>testing</strong> purposes only.
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

    /**
     * Read all signs.
     *
     * @return a list of signs, may be empty but not null.
     */
    public List<Sign> read() {
        return readInternal(StringUtils.EMPTY, false);
    }

    /**
     * Read the signs where the name_locale_de matches the parameter.
     *
     * @param whereSignNameLocaleDeLike
     * @return a list of signs, may be empty but not null.
     */
    public List<Sign> read(String whereSignNameLocaleDeLike) {
        return readInternal(whereSignNameLocaleDeLike, false);
    }

    /**
     * Read the signs which have been starred by the user.
     *
     * @return
     */
    public List<Sign> readStarredSignsOnly() {
        return readInternal(StringUtils.EMPTY, true);
    }


    @NonNull
    private List<Sign> readInternal(String whereSignNameLocaleDeLike, boolean readStarredSignsOnly) {
        final List<Sign> signs = new ArrayList<>();
        Cursor cursor;
        if (StringUtils.isNotEmpty(whereSignNameLocaleDeLike)) {
            Log.d(CLASS_NAME, MessageFormat.format("Reading signs with name_locale_de like: {0}", whereSignNameLocaleDeLike));
            cursor = database.query(DbContract.SignTable.TABLE_NAME,
                    DbContract.SignTable.ALL_COLUMNS, DbContract.SignTable.NAME_LOCALE_DE_LIKE,
                    new String[]{"%" + whereSignNameLocaleDeLike + "%"}, null, null, DbContract.SignTable.ORDER_BY_NAME_DE_ASC);
        } else if (readStarredSignsOnly) {
            Log.d(CLASS_NAME, "Reading starred signs only");
            cursor = database.query(DbContract.SignTable.TABLE_NAME,
                    DbContract.SignTable.ALL_COLUMNS, DbContract.SignTable.IS_STARRED,
                    new String[]{DbContract.BOOLEAN_TRUE}, null, null, DbContract.SignTable.ORDER_BY_NAME_DE_ASC);
        } else {
            Log.d(CLASS_NAME, "Reading all signs");
            cursor = database.query(DbContract.SignTable.TABLE_NAME,
                    DbContract.SignTable.ALL_COLUMNS, null, null, null, null, DbContract.SignTable.ORDER_BY_NAME_DE_ASC);
        }
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
            final String selection = DbContract.SignTable._ID + DbContract.LIKE;
            final String[] selectionArgs = {String.valueOf(sign.getId())};
            int rowsUpdated = this.database.update(
                    DbContract.SignTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            if (0 == rowsUpdated) {
                throw new IllegalStateException(MessageFormat.format("Updating sign {0} updated no rows!", sign));
            }
            if (1 > rowsUpdated) {
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
    public void delete(List<Sign> signs) {
        for (Sign sign : signs) {
            delete(sign);
        }
    }

    /**
     * For <strong>testing</strong> purposes only!
     */
    public void delete(Sign sign) {
        Log.d(CLASS_NAME, MessageFormat.format("Deleting sign {0}", sign));
        this.database.beginTransaction();
        try {
            this.database.delete(DbContract.SignTable.TABLE_NAME,
                    DbContract.SignTable.COLUMN_NAME_SIGN_NAME + DbContract.EQUAL_SIGN + DbContract.QUESTION_MARK,
                    new String[]{sign.getName()});
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
        final int starred = cursor.getInt(cursor.getColumnIndex(DbContract.SignTable.COLUMN_NAME_STARRED));
        if (1 == starred) {
            signBuilder.setStarred(true);
        } else {
            signBuilder.setStarred(false);
        }
        return signBuilder.create();
    }


}
