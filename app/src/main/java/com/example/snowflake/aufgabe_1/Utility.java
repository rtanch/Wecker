package com.example.snowflake.aufgabe_1;

import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utility class holding static methods to avoid code duplication.
 * Provides methods for multiple other classes.
 */

public class Utility {

    private final static int weekdays = 7;
    private final static long twentyFourHoursInMS = 86400000;

    /**
     * @param selectedRingtoneRepeatances
     * @return
     */
    public static String getAlarmRepetitionShortcuts(ArrayList<String> selectedRingtoneRepeatances) {

        String getAlarmRepetitionShortcuts = "";
        if (selectedRingtoneRepeatances.size() == 0) {
            getAlarmRepetitionShortcuts = "Never";
        } else if (selectedRingtoneRepeatances.size() == weekdays) {
            getAlarmRepetitionShortcuts = "Daily";
        } else {
            for (int i = 0; i < selectedRingtoneRepeatances.size(); i++) {
                getAlarmRepetitionShortcuts = getAlarmRepetitionShortcuts + selectedRingtoneRepeatances.get(i);
                if (i != selectedRingtoneRepeatances.size() - 1) {
                    getAlarmRepetitionShortcuts = getAlarmRepetitionShortcuts + ", ";
                }
            }
        }
        return getAlarmRepetitionShortcuts;
    }

    /**
     * Returns formatted Time String from Calendar object
     *
     * @param calendar
     * @return formatted Time String (00:00)
     */
    public static String formatTime(Calendar calendar) {

        String strHour, strMinute;

        int hour = calendar.getTime().getHours();
        int minute = calendar.getTime().getMinutes();

        if (hour < 10) {
            strHour = "0" + hour;
        } else {
            strHour = String.valueOf(hour);
        }

        if (minute < 10) {
            strMinute = "0" + minute;
        } else {
            strMinute = String.valueOf(minute);
        }

        return strHour + " : " + strMinute;

    }


    /**
     * Get the time of day the alarm will be played
     *
     * @param pickedHour
     * @param pickedMinute
     * @return
     */
    public static Calendar getAlertCalendar(int pickedHour, int pickedMinute) {

        Calendar setTime = Calendar.getInstance();
        setTime.set(
                setTime.get(Calendar.YEAR),
                setTime.get(Calendar.MONTH),
                setTime.get(Calendar.DAY_OF_MONTH),
                pickedHour,
                pickedMinute,
                0
        );
        return setTime;
    }

    /**
     * Returns the time until alarm triggers.
     * This value may exceed 24 hours as it includes alarms that
     * Includes trigger times for alarms that are supposed to be played the next day and that exceed 24 hours.
     *
     * @param pickedHour   selected hour value
     * @param pickedMinute selected minute value
     * @return time in milliseconds until alarm triggers again
     */
    public static long getNextAlarmTime(int pickedHour, int pickedMinute) {

        long timeToTriggerAlarm = 0;

        Calendar currentTime = Calendar.getInstance();
        Calendar setTime = getAlertCalendar(pickedHour, pickedMinute);

        // Alarm was set earlier than current time => Alarm was set for tomorrow
        if (setTime.getTimeInMillis() < currentTime.getTimeInMillis()) {
            Log.d("DEBUGLOG: UTILITY:", "Alarm set for TOMORROW.");
            timeToTriggerAlarm = setTime.getTimeInMillis() + twentyFourHoursInMS;
        }
        // Alarm was set later than current time => Alarm was set for today
        else {
            Log.d("DEBUGLOG: UTILITY:", "Alarm set for TODAY.");
            timeToTriggerAlarm = setTime.getTimeInMillis();
        }
        return timeToTriggerAlarm;
    }


    /**
     * Checks whether a ringtone is or isn't set to repeat
     *
     * @param selectedWeekdays array containing seven values boolean values each representing a weekday (sunday, monday, tuesday, wednesday, thursday, friday)
     * @return true -> ringtone SET to repeat
     * false -> ringtone NOT set to repeat
     */
    public static boolean isRepeat(boolean[] selectedWeekdays) {
        boolean repeat = false;
        for (int i = 0; i < selectedWeekdays.length; i++) {
            if (selectedWeekdays[i]) {
                repeat = true;
            }
        }
        return repeat;
    }

}
