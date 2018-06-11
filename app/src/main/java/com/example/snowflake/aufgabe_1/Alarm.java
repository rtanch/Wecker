package com.example.snowflake.aufgabe_1;

import java.io.Serializable;
import java.util.Calendar;

/**
 * class Alarm, holds getter & setter methods for the Alarm.
 */

public class Alarm implements Serializable {

    private boolean active, selected;
    private String label;
    private boolean[] repeatance;
    private Calendar wakeUpTime;
    private int id;

    //save random id in Alarm class and use it as id for pending intent
    public Alarm(boolean active, boolean selected, String label, boolean[] repeatance, Calendar wakeUpTime, int id) {

        this.active = active;
        this.selected = selected;
        this.label = label;
        this.repeatance = repeatance;
        this.wakeUpTime = wakeUpTime;
        this.id = id;
    }

    // Getter & Setter ->
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean[] getRepeatance() {
        return repeatance;
    }

    public void setRepeatance(boolean[] repeatance) {
        this.repeatance = repeatance;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Calendar getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(Calendar wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
