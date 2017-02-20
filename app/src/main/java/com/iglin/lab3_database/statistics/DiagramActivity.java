package com.iglin.lab3_database.statistics;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.iglin.lab3_database.R;
import com.iglin.lab3_database.db.TimeTrackingDbContract;
import com.iglin.lab3_database.statistics.StatisticsActivity;

import java.util.ArrayList;
import java.util.List;

public class DiagramActivity  extends StatisticsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diagram);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fillDatesHeader();

        TextView textView = (TextView) findViewById(R.id.textStatsHeader);
        textView.setText(getString(R.string.action_diag));

        updateData();
    }

    @Override
    protected void updateData() {
        Cursor cursor = contentProvider.getMostDurableActivities(startingTime, endingTime);

        List<PieEntry> entries = new ArrayList<>();

        int indexStat, indexName;
        while (cursor.moveToNext()) {
            indexStat = cursor.getColumnIndex(TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT);
            indexName = cursor.getColumnIndex(TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT);
            // turn your data into Entry objects
            entries.add(new PieEntry(cursor.getInt(indexStat), cursor.getString(indexName)));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, getString(R.string.category));
        pieDataSet.setColors(Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW);
        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        pieChart.setContentDescription(getString(R.string.category));
        Description description = new Description();
        description.setText(getString(R.string.diag_text));
        pieChart.setDescription(description);
        Log.i(getClass().getName(), pieDataSet.toString());
        pieChart.setData(new PieData(pieDataSet));
    }
}
