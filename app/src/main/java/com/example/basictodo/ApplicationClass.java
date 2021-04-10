package com.example.basictodo;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Task> tasks;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            TaskDB db = new TaskDB(this);
            db.open();
            tasks = db.getData();
            db.close();
        }
        catch(SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
