package com.example.basictodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskActivity extends AppCompatActivity implements TaskAdapter.LongItemSelected {
    private final static int TODO_ACTIVITY = 1;
    private final static int EDIT_TODO_ACTIVITY = 2;

    public static boolean onLongClick = false;

    private String title;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private Button btnHome;
    private TextView tvInfo;
    private FloatingActionButton fltBtnTaskActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        recyclerView = (RecyclerView) findViewById(R.id.recView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        updateData();

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        if (ApplicationClass.tasks.isEmpty())
            tvInfo.setVisibility(View.VISIBLE);
        else
            tvInfo.setVisibility(View.GONE);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this, com.example.basictodo.MainActivity.class);
                startActivity(intent);
            }
        });

        fltBtnTaskActivity = (FloatingActionButton) findViewById(R.id.fltBtnTaskActivity);
        fltBtnTaskActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this,
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
                    updateData();
                    if (ApplicationClass.tasks.isEmpty())
                        tvInfo.setVisibility(View.VISIBLE);
                    else
                        tvInfo.setVisibility(View.GONE);
                    db.close();
                }
                catch (SQLiteException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (requestCode == EDIT_TODO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                try {
                    TaskDB db = new TaskDB(this);
                    db.open();
                    db.updateEntry(title, data.getStringExtra("priority"), data.getStringExtra("task"),
                            data.getStringExtra("date"), data.getStringExtra("time"));
                    updateData();
                    db.close();
                }
                catch (SQLiteException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void deleteTask(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.delete).
                setTitle("Delete TODO-task").setMessage("Are you sure you want to delete this task?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = ApplicationClass.tasks.get(TaskAdapter.getPosition()).getTitle();
                        try {
                            TaskDB db = new TaskDB(TaskActivity.this);
                            db.open();
                            db.deleteEntry(title);
                            updateData();
                            if (ApplicationClass.tasks.isEmpty())
                                tvInfo.setVisibility(View.VISIBLE);
                            else
                                tvInfo.setVisibility(View.GONE);
                            db.close();
                        }
                        catch (SQLiteException e) {
                            Toast.makeText(TaskActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TaskAdapter.uncheckCheckbox();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateData() {
        try {
            TaskDB db = new TaskDB(this);
            db.open();
            ApplicationClass.tasks = db.getData();
            db.close();
        }
        catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        adapter = new TaskAdapter(this, ApplicationClass.tasks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLongItemSelected(int id, String title) {
        this.title = title;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setIcon(R.drawable.edit).setTitle("Edit the TODO-task").
                setMessage("Do you want to edit this task?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onLongClick = true;
                Intent intent = new Intent(TaskActivity.this, com.example.basictodo.TODODataActivity.class);
                startActivityForResult(intent, EDIT_TODO_ACTIVITY);
            }
        }).setNegativeButton("No", null).create();
        alertDialog.show();
    }
}