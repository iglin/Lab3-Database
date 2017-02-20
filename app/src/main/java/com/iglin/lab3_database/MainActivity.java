package com.iglin.lab3_database;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.model.TimeRecord;
import com.iglin.lab3_database.statistics.DiagramActivity;
import com.iglin.lab3_database.statistics.MostDurableActivity;
import com.iglin.lab3_database.statistics.MostFreqActivity;
import com.iglin.lab3_database.statistics.SumByCategoriesActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CM_DELETE_ID = 1;

    public static final String DETAILS_RECORD_ID = "record_id";

    private TimeTrackingContentProvider contentProvider;

    private List<TimeRecord> recordsList;
    private RecordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentProvider = new TimeTrackingContentProvider(getApplicationContext());
        loadRecords();
        ListView lvData = (ListView) findViewById(R.id.listView);
        registerForContextMenu(lvData);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TimeRecord timeRecord = (TimeRecord) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(DETAILS_RECORD_ID, timeRecord.getId());
                startActivity(intent);
            }
        });
        // добавляем контекстное меню к списку

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRecords();

               // Snackbar.make(view, timeRecord.getDescription() +" " + timeRecord.getDuration() + " " + timeRecord.getTimeCategory(), Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (contentProvider != null) {
            loadRecords();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (contentProvider != null) {
            loadRecords();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_new:
                intent = new Intent(this, NewRecordActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_most_freq:
                intent = new Intent(this, MostFreqActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_dur:
                intent = new Intent(this, MostDurableActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_diag:
                intent = new Intent(this, DiagramActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_cat:
                intent = new Intent(this, SumByCategoriesActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            TimeRecord timeRecord = adapter.getItem(acmi.position);
            contentProvider.deleteRecord(timeRecord);

            loadRecords();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void loadRecords() {
        recordsList = contentProvider.getRecords();
        adapter = new RecordListAdapter(getApplicationContext(), R.id.listView, recordsList);
        ListView lvData = (ListView) findViewById(R.id.listView);
        lvData.setAdapter(adapter);
    }
}
