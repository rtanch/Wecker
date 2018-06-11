package com.example.snowflake.aufgabe_1;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * ActiveAlarmActivity
 * -show while alert is active
 * -deactivate alert on click (does not cancel PendingIntent)
 */

public class ActiveAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activealarm);

        TextView textView_time = (TextView) findViewById(R.id.textView_time_AAA);
        textView_time.setText(getIntent().getIntExtra("alarmTimeHours", 0) + " : " + getIntent().getIntExtra("alarmTimeMinutes", 0));

        TextView textView_title = (TextView) findViewById(R.id.textView_title_AAA);
        textView_title.setText(getIntent().getStringExtra("alarmLabel"));

        findViewById(R.id.activeAlarmClickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaAdapter.stopPlayingSound();
                finish();
            }
        });

    }
}