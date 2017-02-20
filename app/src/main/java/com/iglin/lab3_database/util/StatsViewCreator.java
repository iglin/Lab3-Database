package com.iglin.lab3_database.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iglin.lab3_database.R;
import com.iglin.lab3_database.StatsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 20.02.2017.
 */

public class StatsViewCreator {
    public StatsViewCreator() {
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private void fillHeader(StatsActivity activity, View rootView, String header) {
        TextView textView = (TextView) rootView.findViewById(R.id.textStatsHeader);
        textView.setText(header);

        Calendar current = Calendar.getInstance();
        current.set(Calendar.MILLISECOND, 0);
        current.set(Calendar.SECOND, 0);
        Calendar previous = (Calendar) current.clone();
        previous.add(Calendar.MONTH, -1);
        activity.setStartingTime(previous);
        activity.setEndingTime(current);

        textView = (TextView) rootView.findViewById(R.id.tvStartDate);
        textView.setText(dateFormat.format(previous.getTime()));

        textView = (TextView) rootView.findViewById(R.id.tvEndDate);
        textView.setText(dateFormat.format(current.getTime()));

        textView = (TextView) rootView.findViewById(R.id.tvStartTime);
        textView.setText(timeFormat.format(previous.getTime()));
        textView = (TextView) rootView.findViewById(R.id.tvEndTime);
        textView.setText(timeFormat.format(current.getTime()));

        //String startTime = timeFormat.format(new Date(timeRecord.getStartTime()));
    }



    public View createMostFrequentStatsView(StatsActivity activity, LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.most_freq_fragment, container, false);
        fillHeader(activity, rootView, activity.getString(R.string.action_freq));
        return rootView;
    }
}
