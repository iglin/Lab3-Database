package com.iglin.lab3_database.statistics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.iglin.lab3_database.R;
import com.iglin.lab3_database.db.TimeTrackingDbContract;
import com.iglin.lab3_database.statistics.StatisticsActivity;

public class MostFreqActivity extends StatisticsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.action_freq));


        TextView textView = (TextView) findViewById(R.id.textStatsHeader);
        textView.setText(getString(R.string.action_freq));

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
}
