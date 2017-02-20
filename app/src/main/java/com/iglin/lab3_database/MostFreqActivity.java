package com.iglin.lab3_database;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.db.TimeTrackingDbContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MostFreqActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private TimeTrackingContentProvider contentProvider;

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private Calendar startingTime;
    private Calendar endingTime;
    private boolean pickingStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_freq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.stats));

        contentProvider = new TimeTrackingContentProvider(getApplicationContext());

        TextView textView = (TextView) findViewById(R.id.textStatsHeader);
        textView.setText(getString(R.string.action_freq));

        Calendar current = Calendar.getInstance();
        current.set(Calendar.MILLISECOND, 0);
        current.set(Calendar.SECOND, 0);
        Calendar previous = (Calendar) current.clone();
        previous.add(Calendar.MONTH, -1);
        startingTime = previous;
        endingTime = current;

        textView = (TextView) findViewById(R.id.tvStartDate);
        textView.setText(dateFormat.format(previous.getTime()));

        textView = (TextView) findViewById(R.id.tvEndDate);
        textView.setText(dateFormat.format(current.getTime()));

        textView = (TextView) findViewById(R.id.tvStartTime);
        textView.setText(timeFormat.format(previous.getTime()));
        textView = (TextView) findViewById(R.id.tvEndTime);
        textView.setText(timeFormat.format(current.getTime()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = contentProvider.getMostFrequentActivities(startingTime, endingTime);

                String[] from = new String[] {
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT,
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT
                };
                int[] to = new int[] { R.id.textItemText, R.id.textItemNumber };

                SimpleCursorAdapter mCursorAd = new SimpleCursorAdapter(getApplicationContext(), R.layout.simple_item, cursor, from, to, 0);
                ListView mLv = (ListView) findViewById(R.id.listStat);
                mLv.setAdapter(mCursorAd);
            }
        });
    }


    /**
     * Dates stuff
     */
    public void showStartTimeDialog(View view) {
        pickingStartTime = true;
        final Calendar c = startingTime;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        new TimePickerDialog(this, this, hours, minutes, true).show();
    }
    public void showEndTimeDialog(View view) {
        pickingStartTime = false;
        final Calendar c = endingTime;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        new TimePickerDialog(this, this, hours, minutes, true).show();
    }
    public void showStartDateDialog(View v){
        pickingStartTime = true;
        final Calendar c = startingTime;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) - 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();
    }
    public void showEndDateDialog(View v){
        pickingStartTime = false;
        final Calendar c = endingTime;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        if (pickingStartTime) {
            startingTime = recalculateTime(false, startingTime, c);
            TextView textView = (TextView) findViewById(R.id.tvStartDate);
            textView.setText(dateFormat.format(startingTime.getTime()));
        } else {
            endingTime = recalculateTime(false, endingTime, c);
            TextView textView = (TextView) findViewById(R.id.tvEndDate);
            textView.setText(dateFormat.format(endingTime.getTime()));
        }
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 0, hour, minute, 0);
        if (pickingStartTime) {
            startingTime = recalculateTime(true, startingTime, c);
            TextView textView = (TextView) findViewById(R.id.tvStartTime);
            textView.setText(timeFormat.format(startingTime.getTime()));
        } else {
            endingTime = recalculateTime(true, endingTime, c);
            TextView textView = (TextView) findViewById(R.id.tvEndTime);
            textView.setText(timeFormat.format(endingTime.getTime()));
        }
    }

    private Calendar recalculateTime(boolean updateTime, Calendar previousValue, Calendar newValue) {
        if (updateTime) {
            previousValue.set(Calendar.HOUR_OF_DAY, newValue.get(Calendar.HOUR_OF_DAY));
            previousValue.set(Calendar.MINUTE, newValue.get(Calendar.MINUTE));
        } else {
            previousValue.set(Calendar.DATE, newValue.get(Calendar.DATE));
            previousValue.set(Calendar.MONTH, newValue.get(Calendar.MONTH));
            previousValue.set(Calendar.YEAR, newValue.get(Calendar.YEAR));
        }
        return previousValue;
    }
}
