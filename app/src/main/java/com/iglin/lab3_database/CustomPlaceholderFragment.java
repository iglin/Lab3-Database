package com.iglin.lab3_database;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.iglin.lab3_database.util.StatsViewCreator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.iglin.lab3_database.StatsActivity.*;

/**
 * Created by user on 20.02.2017.
 */

public class CustomPlaceholderFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);


    private Calendar startingTime;
    private Calendar endingTime;
    private boolean pickingStartTime;

    private StatsViewCreator statsViewCreator = new StatsViewCreator(getContext());

    public Calendar getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Calendar startingTime) {
        this.startingTime = startingTime;
    }

    public Calendar getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Calendar endingTime) {
        this.endingTime = endingTime;
    }

    public CustomPlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CustomPlaceholderFragment newInstance(int sectionNumber) {
        CustomPlaceholderFragment fragment = new CustomPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int section = getArguments().getInt(ARG_SECTION_NUMBER);

        switch (section) {
            case MOST_FREQ_STAT_ID:
                return statsViewCreator.createMostFrequentStatsView(this, inflater, container);
            case MOST_DURABLE_STAT_ID:
                return statsViewCreator.createMostDurableStatsView(this, inflater, container);
            case SUM_ON_CATEGORIES_STAT_ID:
                return statsViewCreator.createMostFrequentStatsView(this, inflater, container);
            case DIAGRAM_STAT_ID:
                return statsViewCreator.createMostFrequentStatsView(this, inflater, container);
        }
        return null;
    }

    /**
     * Dates stuff
     */
    public void showStartTimeDialog(View view) {
        pickingStartTime = true;
        final Calendar c = startingTime;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        new TimePickerDialog(getContext(), this, hours, minutes, true).show();
    }
    public void showEndTimeDialog(View view) {
        pickingStartTime = false;
        final Calendar c = endingTime;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        new TimePickerDialog(getContext(), this, hours, minutes, true).show();
    }
    public void showStartDateDialog(View v){
        pickingStartTime = true;
        final Calendar c = startingTime;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) - 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), this, year, month, day).show();
    }
    public void showEndDateDialog(View v){
        pickingStartTime = false;
        final Calendar c = endingTime;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getContext(), this, year, month, day).show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        if (pickingStartTime) {
            startingTime = recalculateTime(false, startingTime, c);
            TextView textView = (TextView) getActivity().findViewById(R.id.tvStartDate);
            textView.setText(dateFormat.format(startingTime.getTime()));
        } else {
            endingTime = recalculateTime(false, endingTime, c);
            TextView textView = (TextView) getActivity().findViewById(R.id.tvEndDate);
            textView.setText(dateFormat.format(endingTime.getTime()));
        }
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 0, hour, minute, 0);
        if (pickingStartTime) {
            startingTime = recalculateTime(true, startingTime, c);
            TextView textView = (TextView) getActivity().findViewById(R.id.tvStartTime);
            textView.setText(timeFormat.format(startingTime.getTime()));
        } else {
            endingTime = recalculateTime(true, endingTime, c);
            TextView textView = (TextView) getActivity().findViewById(R.id.tvEndTime);
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
