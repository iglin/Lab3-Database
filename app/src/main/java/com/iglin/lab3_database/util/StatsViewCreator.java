package com.iglin.lab3_database.util;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.iglin.lab3_database.R;
import com.iglin.lab3_database.StatsActivity;
import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.db.TimeTrackingDbContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by user on 20.02.2017.
 */

public class StatsViewCreator {
    public StatsViewCreator(Context context) {
        contentProvider = new TimeTrackingContentProvider(context);
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private TimeTrackingContentProvider contentProvider;

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
    }



    public View createMostFrequentStatsView(final StatsActivity activity, LayoutInflater inflater, final ViewGroup container) {
        final View rootView = inflater.inflate(R.layout.most_freq_fragment, container, false);
        fillHeader(activity, rootView, activity.getString(R.string.action_freq));

        FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = contentProvider.getMostFrequentActivities(activity.getStartingTime(), activity.getEndingTime());
                //activity.startManagingCursor(cursor);

                // формируем столбцы сопоставления
                String[] from = new String[] {
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT,
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT
                };
                int[] to = new int[] { R.id.textItemText, R.id.textItemNumber };

                // создаем адаптер и настраиваем список
                SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(activity.getApplicationContext(), R.layout.simple_item, cursor, from, to, 0);

                ListView lvData = (ListView) rootView.findViewById(R.id.listStat);
                lvData.setAdapter(scAdapter);

             /*   int index = cursor.getColumnIndex(TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT);
                String text = "";
                while (cursor.moveToNext()) {
                    index = cursor.getColumnIndex(TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT);
                    text = cursor.getString(index);
                    Log.i(getClass().getName(), text);
                    index = cursor.getColumnIndex(TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT);
                    Log.i(getClass().getName(), String.valueOf(cursor.getInt(index)));
                }
                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
        return rootView;
    }
}
