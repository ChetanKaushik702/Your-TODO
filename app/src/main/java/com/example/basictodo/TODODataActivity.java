package com.example.basictodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TODODataActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final int SET_DATE = 1;

    private ImageView ivDate, ivTime, ivLowPrior, ivHighPrior;
    private TextView tvDate, tvTime, tvPriority;
    private EditText etTitle, etDescription;
    private Button btnSetTodo, btnCancel;

    private String date;
    private String time;

    private boolean isBtnDate = false, isBtnTime = false, isLowPrior = false, isHighPrior = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_o_d_o_data);

        // text views
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPriority = (TextView) findViewById(R.id.tvPrior);

        // edit texts
        etTitle = (EditText) findViewById(R.id.etTitle);
        if (TaskActivity.onLongClick)
            etTitle.setVisibility(View.GONE);
        etDescription = (EditText) findViewById(R.id.etTask);

        // buttons
        btnSetTodo = (Button) findViewById(R.id.btnSetTodo);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnSetTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDetails()) {
                    Intent intent = new Intent();
                    intent.putExtra("title", etTitle.getText().toString().trim());
                    intent.putExtra("task", etDescription.getText().toString().trim());
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    String prior = null;
                    if (isLowPrior) prior = "LOW";
                    else    prior = "HIGH";
                    intent.putExtra("priority", prior);
                    setResult(RESULT_OK, intent);
                    TODODataActivity.this.finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                TODODataActivity.this.finish();
            }
        });

        // image views
        ivLowPrior = (ImageView) findViewById(R.id.ivLowPrior);
        ivHighPrior = (ImageView) findViewById(R.id.ivHighPrior);
        ivDate = (ImageView) findViewById(R.id.ivDate);
        ivTime = (ImageView) findViewById(R.id.ivTime);

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnDate = true;
                Intent intent = new Intent(TODODataActivity.this, com.example.basictodo.SetDate.class);
                startActivityForResult(intent, SET_DATE);
            }
        });

        ivTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnTime = true;
                DialogFragment timeFragment = new TimePicker();
                timeFragment.show(getSupportFragmentManager(), "time picker");
            }
        });

        ivLowPrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLowPrior = true;
                tvPriority.setText("Priority: LOW");
            }
        });

        ivHighPrior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHighPrior = true;
                tvPriority.setText("Priority: HIGH");
            }
        });
    }

    private boolean checkDetails() {
        String title = etTitle.getText().toString().trim();
        String task = etDescription.getText().toString().trim();

        if (TaskActivity.onLongClick) {
            if (task.equals("") || !isBtnDate || !isBtnTime || (!isLowPrior && !isHighPrior)) {
                Toast.makeText(this, "Please select all the fields!!",
                        Toast.LENGTH_LONG).show();
                return false;
            } else return true;
        }
        else{
                if (title.equals("") || task.equals("") || !isBtnDate || !isBtnTime || (!isLowPrior && !isHighPrior)) {
                    Toast.makeText(this, "Please select all the fields!!",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                else return true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SET_DATE) {
            if (resultCode == RESULT_OK) {
                date = data.getStringExtra("date");
                tvDate.setText(date);
            }
        }
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        String isAMorPM = "";
        if (i == 0) {
            i += 12;
            isAMorPM += "AM";
        }
        else if (i >= 12) {
            if (i > 12)
                i -= 12;
            isAMorPM += "PM";
        }
        else
            isAMorPM += "AM";
        String h = Integer.toString(i);
        String min = "";
        if (Integer.toString(i1).length() == 1)
            min += "0" + Integer.toString(i1);
        else
            min += Integer.toString(i1);
        time = h + ":" + min + " " + isAMorPM;
        tvTime.setText(time);
        Toast.makeText(this, "Time marked successfully!!", Toast.LENGTH_SHORT).show();
    }
}