package com.example.snowflake.aufgabe_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

/**
 * ControlActivity
 * - Provides functionality to Layout Components of activity_control.xml
 * - Creates new Alarm Objects
 */

public class ControlActivity extends AppCompatActivity {

    private final int minutes = 59, hours = 23;
    private FloatingActionButton fbutton_save;
    private NumberPicker hourPicker, minutePicker;
    private View view_label, view_repeat;
    private TextView textview_label, textview_repeat;

    private String[] ringtoneRepeatances;
    private String[] ringtoneRepeatancesShortcuts;

    private boolean[] checkRingtoneRepeatances;
    private ArrayList<String> selectedRingtoneRepeatances = new ArrayList<>();

    private AlarmService alarmService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Intent intent = getIntent();
        alarmService = (AlarmService) intent.getSerializableExtra("alarmService");

        addToolbar();
        addWheelPicker();
        addLabelDialog();
        addRepeatDialog();
        addSaveButton();
    }

    /**
     * addToolbar
     * - sets Toolbar as SupportActionBar
     */
    public void addToolbar() {
        Toolbar toolbarCC = (Toolbar) findViewById(R.id.toolbarCC);
        setSupportActionBar(toolbarCC);
    }


    /**
     * addWheelPicker
     * - Sets boundaries for time picker (Min- /Max value) composed of two Number Pickers
     * - Sets the current value of time pickers to current time
     */
    public void addWheelPicker() {
        final Time currentTime = new Time(System.currentTimeMillis());

        hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(hours);
        hourPicker.setValue(currentTime.getHours());

        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(minutes);
        minutePicker.setValue(currentTime.getMinutes());
    }


    /**
     * addLabelDialog
     * - Creates a dialog for entering an alarm title
     * - Writes title to Textview of ControlActivity
     * -limited edittext to 25 char.
     */
    public void addLabelDialog() {
        textview_label = (TextView) findViewById(R.id.textview_label);
        view_label = (View) findViewById(R.id.layout_label);
        view_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ControlActivity.this);
                mBuilder.setTitle("Label");

                final EditText input = new EditText(ControlActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
                mBuilder.setView(input);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().equals("")) {

                            textview_label.setText("Alarm");
                        } else {
                            textview_label.setText(input.getText().toString());
                        }
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.show();
            }
        });
    }


    /**
     * addRepeatingDialog
     * - Creates a dialog with multiple choice items of weekdays on which the alarm should be repeated
     * - Writes choices as shortcuts to TextView of ControlActivity
     */
    public void addRepeatDialog() {

        textview_repeat = (TextView) findViewById(R.id.textview_repeat);

        ringtoneRepeatances = getResources().getStringArray(R.array.ringtone_repeatance);
        ringtoneRepeatancesShortcuts = getResources().getStringArray(R.array.ringtone_repeatance_shortcuts);

        checkRingtoneRepeatances = new boolean[ringtoneRepeatancesShortcuts.length];
        for (int i = 0; i < checkRingtoneRepeatances.length; i++) {
            checkRingtoneRepeatances[i] = false;
        }

        view_repeat = (View) findViewById(R.id.layout_repeat);
        view_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ControlActivity.this);
                mBuilder.setTitle("Repeat");
                mBuilder.setMultiChoiceItems(ringtoneRepeatances, checkRingtoneRepeatances, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            checkRingtoneRepeatances[which] = true;
                        } else if (!isChecked) {
                            checkRingtoneRepeatances[which] = false;
                        } else {
                            // DO NOTHING
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String text = "";
                        selectedRingtoneRepeatances.clear();
                        for (int i = 0; i < checkRingtoneRepeatances.length; i++) {
                            if (checkRingtoneRepeatances[i] == true) {
                                selectedRingtoneRepeatances.add(ringtoneRepeatancesShortcuts[i]);
                            }
                        }
                        textview_repeat.setText(Utility.getAlarmRepetitionShortcuts(selectedRingtoneRepeatances));
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Toggle All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (selectedRingtoneRepeatances.isEmpty()) {
                            for (int i = 0; i < ringtoneRepeatancesShortcuts.length; i++) {
                                checkRingtoneRepeatances[i] = true;
                                selectedRingtoneRepeatances.add(ringtoneRepeatancesShortcuts[i]);
                            }
                        } else {
                            selectedRingtoneRepeatances.clear();
                            for (int i = 0; i < ringtoneRepeatancesShortcuts.length; i++) {
                                checkRingtoneRepeatances[i] = false;
                            }
                        }
                        textview_repeat.setText(Utility.getAlarmRepetitionShortcuts(selectedRingtoneRepeatances));
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }


    /**
     * Layout components for saving an alarm
     * - calls saveAlarm() once save button is pressed
     */
    public void addSaveButton() {
        fbutton_save = (FloatingActionButton) findViewById(R.id.saveChangesButton);
        fbutton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAlarm();
            }
        });
    }


    /**
     * saveAlarm
     * - creates a new alarm object and adds it to the list
     * - sends single or multiple pendingIntents
     * - starts main activity
     */
    public void saveAlarm() {

        int pickedHour = hourPicker.getValue();
        int pickedMinute = minutePicker.getValue();
        //generate random id for Alarm
        final int alarmId = new Random().nextInt();
        Log.d("DEBUGLOG: CC:", "Using ID: " + alarmId);

        long timeToTriggerAlert = Utility.getNextAlarmTime(pickedHour, pickedMinute);

        //play ringtone at desired alarmtime using pending intent
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmNotifierIntent = new Intent(ControlActivity.this, AlarmNotifier.class);
        alarmNotifierIntent.putExtra("alarm activated", true);
        alarmNotifierIntent.putExtra("alarmLabel", textview_label.getText());
        alarmNotifierIntent.putExtra("alarmTimeHours", hourPicker.getValue());
        alarmNotifierIntent.putExtra("alarmTimeMinutes", minutePicker.getValue());
        alarmNotifierIntent.putExtra("checkRingtoneRepeatances", checkRingtoneRepeatances);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ControlActivity.this, alarmId, alarmNotifierIntent, 0);
        // if alert not set to repeat on any given weekday send pendingIntent once otherwise sent pendingIntent daily
        if (selectedRingtoneRepeatances.isEmpty()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeToTriggerAlert, pendingIntent);
            Log.d("DEBUGLOG: CC:", "Alarm NOT set to repeat -> send pendingIntent once");
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToTriggerAlert, AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("DEBUGLOG: CC:", "Alarm SET to repeat -> send pendingIntent daily");
        }
        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(timeToTriggerAlert, pendingIntent);
        alarmManager.setAlarmClock(info, pendingIntent);

        //add alarm to list (save all properties including pendingIntent)
        alarmService.add(new Alarm(true, false, textview_label.getText().toString(), checkRingtoneRepeatances, Utility.getAlertCalendar(pickedHour, pickedMinute), alarmId));

        //write changed alarm list to file (added alarm)
        if (alarmService != null) {
            alarmService.write(ControlActivity.this, "Alarms.txt");
        }

        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivityIntent);
    }


}
