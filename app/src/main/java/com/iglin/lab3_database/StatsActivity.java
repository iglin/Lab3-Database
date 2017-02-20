package com.iglin.lab3_database;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class StatsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    private static final int MOST_FREQ_STAT_ID = 0;
    private static final int MOST_DURABLE_STAT_ID = 1;
    private static final int SUM_ON_CATEGORIES_STAT_ID = 2;
    private static final int DIAGRAM_STAT_ID = 3;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);


    private Calendar startingTime;
    private Calendar endingTime;
    private boolean pickingStartTime;

    private static StatsViewCreator statsViewCreator;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.stats));
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statsViewCreator = new StatsViewCreator(getApplicationContext());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
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
                    return statsViewCreator.createMostFrequentStatsView((StatsActivity) this.getActivity(),
                            inflater, container);
                case MOST_DURABLE_STAT_ID:
                    return statsViewCreator.createMostFrequentStatsView((StatsActivity) this.getActivity(), inflater, container);
                case SUM_ON_CATEGORIES_STAT_ID:
                    return statsViewCreator.createMostFrequentStatsView((StatsActivity) this.getActivity(), inflater, container);
                case DIAGRAM_STAT_ID:
                    return statsViewCreator.createMostFrequentStatsView((StatsActivity) this.getActivity(), inflater, container);
            }
            return null;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case MOST_FREQ_STAT_ID:
                    return getString(R.string.action_freq);
                case MOST_DURABLE_STAT_ID:
                    return getString(R.string.action_max_sum);
                case SUM_ON_CATEGORIES_STAT_ID:
                    return getString(R.string.action_category_sum);
                case DIAGRAM_STAT_ID:
                    return getString(R.string.action_diag);
            }
            return null;
        }
    }
}
