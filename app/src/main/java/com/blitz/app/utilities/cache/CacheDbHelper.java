package com.blitz.app.utilities.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper to manage creation and access of a Sqlite database for storing cached
 * key-value pairs.
 *
 * See http://developer.android.com/training/basics/data-storage/databases.html
 *
 * Created by Nate on 8/25/14.
 */
public class CacheDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Cache.db";

    private static final String SQL_TABLE_NAME = "table_backed_rest_cache";
    private static final String KEY_TYPE = "TEXT";
    private static final String VALUE_TYPE = "TEXT";
    private static final String KEY_COLUMN_NAME = "KEY";
    private static final String VALUE_COLUMN_NAME = "VALUE";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SQL_TABLE_NAME + " (" +
                    KEY_COLUMN_NAME + " " + KEY_TYPE + ", " +
                    VALUE_COLUMN_NAME + " " + VALUE_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SQL_TABLE_NAME;

    public CacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This is just cached data, so the upgrade policy is to delete the data and start over.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Similarly to onUpgrade, just start fresh on downgrade of the schema.
        onUpgrade(db, oldVersion, newVersion);
    }
}
