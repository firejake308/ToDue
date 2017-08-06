package com.insertcoolnamehere.todue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hassa on 8/6/2017.
 */

public class TaskListDbHelper extends SQLiteOpenHelper {
    /**
     * Must be incremented whenever the db schema changes
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TaskList.db";

    public TaskListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TaskListContract.SQL_CLEAR_TASK_LIST);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete everything and start over
        db.execSQL(TaskListContract.SQL_CLEAR_TASK_LIST);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
