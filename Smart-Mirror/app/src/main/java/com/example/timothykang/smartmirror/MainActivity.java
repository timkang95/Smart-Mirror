package com.example.timothykang.smartmirror;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    long timeElapsed;
    //to make our alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    int choose_song_sound;
    final private String TAG = "CLIENT INFO";
    final private String ORG_ID = "qi0pmu";
    final private String DEVICE_TYPE = "Android";
    final private String DEVICE_ID = "123456789123";
    final private String EVENT_ID = "SmartMirrorAlarm-iotf-service";
    final private String FORMAT_STRING = "JSON";
    final private String CLIENT_IDENTIFIER = "d:qi0pmu:Android:123123123123";
    final private String USERNAME = "use-token-auth";
    final private String PASSWORD = "xAOKaIJoC?5?xI(Wtv";
    final private String EVENT_TOPIC = "iot-2/evt/" + EVENT_ID + "/fmt/" + FORMAT_STRING;
    final private String URI = "tcp://qi0pmu";
    private MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        this.context = this;

        // initialize our alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //initialize our text update box
        update_text = (TextView) findViewById(R.id.update_text);

        // create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        // create an intent to the Alarm Receiver class
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);


        // create the spinner in the main UI
        Spinner spinner = (Spinner) findViewById(R.id.richard_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.song_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set an onclick listener to the onItemSelected method
        spinner.setOnItemSelectedListener(this);


        // initialize start button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        // create an onClick listener to start the alarm
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                // get the int values of the hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // convert 24-hour time to 12-hour time
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }

                // method that changes the update text Textbox
                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                // put in extra string into my_intent
                // tells the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm on");

                // put in an extra int into my_intent
                // tells the clock that you want a certain value from the drop-down menu/spinner
                my_intent.putExtra("song_choice", choose_song_sound);
                Log.e("The song id is" , String.valueOf(choose_song_sound));

                // create a pending intent that delays the intent
                // until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);



            }



        });



        // initialize the stop button
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        // create an onClick listener to stop the alarm or undo an alarm set

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //update end time to calculate how long it took for the user to turn off alarm
                elapsedTime.endtime = System.currentTimeMillis();
                Log.e("end time(milliseconds) " , String.valueOf(elapsedTime.endtime));
                timeElapsed = elapsedTime.endtime - elapsedTime.starttime;
                // method that changes the update text Textbox
                set_alarm_text("Alarm off!");
                Log.e("it took the user " , String.valueOf(timeElapsed/ 1000));

                JSONObject contObj = new JSONObject();
                JSONObject jsonObj = new JSONObject();
                try {
                    contObj.put("time", timeElapsed);
                    jsonObj.put("d", contObj);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                byte[] encodedPayload = new byte[0];

                try {
                    encodedPayload = jsonObj.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    Log.e(TAG, "Puhlishing"+jsonObj);
                    client.publish("time/new", encodedPayload, 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                alarm_manager.cancel(pending_intent);

                // put extra string into my_intent
                // tells the clock that you pressed the "alarm off" button
                my_intent.putExtra("extra", "alarm off");
                // also put an extra int into the alarm off section
                // to prevent crashes in a Null Pointer Exception
                my_intent.putExtra("song_choice", choose_song_sound);


                // stop the ringtone
                sendBroadcast(my_intent);

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.timothykang.myweatherapp");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }

            }
        });

        try {
            setupMQTTConnection();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.e("DONE CREATING STUFF", "...yaaaay");
    }

    private boolean isMqttConnected() {
        Log.d(TAG, ".isMqttConnected() entered");
        boolean connected = false;
        try {
            if ((client != null) && (client.isConnected())) {
                connected = true;
            }
        } catch (Exception e) {
            // swallowing the exception as it means the client is not connected
        }
        Log.d(TAG, ".isMqttConnected() - returning " + connected);
        return connected;
    }

    private void setupMQTTConnection() throws MqttException {
        Log.d(TAG, "MQTT Setup entered...");

        String clientId = "d:" + ORG_ID + ":" + DEVICE_TYPE + ":" + DEVICE_ID;
        String connectionURI = "tcp://" + ORG_ID + ".messaging.internetofthings.ibmcloud.com:1883";

        if(!isMqttConnected()) {

        }
        if (!isMqttConnected()) {
            if (client != null) {
                client.unregisterResources();
                client = null;
            }
            client = new MqttAndroidClient(context, connectionURI, clientId);

            String username = USERNAME;
            char[] password = PASSWORD.toCharArray();;

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password);

            Log.d(TAG, "Connecting to server: " + connectionURI);
            try {
                Log.e("DEV", "URI: " + connectionURI + " | clientID: " + clientId + " | user: " + username + " | pass: " + PASSWORD);
                client.connect(options, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.e(TAG, "Connect Successful!!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "Connect Failed...");
                    }
                });
            } catch (MqttException e) {
                Log.e(TAG, "Exception caught while attempting to connect to server", e.getCause());
                throw e;
            }
        }
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        // outputting whatever id the user has selected
        //Toast.makeText(parent.getContext(), "the spinner item is "
        //        + id, Toast.LENGTH_SHORT).show();
        choose_song_sound = (int) id;


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }

}
