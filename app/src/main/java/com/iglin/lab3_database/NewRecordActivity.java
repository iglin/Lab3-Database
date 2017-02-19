package com.iglin.lab3_database;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.model.RecordPicture;
import com.iglin.lab3_database.model.TimeCategory;
import com.iglin.lab3_database.model.TimeRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private TimeTrackingContentProvider contentProvider;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean startTime = true;

    private Date startingTime;
    private Date endingTime;
    private Date dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        contentProvider = new TimeTrackingContentProvider(getApplicationContext());

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<TimeCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TimeCategory.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        TextView textView = (TextView) findViewById(R.id.textViewDate);
        String result;
        if (day < 10) result = "0" + day + "/";
        else result = day + "/";
        if (month < 9) result += "0" + (month + 1) + "/" + year;
        else result += (month + 1) + "/" + year;
        textView.setText(result);

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        dateTime = new Date(c.getTimeInMillis());
        Log.i(getClass().getName(), dateTime.toString());
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        TextView textView;
        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 0, hour, minute, 0);
        if (startTime) {
            textView = (TextView) findViewById(R.id.textViewTime);
            startingTime = new Date(c.getTimeInMillis());
        } else {
            textView = (TextView) findViewById(R.id.textViewEndTime);
            endingTime = new Date(c.getTimeInMillis());
        }
        String result;
        if (hour < 10) result = "0" + hour + ":";
        else result = hour + ":";
        if (minute < 10) result += "0" + minute;
        else result += minute;
        textView.setText(result);
    }

    public void showDatePickerDialog(View v){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this,this, year, month, day).show();
    }

    public void showTimePickerDialog(View v){
        startTime = true;
        new TimePickerDialog(this, this, 0, 0, true).show();
    }

    public void showEndTimePickerDialog(View v){
        startTime = false;
        new TimePickerDialog(this, this, 0, 0, true).show();
    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            LinearLayout layout = (LinearLayout)findViewById(R.id.picsLayout);
            ImageView image = new ImageView(this);
            //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
           // image.setMaxHeight(20);
            //image.setMaxWidth(20);
            image.setImageBitmap(imageBitmap);
            layout.addView(image);
        }
    }

    public void saveRecord(View view) {
        if (dateTime == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.date_error), Toast.LENGTH_LONG).show();
            return;
        }
        if (startingTime == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.start_time_error), Toast.LENGTH_LONG).show();
            return;
        }
        if (endingTime == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.end_time_error), Toast.LENGTH_LONG).show();
            return;
        }
        dateTime.setHours(startingTime.getHours());
        dateTime.setMinutes(startingTime.getMinutes());
        dateTime.setSeconds(0);
        Date endTime = new Date(dateTime.getTime());
        endTime.setHours(endingTime.getHours());
        endTime.setMinutes(endingTime.getMinutes());
        long duration = (endTime.getTime() - dateTime.getTime()) / 60000;
        if (duration < 1) {
            Toast.makeText(getApplicationContext(), getString(R.string.time_error), Toast.LENGTH_LONG).show();
            return;
        }

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setDuration((int) duration);
        timeRecord.setStartTime(dateTime.getTime());
        timeRecord.setEndTime(endTime.getTime());
        String category = String.valueOf(((Spinner) findViewById(R.id.spinner)).getSelectedItem());
        timeRecord.setTimeCategory(TimeCategory.valueOf(category));

        Editable editable = ((EditText) findViewById(R.id.editTextDescr)).getText();
        if (editable != null && editable.length() > 0) timeRecord.setDescription(editable.toString());

        LinearLayout layout = (LinearLayout)findViewById(R.id.picsLayout);
        List<RecordPicture> list = new ArrayList<>(layout.getChildCount());
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof ImageView) {
                Bitmap bitmap = ((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap();
                list.add(new RecordPicture(bitmap));
            }
        }
        if (!list.isEmpty()) {
            timeRecord.setPics(list);
        }

        Log.i(getClass().getName(), "START DATE " + new Date(timeRecord.getStartTime()).toString());
        Log.i(getClass().getName(), "END DATE " + new Date(timeRecord.getEndTime()).toString());
        Log.i(getClass().getName(), "DURATION " + timeRecord.getDuration());

        contentProvider.insertRecord(timeRecord);
        onBackPressed();
    }
}
