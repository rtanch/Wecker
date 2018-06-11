package com.example.snowflake.aufgabe_1;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;


/**
 * Created by Daniel on 13.10.2017.
 * class AlarmAdapter with getView methode
 * The List holds Alarm elements, connect the xml elements textview,switch and layout
 * starting a Alarm dialog with clicklistener to remove an Alarm from the List
 * set the switch with a listener, proof the switch status and activate the Alarm
 * pending intent sets the app icon
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public Context context;
    private List<Alarm> ListAlarmEntry;
    private AlarmService alarmService;
    private ArrayList<String> selectedRingtoneRepeatances = new ArrayList<>();
    private String[] ringtoneRepeatancesShortcuts;
    boolean[] checkRingtoneRepeatances;
    MainActivity mainActivity;


    private LayoutInflater inflater;
    private TextView list_item_alarm_textViewTitel;
    private TextView list_item_alarm_textViewUhr;
    private TextView repeat_textview;
    private RelativeLayout clickListEntry;
    private Switch switch1;


    public AlarmAdapter(Context context, int resource, ArrayList<Alarm> objects, AlarmService alarmService) {
        super(context, resource, objects);

        this.alarmService = alarmService;
        this.context = context;
        this.ListAlarmEntry = objects;
        this.mainActivity = (MainActivity) context;

    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        final Alarm alarm = ListAlarmEntry.get(position);

        //get the inflater and inflate the XML layout for each item
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        final View view = inflater.inflate(R.layout.list_item_alarm, null);

        list_item_alarm_textViewTitel = (TextView) view.findViewById(R.id.list_item_alarm_textViewTitel);
        list_item_alarm_textViewUhr = (TextView) view.findViewById(R.id.list_item_alarm_textViewUhr);
        repeat_textview = (TextView) view.findViewById(R.id.repeat_textview);
        clickListEntry = (RelativeLayout) view.findViewById(R.id.clickListEntry);
        switch1 = (Switch) view.findViewById(R.id.switch1);


        if (alarm.isActive()) {
            Log.d("ALARMADAPTER", "isActive true");
            switch1.setChecked(true);
        } else {
            Log.d("ALARMADAPTER", "isActive false");
            switch1.setChecked(false);
        }
        Log.d("ALARMADAPTER", "isActive done");


        list_item_alarm_textViewTitel.setText(alarm.getLabel());
        list_item_alarm_textViewUhr.setText(Utility.formatTime(alarm.getWakeUpTime()));


        clickListEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Möchten Sie diesen Alarm löschen ?");


                builder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {

                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent intent2 = new Intent(view.getContext(), AlarmNotifier.class);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), alarmService.getAlarmlist().get(getPosition(alarm)).getId(), intent2, 0);
                                    alarmManager.cancel(pendingIntent);

                                    mainActivity.removeEntry(getPosition(alarm));


                                } catch (Exception e) {
                                    Log.d("DEBUGLOG AAdapt", "Problem casting context to MainActivity" + e.getMessage());
                                    e.printStackTrace();
                                }
                                dialog.cancel();

                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                            }
                        });

                builder.show();


            }
        });


        selectedRingtoneRepeatances.clear();
        ringtoneRepeatancesShortcuts = context.getResources().getStringArray(R.array.ringtone_repeatance_shortcuts);
        checkRingtoneRepeatances = alarm.getRepeatance();
        for (int i = 0; i < checkRingtoneRepeatances.length; i++) {
            if (checkRingtoneRepeatances[i] == true) {
                selectedRingtoneRepeatances.add(ringtoneRepeatancesShortcuts[i]);
            }
        }
        repeat_textview.setText(Utility.getAlarmRepetitionShortcuts(selectedRingtoneRepeatances));


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    alarm.setActive(isChecked);
                    alarmService.write(context, "Alarms.txt");
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent(view.getContext(), AlarmNotifier.class);
                    intent1.putExtra("alarm activated", true);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), alarmService.getAlarmlist().get(getPosition(alarm)).getId(), intent1, 0);

                    long timeToTriggerAlarm = Utility.getNextAlarmTime(alarm.getWakeUpTime().getTime().getHours(), alarm.getWakeUpTime().getTime().getMinutes());

                    if (selectedRingtoneRepeatances.isEmpty()) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToTriggerAlarm, pendingIntent);
                        Log.d("DEBUGLOG: CC:", "Alarm NOT set to repeat -> send pendingIntent once");
                    } else {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToTriggerAlarm, AlarmManager.INTERVAL_DAY, pendingIntent);
                        Log.d("DEBUGLOG: CC:", "Alarm SET to repeat -> send pendingIntent daily");
                    }
                    AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(timeToTriggerAlarm, pendingIntent);
                    alarmManager.setAlarmClock(info, pendingIntent);

                } else {
                    alarm.setActive(isChecked);
                    alarmService.write(context, "Alarms.txt");
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent2 = new Intent(view.getContext(), AlarmNotifier.class);


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), alarmService.getAlarmlist().get(getPosition(alarm)).getId(), intent2, 0);
                    alarmManager.cancel(pendingIntent);
                }


            }
        });


        return view;

    }

}