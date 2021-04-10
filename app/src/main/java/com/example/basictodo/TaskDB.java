package com.example.basictodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TaskDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PRIOR = "_prior";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DATE = "_date";
    public static final String KEY_TIME = "_time";
    public static final String KEY_TASK = "_task";

    private final String DATABASE_NAME = "TasksDB";
    private final String DATABASE_TABLE = "TasksTable";
    private final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public TaskDB(Context ourContext) {
        this.ourContext = ourContext;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " ( " + KEY_ROWID +
                             " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_PRIOR + " TEXT NOT NULL, " +
                             KEY_TITLE + " TEXT NOT NULL, " + KEY_TASK + " TEXT NOT NULL, " + KEY_DATE +
                             " TEXT NOT NULL, " + KEY_TIME + " TEXT NOT NULL);";
            sqLiteDatabase.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }

    }

    public TaskDB open() throws SQLiteException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public ArrayList<Task> getData() {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task;
        String[] columns = new String[] {KEY_ROWID, KEY_PRIOR, KEY_TITLE, KEY_TASK, KEY_DATE, KEY_TIME};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, KEY_PRIOR);

        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iPrior = c.getColumnIndex(KEY_PRIOR);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iTask = c.getColumnIndex(KEY_TASK);
        int iDate = c.getColumnIndex(KEY_DATE);
        int iTime = c.getColumnIndex(KEY_TIME);

        for (c.moveToFirst() ; !c.isAfterLast() ; c.moveToNext()) {
            task = new Task(c.getString(iTitle), c.getString(iPrior), c.getString(iDate), c.getString(iTime), c.getString(iTask));
            tasks.add(task);
        }
        return tasks;
    }

    public long createEntry(String title, String prior, String task, String date, String time) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_PRIOR, prior);
        cv.put(KEY_TASK, task);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TIME, time);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public long deleteEntry(String title) {
        return ourDatabase.delete(DATABASE_TABLE, KEY_TITLE + "=?", new String[]{title});
    }

    public long updateEntry(String title, String prior, String task, String date, String time) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_PRIOR, prior);
        cv.put(KEY_TASK, task);
        cv.put(KEY_DATE, date);
        cv.put(KEY_TIME, time);
        return ourDatabase.update(DATABASE_TABLE, cv, KEY_TITLE  + "=?", new String[]{title});
    }
}
