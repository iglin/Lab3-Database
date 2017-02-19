package com.iglin.lab3_database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.iglin.lab3_database.db.TimeTrackingContentProvider;
import com.iglin.lab3_database.db.TimeTrackingDbContract;
import com.iglin.lab3_database.model.TimeCategory;
import com.iglin.lab3_database.model.TimeRecord;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CM_DELETE_ID = 1;

    private TimeTrackingContentProvider contentProvider;

    List<TimeRecord> recordsList;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            Intent intent = new Intent(this, NewRecordActivity.class);
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
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
          //  db.delRec(acmi.id);
            // обновляем курсор
            loadRecords();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void loadRecords() {
        recordsList = contentProvider.getRecords();
        ListAdapter adapter = new RecordListAdapter(getApplicationContext(), R.id.listView, recordsList);
        ListView lvData = (ListView) findViewById(R.id.listView);
        lvData.setAdapter(adapter);
    }
}
