package com.iglin.lab3_database.db;

import android.provider.BaseColumns;

/**
 * Created by user on 18.02.2017.
 */

public final class TimeTrackingDbContract {

    private TimeTrackingDbContract() {
        throw new AssertionError("Do not instantiate contract class!");
    }

    public static abstract class Category implements BaseColumns {
        public static final String TABLE_NAME = "category";
      //  public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static abstract class Picture implements BaseColumns {
        public static final String TABLE_NAME = "picture";
       // public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PICTURE = "picture";
    }

    public static abstract class Record implements BaseColumns {
        public static final String TABLE_NAME = "record";
      //  public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_MINUTES = "minutes";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }

    public static abstract class RecordPisctures implements BaseColumns {
        public static final String TABLE_NAME = "record_pictures";
        public static final String COLUMN_NAME_RECORD = "record";
        public static final String COLUMN_NAME_PICTURE = "picture";
    }

}
