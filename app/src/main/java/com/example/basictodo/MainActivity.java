package com.example.basictodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int TODO_ACTIVITY = 1;

    private FloatingActionButton fltBtn;
    private Button btnTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTasks = (Button) findViewById(R.id.btnTasks);
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.basictodo.TaskActivity.class);
                startActivity(intent);
            }
        });
        fltBtn = (FloatingActionButton) findViewById(R.id.fltBtn);
        fltBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        com.example.basictodo.TODODataActivity.class);
                startActivityForResult(intent, TODO_ACTIVITY);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TODO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                try {
                    TaskDB db = new TaskDB(this);
                    db.open();
                    db.createEntry(data.getStringExtra("title"), data.getStringExtra("priority"),
                            data.getStringExtra("task"), data.getStringExtra("date"),
                            data.getStringExtra("time"));
                    db.close();
                }
                catch (SQLiteException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}