package com.example.basictodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SetDate extends AppCompatActivity {

    private CalendarView cvDate;
    private Button btnSetDate;
    private TextView tvDate;

    private String date;
    private boolean isDateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);

        cvDate = (CalendarView) findViewById(R.id.cvDate);
        tvDate = (TextView) findViewById(R.id.tvCalendar);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);

        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i2 + "/" + (i1+1) + "/" + i;
                isDateChanged = true;
            }
        });

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDateChanged == false) {
                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);

                    date = day + "/" + (month+1) + "/" + year;
                }
                Intent intent = new Intent();
                intent.putExtra("date", date);
                setResult(RESULT_OK, intent);
                SetDate.this.finish();
                Toast.makeText(SetDate.this, "Date marked succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}