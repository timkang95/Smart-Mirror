package com.example.timothykang.smartmirror;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
    }

    /** Called when the user clicks the + button */
    public void addEditAlarms(View view) {
        Intent intent = new Intent(this, addEditList.class);
        startActivity(intent);
    }
}