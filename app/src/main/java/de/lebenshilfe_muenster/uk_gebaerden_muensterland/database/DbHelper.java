package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
public class DbHelper extends SQLiteOpenHelper {

    public static final String CLASS_NAME = DbHelper.class.getName();

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(CLASS_NAME,"onCreate database");
        database.execSQL(DbContract.SignTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(CLASS_NAME,"onUpgrade database");
        // FIXME: Replace with proper upgrade mechanism https://github.com/Scaronthesky/UK-Gebaerden_Muensterland/issues/8
        db.execSQL(DbContract.SignTable.DROP);
        onCreate(db);
    }
}
