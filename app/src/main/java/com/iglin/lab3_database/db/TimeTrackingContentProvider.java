package com.iglin.lab3_database.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.iglin.lab3_database.db.TimeTrackingDbContract.*;
import com.iglin.lab3_database.model.RecordPicture;
import com.iglin.lab3_database.model.TimeCategory;
import com.iglin.lab3_database.model.TimeRecord;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 18.02.2017.
 */

public class TimeTrackingContentProvider {
    private TimeTrackingDbHelper mDbHelper;

    public TimeTrackingContentProvider(Context context) {
        mDbHelper = new TimeTrackingDbHelper(context);
    }

    private TimeTrackingContentProvider() {
        throw new AssertionError("Use parametrized constructor instead!");
    }

    public Cursor getCategories() {
        String[] projection = {
                Category._ID,
                Category.COLUMN_NAME_NAME
        };

        String sortOrder = Category.COLUMN_NAME_NAME + " ASC";

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        return db.query(
                Category.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
    }

    public TimeCategory getCategory(int categoryId) {
        String[] projection = {
                Category._ID,
                Category.COLUMN_NAME_NAME
        };

        String selection = Category._ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                Category.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        cursor.moveToNext();
        int index = cursor.getColumnIndex(Category.COLUMN_NAME_NAME);
        return TimeCategory.valueOf(cursor.getString(index));
    }

    public Cursor getCategory(String category) {
        String[] projection = {
                Category._ID,
                Category.COLUMN_NAME_NAME
        };
        String selection = Category.COLUMN_NAME_NAME + " LIKE ?";
        String[] selectionArgs = { category };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        return db.query(
                Category.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
    }

/*    public Cursor getPictures(int recordId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        return db.rawQuery("SELECT " + Picture._ID + ", " + Picture.COLUMN_NAME_RECORD + ", " + Picture.COLUMN_NAME_PICTURE
                        + " FROM " + Picture.TABLE_NAME
                + " WHERE " + Picture._ID + " IN (SELECT " + RecordPisctures.COLUMN_NAME_PICTURE
                + " FROM " + RecordPisctures.TABLE_NAME
                + " WHERE " + RecordPisctures.COLUMN_NAME_RECORD + " = ?)",
                new String[] {String.valueOf(recordId)});
    }*/

    public Cursor getPictures(int recordId) {
        String[] projection = {
                Picture._ID,
                Picture.COLUMN_NAME_RECORD,
                Picture.COLUMN_NAME_PICTURE
        };
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = Record._ID + " = ?";
        String[] selectionArgs = { String.valueOf(recordId) };

        return db.query(
                Picture.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
    }

    public TimeRecord insertRecord(TimeRecord record) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = getCategory(record.getTimeCategory().name());
        cursor.moveToNext();
        int index = cursor.getColumnIndex(Category._ID);
        int categoryId = cursor.getInt(index);

        ContentValues values = new ContentValues();
        values.put(Record.COLUMN_NAME_CATEGORY, categoryId);
        values.put(Record.COLUMN_NAME_START, record.getStartTime());
        values.put(Record.COLUMN_NAME_END, record.getEndTime());
        values.put(Record.COLUMN_NAME_MINUTES, record.getDuration());
        int id;
        if (record.getDescription() != null) {
            values.put(Record.COLUMN_NAME_DESCRIPTION, record.getDescription());
            id = (int) db.insert(
                    Record.TABLE_NAME,
                    null,
                    values);
        } else {
            id = (int) db.insert(
                    Record.TABLE_NAME,
                    Record.COLUMN_NAME_DESCRIPTION,
                    values);
        }
        record.setId(id);

        if (record.getPics() != null && !record.getPics().isEmpty()) {
            for (RecordPicture picture : record.getPics()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Picture.COLUMN_NAME_PICTURE, bitmapToBytesArray(picture.getPicture()));
                contentValues.put(Picture.COLUMN_NAME_RECORD, id);
                int newRowId;
                newRowId = (int) db.insert(
                        Picture.TABLE_NAME,
                        null,
                        contentValues);
                picture.setId(newRowId);
            }
        }
        db.close();
        return record;
    }

    @Nullable
    public TimeRecord getRecord(int id) {
        String selection = Record._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String[] projection = {
                Record._ID,
                Record.COLUMN_NAME_DESCRIPTION,
                Record.COLUMN_NAME_CATEGORY,
                Record.COLUMN_NAME_START,
                Record.COLUMN_NAME_END,
                Record.COLUMN_NAME_MINUTES
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                Record.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        TimeRecord timeRecord = null;
        int index;
        if (cursor.moveToNext()) {
            timeRecord = new TimeRecord();
            index = cursor.getColumnIndex(Record._ID);
            timeRecord.setId(cursor.getInt(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_DESCRIPTION);
            timeRecord.setDescription(cursor.getString(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_MINUTES);
            timeRecord.setDuration(cursor.getInt(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_START);
            timeRecord.setStartTime(cursor.getLong(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_END);
            timeRecord.setEndTime(cursor.getLong(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_CATEGORY);
            timeRecord.setTimeCategory(getCategory(cursor.getInt(index)));

            Cursor picCursor = getPictures(timeRecord.getId());
            if (picCursor.getCount() > 0) {
                List<RecordPicture> pics = new ArrayList<>(picCursor.getCount());
                while (picCursor.moveToNext()) {
                    index = picCursor.getColumnIndex(Picture._ID);
                    int picId = picCursor.getInt(index);
                    index = picCursor.getColumnIndex(Picture.COLUMN_NAME_PICTURE);
                    Bitmap bitmap = bytesArrayToBitmap(picCursor.getBlob(index));
                    pics.add(new RecordPicture(picId, bitmap));
                }
                timeRecord.setPics(pics);
            }
            picCursor.close();
        }
        cursor.close();
        db.close();
        return timeRecord;
    }

    public List<TimeRecord> getRecords() {

        String[] projection = {
                Record._ID,
                Record.COLUMN_NAME_DESCRIPTION,
                Record.COLUMN_NAME_CATEGORY,
                Record.COLUMN_NAME_START,
                Record.COLUMN_NAME_END,
                Record.COLUMN_NAME_MINUTES
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                Record.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<TimeRecord> list = new ArrayList<>(cursor.getCount());
        int index;
        while (cursor.moveToNext()) {
            TimeRecord timeRecord = new TimeRecord();
            index = cursor.getColumnIndex(Record._ID);
            timeRecord.setId(cursor.getInt(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_DESCRIPTION);
            timeRecord.setDescription(cursor.getString(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_MINUTES);
            timeRecord.setDuration(cursor.getInt(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_START);
            timeRecord.setStartTime(cursor.getLong(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_END);
            timeRecord.setEndTime(cursor.getLong(index));
            index = cursor.getColumnIndex(Record.COLUMN_NAME_CATEGORY);
            timeRecord.setTimeCategory(getCategory(cursor.getInt(index)));

            Cursor picCursor = getPictures(timeRecord.getId());
            if (picCursor.getCount() > 0) {
                List<RecordPicture> pics = new ArrayList<>(picCursor.getCount());
                while (picCursor.moveToNext()) {
                    index = picCursor.getColumnIndex(Picture._ID);
                    int picId = picCursor.getInt(index);
                    index = picCursor.getColumnIndex(Picture.COLUMN_NAME_PICTURE);
                    Bitmap bitmap = bytesArrayToBitmap(picCursor.getBlob(index));
                    pics.add(new RecordPicture(picId, bitmap));
                }
                timeRecord.setPics(pics);
            }
            picCursor.close();
            list.add(timeRecord);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void deleteRecord(int id) {
        TimeRecord timeRecord = getRecord(id);
        String selection = Record._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(Record.TABLE_NAME, selection, selectionArgs);

        selection = Picture.COLUMN_NAME_RECORD + " = ?";
        selectionArgs = new String[] { String.valueOf(id) };
        db.delete(Picture.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void deleteRecord(TimeRecord timeRecord) {
        String selection = Record._ID + " = ?";
        String[] selectionArgs = { String.valueOf(timeRecord.getId()) };
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(Record.TABLE_NAME, selection, selectionArgs);

        selection = Picture.COLUMN_NAME_RECORD + " = ?";
        selectionArgs = new String[] { String.valueOf(timeRecord.getId()) };
        db.delete(Picture.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    private byte[] bitmapToBytesArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap bytesArrayToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
