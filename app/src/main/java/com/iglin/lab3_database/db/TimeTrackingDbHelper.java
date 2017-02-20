package com.iglin.lab3_database.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iglin.lab3_database.model.TimeCategory;

import static com.iglin.lab3_database.db.TimeTrackingDbContract.Category;
import static com.iglin.lab3_database.db.TimeTrackingDbContract.Picture;
import static com.iglin.lab3_database.db.TimeTrackingDbContract.Record;

/**
 * Created by user on 18.02.2017.
 */

public class TimeTrackingDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TimeTracking.db";

    private static final String TEXT_TYPE = "TEXT";
    private static final String INT_TYPE = "INTEGER";
    private static final String REAL_TYPE = "REAL";
    private static final String NUMERIC_TYPE = "NUMERIC";
    private static final String BLOB_TYPE = "BLOB";

    private static final String SQL_CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + Category.TABLE_NAME + " (" +
                    Category._ID + " INTEGER PRIMARY KEY, " +
                    Category.COLUMN_NAME_NAME + " " + TEXT_TYPE +
                    " )";
    private static final String SQL_DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS " + Category.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_PICTURE =
            "CREATE TABLE " + Picture.TABLE_NAME + " (" +
                    Picture._ID + " INTEGER PRIMARY KEY, " +
                    Picture.COLUMN_NAME_RECORD + " " + INT_TYPE + ", " +
                    Picture.COLUMN_NAME_PICTURE + " " + BLOB_TYPE + ", " +
                    " FOREIGN KEY(" + Picture.COLUMN_NAME_RECORD + ") REFERENCES "
                    + Record.TABLE_NAME + "(" + Record._ID + ") " +
                    " )";
    private static final String SQL_DROP_TABLE_PICTURE = "DROP TABLE IF EXISTS " + Picture.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_RECORD =
            "CREATE TABLE " + Record.TABLE_NAME + " (" +
                    Record._ID + " INTEGER PRIMARY KEY, " +
                    Record.COLUMN_NAME_DESCRIPTION + " " + TEXT_TYPE + ", " +
                    Record.COLUMN_NAME_CATEGORY + " " + INT_TYPE + ", " +
                    Record.COLUMN_NAME_START + " " + NUMERIC_TYPE + ", " +
                    Record.COLUMN_NAME_END + " " + NUMERIC_TYPE + ", " +
                    Record.COLUMN_NAME_MINUTES + " " + INT_TYPE + ", " +
                    " FOREIGN KEY(" + Record.COLUMN_NAME_CATEGORY + ") REFERENCES "
                        + Category.TABLE_NAME + "(" + Category._ID + ") " +
                    " )";
    private static final String SQL_DROP_TABLE_RECORD = "DROP TABLE IF EXISTS " + Record.TABLE_NAME;

    TimeTrackingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CATEGORY);
        db.execSQL(SQL_CREATE_TABLE_PICTURE);
        db.execSQL(SQL_CREATE_TABLE_RECORD);

        for (TimeCategory timeCategory : TimeCategory.values()) {
            ContentValues values = new ContentValues();
            values.put(Category.COLUMN_NAME_NAME, String.valueOf(timeCategory));
            db.insert(Category.TABLE_NAME, null, values);
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE_PICTURE);
        db.execSQL(SQL_DROP_TABLE_RECORD);
        db.execSQL(SQL_DROP_TABLE_CATEGORY);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
