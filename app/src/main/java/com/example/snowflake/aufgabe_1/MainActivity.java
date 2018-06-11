package com.example.snowflake.aufgabe_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


/*
[Hinweis]	Bitte	eine	sinnvolle	Package-Benennung	verwenden.	„com.example“	ist	keine	solche.
[Hinweis]	Klasse	AlarmAdapter Klassenvariablen	immer	„private“.
[Hinweis]	Klasse	AlarmService e.printStackTrace()	ist	kein	sinnvolles	Exception-Handling!
[Hinweis]	Klasse	MainActivity Den	Dateinamen	„Alarms.txt“	besser	als	Konstante	definieren.
[Hinweis]	Klasse	Utility Konstanten	werden	in	Java	in	CAPSLOCK	benannt.	Bestehen	diese	aus	mehreren
            Worten,	so	werden	diese	durch	„_“	getrennt.	Aus	„weekdays“	wird	dann	„WEEK_DAYS“.
 */



/**
 * Created by Gruppe11 on 10/5/17.
 * Main class connects the save button and adds the toolbar
 * adding listview to adapter, methode remove Alarm entry
 * and methode update
 */

public class MainActivity extends AppCompatActivity {

    private AlarmService alarmService = null;
    private AlarmAdapter ad = null;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmchooser);


        //always use this instance of alarmService!!
        alarmService = new AlarmService();

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addAlarmButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
                intent.putExtra("alarmService", alarmService);
                startActivity(intent);
            }
        });

        alarmService.read(MainActivity.this, "Alarms.txt");

        ad = new AlarmAdapter(MainActivity.this, 0, alarmService.getAlarmlist(), alarmService);
        ListView lv = (ListView) findViewById(R.id.listviewalarm);
        lv.setAdapter(ad);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().
                setDisplayShowHomeEnabled(true);

        if (MediaAdapter.isPlaying()) {
            Intent intent = new Intent(getApplicationContext(), ActiveAlarmActivity.class);
            startActivity(intent);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void settingRingtone(MenuItem item) {

        Intent i = new Intent(this, MyPreferencesActivity.class);
        startActivity(i);

    }


    // Update List View - use AlarmService functions!

    /**
     * Methode remove removes the selected Alarm entry from the listview.
     * writes the data from listview into a text file
     * calls methode update for the new List.
     *
     * @param index to get the selected Alarm
     */
    public void removeEntry(int index) {

        alarmService.remove(index);
        alarmService.write(this, "Alarms.txt");

        updateList();
    }

    /**
     * clears the List, reads the data from the text file
     * displays the new List.
     */
    public void updateList() {


        ad.clear();
        alarmService.read(this, "Alarms.txt");
        ad.notifyDataSetChanged();

    }

}

