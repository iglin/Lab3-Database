package com.iglin.lab3_database;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.model.RecordPicture;
import com.iglin.lab3_database.model.TimeRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private TimeTrackingContentProvider contentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        updateUI();
    }

    private void updateUI() {
        contentProvider = new TimeTrackingContentProvider(getApplicationContext());

        int id = getIntent().getIntExtra(MainActivity.DETAILS_RECORD_ID, 0);
        TimeRecord record = contentProvider.getRecord(id);

        TextView textView= (TextView) findViewById(R.id.textTitle);
        textView.setText(record.getTimeCategory().name());

        if (record.getDescription() != null) {
            textView = (TextView) findViewById(R.id.textDescr);
            textView.setText(record.getDescription());
        }

        textView = (TextView) findViewById(R.id.textMins);
        textView.setText(String.valueOf(record.getDuration()));

        textView = (TextView) findViewById(R.id.textTimes);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        String date = dateFormat.format(new Date(record.getStartTime()));
        dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String startTime = dateFormat.format(new Date(record.getStartTime()));
        String endTime = dateFormat.format(new Date(record.getEndTime()));
        String text = date + "\n" + startTime + " - " + endTime;
        textView.setText(text);

        if (record.getPics() != null && !record.getPics().isEmpty()) {
            Log.i(getClass().getName(), "COUNT " + record.getPics().size());
            LinearLayout layout = (LinearLayout) findViewById(R.id.listViewPics);
            layout.removeAllViews();
            for (RecordPicture picture : record.getPics()) {
                ImageView image = new ImageView(this);
                //   image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                //  image.setMaxHeight(20);
                //  image.setMaxWidth(20);
                image.setPadding(10, 10, 10, 10);
                image.setImageBitmap(picture.getPicture());
                layout.addView(image);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();
    }
}
