package com.example.snowflake.aufgabe_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * RingtonePlayingService
 * -control the MediaPlayer
 * -makes function calls to play ringtones if conditions are met
 */

public class RingtonePlayingService extends Service {

    private boolean isRunning = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("DEBUGLOG: RPS:", "onStartCommand Called -> Play Ringtone");
        boolean alarmActivated = intent.getBooleanExtra("alarm activated", false);
        mediaAdapterHandler(alarmActivated);
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "onDestroy called", Toast.LENGTH_SHORT).show();
    }


    /**
     * Handles Media Adapter; Plays / stops alarm if the right conditions are met.
     *
     * @param alarmActivated false -> alarm not activated or not matching selected weekday
     *                       true -> alarm activated (& matching selected weekday if ringtone is repeated)
     */
    public void mediaAdapterHandler(boolean alarmActivated) {

        if (!this.isRunning && alarmActivated) {
            Log.d("DEBUGLOG: RPS:", "Ringtone not playing and AlarmActivated");
            MediaAdapter.startPlayingSound(this);
            isRunning = true;
        } else if (this.isRunning && !alarmActivated) {
            Log.d("DEBUGLOG: RPS:", "Ringtone playing and Alarm deactivated");
            MediaAdapter.stopPlayingSound();
            isRunning = false;
        } else if (this.isRunning && alarmActivated) {
            Log.d("DEBUGLOG: RPS:", "Ringtone playing and Alarm activated");
            MediaAdapter.stopPlayingSound();
            MediaAdapter.startPlayingSound(this);
        } else if (!this.isRunning && !alarmActivated) {
            Log.d("DEBUGLOG: RPS:", "Ringtone not playing and alarm deactivated");
        } else {
            Log.d("DEBUGLOG: RPS:", "Unexcepted Condition");
        }
    }

}