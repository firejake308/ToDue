package com.insertcoolnamehere.todue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by hassa on 8/6/2017.
 */

public final class TaskListContract {

    public static final String SQL_CREATE_TASK_LIST = "CREATE TABLE " + TaskListEntry.TABLE_NAME
            + " (" + TaskListEntry._ID +" INTEGER PRIMARY KEY," + TaskListEntry.COLUMN_NAME_TITLE
            + " TEXT,"+ TaskListEntry.COLUMN_NAME_DO_DATE + " DATE," + TaskListEntry.COLUMN_NAME_DUE_DATE
            + " DATE," +TaskListEntry.COLUMN_NAME_CATEGORY + " TEXT)";

    public static final String SQL_CLEAR_TASK_LIST = "DROP TABLE IF EXISTS " + TaskListEntry.TABLE_NAME;

    private TaskListContract(){}

    public static class TaskListEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasklist";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DO_DATE = "dodate";
        public static final String COLUMN_NAME_DUE_DATE = "duedate";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
