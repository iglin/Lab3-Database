package com.iglin.lab3_database.util;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.iglin.lab3_database.CustomPlaceholderFragment;
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

    public void fillHeader(Fragment activity, View rootView, String header) {
      /*  TextView textView = (TextView) rootView.findViewById(R.id.textStatsHeader);
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
        textView.setText(timeFormat.format(current.getTime()));*/
    }

    public View createMostFrequentStatsView(final CustomPlaceholderFragment fragment, LayoutInflater inflater, final ViewGroup container) {
        final View rootView = inflater.inflate(R.layout.most_freq_fragment, container, false);
        fillHeader(fragment, rootView, fragment.getContext().getString(R.string.action_freq));

        Button fab = (Button) rootView.findViewById(R.id.buttonFreq);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = contentProvider.getMostFrequentActivities(fragment.getStartingTime(), fragment.getEndingTime());

                String[] from = new String[] {
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT,
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT
                };
                int[] to = new int[] { R.id.textItemText, R.id.textItemNumber };

                SimpleCursorAdapter mCursorAd = new SimpleCursorAdapter(fragment.getContext(), R.layout.simple_item, cursor, from, to, 0);
                ListView mLv = (ListView) fragment.getActivity().findViewById(R.id.listStat);
                mLv.setAdapter(mCursorAd);
            }
        });
        return rootView;
    }

    public View createMostDurableStatsView(final CustomPlaceholderFragment fragment, LayoutInflater inflater, final ViewGroup container) {
        final View rootView = inflater.inflate(R.layout.most_durable_fragment, container, false);
        fillHeader(fragment, rootView, fragment.getContext().getString(R.string.action_max_sum));

        Button fab = (Button) rootView.findViewById(R.id.buttonDur);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = contentProvider.getMostDurableActivities(fragment.getStartingTime(), fragment.getEndingTime());

                String[] from = new String[] {
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT,
                        TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT
                };
                int[] to = new int[] { R.id.textItemText, R.id.textItemNumber };

                SimpleCursorAdapter mCursorAd = new SimpleCursorAdapter(fragment.getContext(), R.layout.simple_item, cursor, from, to, 0);
                ListView mLv = (ListView) fragment.getActivity().findViewById(R.id.listStatDur);
                mLv.setAdapter(mCursorAd);
            }
        });
        return rootView;
    }

}
