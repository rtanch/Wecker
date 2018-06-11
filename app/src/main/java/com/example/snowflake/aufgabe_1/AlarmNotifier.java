package com.example.snowflake.aufgabe_1;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * AlarmNotifier
 * -handles ringtones
 * -handles date/ringtone validation -> handles ringtone repeatances
 * -handles notification
 */

public class AlarmNotifier extends BroadcastReceiver {

    private boolean alarmActivated;
    private boolean[] selectedWeekdays;

    private String alarmLabel;
    private int alarmTimeHours, alarmTimeMinutes;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("DEBUGLOG NOTIFIER:", "Receive triggered");

        alarmActivated = intent.getBooleanExtra("alarm activated", false);
        //create an intent to the ringtone service
        Intent serviceIntent;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        selectedWeekdays = intent.getBooleanArrayExtra("checkRingtoneRepeatances");
        alarmLabel = intent.getStringExtra("alarmLabel");
        alarmTimeHours = intent.getIntExtra("alarmTimeHours", 0);
        alarmTimeMinutes = intent.getIntExtra("alarmTimeMinutes", 0);

        if (!isRepeat(selectedWeekdays)) {
            serviceIntent = new Intent(context, RingtonePlayingService.class);
            serviceIntent.putExtra("alarm activated", alarmActivated);
            context.startService(serviceIntent);
            if (alarmActivated) {
                launchActiveAlertActivity(context);
            }
            Log.d("DEBUGLOG NOTIFIER:", "Never repeat this Ringtone");
        } else {
            for (int i = 0; i < selectedWeekdays.length; i++) {

                if (selectedWeekdays[i] && i == day - 1) {
                    if (alarmActivated) {
                        serviceIntent = new Intent(context, RingtonePlayingService.class);
                        serviceIntent.putExtra("alarm activated", alarmActivated);
                        context.startService(serviceIntent);
                        launchActiveAlertActivity(context);
                    } else {
                        serviceIntent = new Intent(context, RingtonePlayingService.class);
                        serviceIntent.putExtra("alarm activated", false);
                        context.startService(serviceIntent);
                    }
                    Log.d("DEBUGLOG NOTIFIER:", "Weekday " + day + " selected as weakly repetition");
                } else {
                    Log.d("DEBUGLOG NOTIFIER:", "Weekday " + day + " not flagged as repeat or wrong day");
                }
            }
            Log.d("DEBUGLOG NOTIFIER:", "Finished validation of ringtone repeatances.");
        }
    }

    /**
     * Checks whether a ringtone is or isn't set to repeat
     *
     * @param selectedWeekdays array containing seven values boolean values each representing a weekday (sunday, monday, tuesday, wednesday, thursday, friday)
     * @return true -> ringtone SET to repeat
     * false -> ringtone NOT set to repeat
     */
    public boolean isRepeat(boolean[] selectedWeekdays) {
        boolean repeat = false;
        for (int i = 0; i < selectedWeekdays.length; i++) {
            if (selectedWeekdays[i]) {
                repeat = true;
            }
        }
        return repeat;
    }


    /**
     * Start ActiveAlertActivity to stop the currently playing ringtone.
     * Pass attributes identifying the alarm to the user on to ActiveAlertActivity as extras
     *
     * @param context
     */
    public void launchActiveAlertActivity(Context context) {
        Intent i = new Intent();
        i.putExtra("alarmLabel", alarmLabel);
        i.putExtra("alarmTimeHours", alarmTimeHours);
        i.putExtra("alarmTimeMinutes", alarmTimeMinutes);
        i.setClassName("com.example.snowflake.aufgabe_1", "com.example.snowflake.aufgabe_1.ActiveAlarmActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
