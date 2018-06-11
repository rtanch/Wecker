package com.example.snowflake.aufgabe_1;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;

/**
 * Static MediaPlayer
 * - plays / stops ringtones without having to create a new object
 * - sets ringtone by passing R id (e.g. R.raw.trump)
 * - checks whether ringtone is or isn't playing
 */

public class MediaAdapter {

    private static MediaPlayer mp = new MediaPlayer();
    private static Uri ringtoneRPath = Settings.System.DEFAULT_RINGTONE_URI;


    /**
     * Plays (resets, prepares, plays) ringtone (if MediaPlayer initialized) and sets data source
     * of ringtone (Ringtone URI)
     *
     * @param context context in which method was called
     */
    public static void startPlayingSound(Context context) {
        // context.getSharedPreferences("sd",12).getString("","sd");
        try {
            if (mp != null) {
                mp.reset();
                mp.setDataSource(context, ringtoneRPath);
                mp.prepare();
                mp.setLooping(true);
                mp.start();
                Log.d("DEBUGLOG: MA:", "Start Playing Ringtone");
            } else {
                Log.d("DEBUGLOG: MA:", "Start Playing Ringtone -> not initialized CallInitMediaPlayer");
            }
        } catch (IOException e) {
            Log.d("DEBUGLOG: MA:", "Playing Sonud -> unexpected Exception");
        } catch (Exception e) {
            Log.d("DEBUGLOG: MA:", "Playing Sound -> unexpected Exception");
        }
    }


    /**
     * Stops and resets (if initialized) the MediaPlayer mp
     */
    public static void stopPlayingSound() {
        if (mp != null) {
            Log.d("DEBUGLOG: MA:", "Stop Playing Ringtone");
            mp.stop();
            mp.reset();
        } else {
            Log.d("DEBUGLOG: MA:", "Stop Playing Ringtone -> not initialized CallInitMediaPlayer");
        }
    }


    /**
     * MP must not be accessible outside of this class
     * -> Specific getter of the state of MP
     *
     * @return true -> Ringtone is currently playing
     * false -> Ringtone is not playing
     */
    public static boolean isPlaying() {

        if (mp.isPlaying()) {
            return true;
        }
        return false;
    }


    /**
     * Changes Ringtone that is being played by the MediaPlayer mp
     *
     * @param ringtoneTitle
     *                      title of ringtone without suffix, only lower case, without prior /
     */
    public static void setRingtone(String ringtoneTitle){
        Log.d("DEBUGLOG: MA:", "title: " + ringtoneTitle);
        ringtoneRPath = Uri.parse("android.resource://com.example.snowflake.aufgabe_1/raw/" + ringtoneTitle);
    }

}