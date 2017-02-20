package com.iglin.lab3_database.statistics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.iglin.lab3_database.R;
import com.iglin.lab3_database.db.TimeTrackingDbContract;
import com.iglin.lab3_database.model.TimeCategory;

import java.util.ArrayList;
import java.util.List;

public class SumByCategoriesActivity extends StatisticsActivity {
    private List<TimeCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sum_by_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fillDatesHeader();

        TextView textView = (TextView) findViewById(R.id.textStatsHeader);
        textView.setText(getString(R.string.action_category_sum));

        categories = new ArrayList<>();

        TimeCategory[] array = TimeCategory.values();
        int j = 0;
        TableRow tableRow = (TableRow) findViewById(R.id.row1);
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            final View child = tableRow.getChildAt(i);
            if (child instanceof CheckBox) {
                ((CheckBox) child).setText(array[j].name());
                ((CheckBox) child).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        TimeCategory timeCategory = TimeCategory.valueOf(((CheckBox) child).getText().toString());
                        if (b) {
                            categories.add(timeCategory);
                        } else {
                            categories.remove(timeCategory);
                        }

                        updateData();
                    }
                });
                j++;
            }
        }

        tableRow = (TableRow) findViewById(R.id.row2);
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            final View child = tableRow.getChildAt(i);
            if (child instanceof CheckBox) {
                ((CheckBox) child).setText(array[j].name());
                ((CheckBox) child).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        TimeCategory timeCategory = TimeCategory.valueOf(((CheckBox) child).getText().toString());
                        if (b) {
                            categories.add(timeCategory);
                        } else {
                            categories.remove(timeCategory);
                        }

                        updateData();
                    }
                });
                j++;
            }
        }

        updateData();
    }

    @Override
    protected void updateData() {
        Cursor cursor = contentProvider.getSumTimeByCategories(startingTime, endingTime, categories);

        if (cursor == null) return;
        String[] from = new String[] {
                TimeTrackingDbContract.Statistics.COLUMN_NAME_TEXT,
                TimeTrackingDbContract.Statistics.COLUMN_NAME_STAT
        };
        int[] to = new int[] { R.id.textItemText, R.id.textItemNumber };

        SimpleCursorAdapter mCursorAd = new SimpleCursorAdapter(getApplicationContext(), R.layout.simple_item, cursor, from, to, 0);
        ListView mLv = (ListView) findViewById(R.id.listStat);
        mLv.setAdapter(mCursorAd);
    }
}
