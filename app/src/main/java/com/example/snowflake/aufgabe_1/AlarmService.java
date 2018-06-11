package com.example.snowflake.aufgabe_1;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * AlertService
 * - Holds list of alerts.
 * - Manages alerts
 * - Handles persistent storage
 */

public class AlarmService implements Serializable {

    private ArrayList<Alarm> alarmlist;

    public AlarmService() {

        alarmlist = new ArrayList<>();

    }

    public ArrayList<Alarm> getAlarmlist() {
        return alarmlist;
    }


    /**
     * Append alarm to alerts (ArrayList)
     *
     * @param alarm
     */
    public void add(Alarm alarm) {
        alarmlist.add(alarm);
    }


    /**
     * Removes alarm at selected index in the list
     *
     * @param index position in list
     */
    public void remove(int index) {
        try {
            alarmlist.remove(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.d("DEBUGLOG: REMOVE", "Index not in numberspace: " + e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("DEBUGLOG: REMOVE", "Unexpected exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Removes all alert items in list
     */
    public void clear() {
        alarmlist.clear();
    }

    /**
     * Reads from specified file (filename) in specified context (directory)
     *
     * @param context  context specifies directory
     * @param filename custom filename (default = "Alarms.txt")
     */
    public void read(Context context, String filename) {

        FileInputStream fin = null;
        ObjectInputStream ois = null;

        try {
            fin = new FileInputStream(context.getFilesDir() + "/" + filename);
            ois = new ObjectInputStream(fin);

            while (true) {
                alarmlist.add((Alarm) ois.readObject());
                Log.d("DEBUGLOG: READ", "Sucessfully read alert object");
            }

        } catch (EOFException e) {
            Log.d("DEBUGLOG: READ", "End of file reached: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("DEBUGLOG: READ", "IOException thrown: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("DEBUGLOG: READ", "Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        } finally {

            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Writes to specified file (filename) in specified context (directory)
     *
     * @param context  context specifies directory
     * @param filename custom filename (default = "Alarms.txt")
     */
    public void write(Context context, String filename) {

        FileOutputStream fos = null;
        ObjectOutputStream os = null;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            for (Alarm alarm : alarmlist) {
                os.writeObject(alarm);
            }
            Log.d("DEBUGLOG: WRITE", "Sucessfully written to file: " + context.getFilesDir() + "/" + filename);
        } catch (IOException e) {
            Log.d("DEBUGLOG: WRITE", "IO-Exception occurred while writing: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("DEBUGLOG: WRITE", "Unexpected error occurred while writing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}