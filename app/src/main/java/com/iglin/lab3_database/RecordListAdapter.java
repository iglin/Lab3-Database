package com.iglin.lab3_database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iglin.lab3_database.model.TimeRecord;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 19.02.2017.
 */

public class RecordListAdapter extends ArrayAdapter<TimeRecord> {
    public RecordListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RecordListAdapter(Context context, int resource, List<TimeRecord> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item, null);
        }

        TimeRecord timeRecord = getItem(position);

        if (timeRecord != null) {
            TextView textView = (TextView) v.findViewById(R.id.tvText);
           // ImageView imageView = (ImageView) v.findViewById(R.id.ivImg);
            if (textView != null) {
                Log.i(getClass().getName(), new Date(timeRecord.getStartTime()).toString());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                String date = dateFormat.format(new Date(timeRecord.getStartTime()));
                dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String startTime = dateFormat.format(new Date(timeRecord.getStartTime()));
                String endTime = dateFormat.format(new Date(timeRecord.getEndTime()));
                String text = timeRecord.getTimeCategory().name() + " " + date + " " + startTime + " - " + endTime;
                textView.setText(text);
            }
           /* if (imageView != null && timeRecord.getPics() != null && !timeRecord.getPics().isEmpty()) {
                imageView.setImageBitmap(timeRecord.getPics().get(0).getPicture());
            }*/
        }
        return v;
    }
}
