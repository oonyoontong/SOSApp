package com.example.helplah;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import utils.VolleyQueueSingleton;
import volleys.SendHelpRequest;

public class SendRequestActivity extends AppCompatActivity {

    private Switch prioritySwitch;
    private EditText ETtitle;
    private EditText ETlocaiton;
    private EditText ETdescription;
    private static EditText ETbestby;
    private Button sendButton;
    private ImageButton pickDateTimeBtn;

    private String title;
    private String location;
    private String description;
    private String bestby;
    private Boolean urgent;

    public ListRequest listRequest;
    private static FragmentManager fragmentManager;

    private Integer requesterID;

    private static final String TAG = "sendrequestActivity";
    private static final String HELP_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/new";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        prioritySwitch = (Switch) findViewById(R.id.SwitchUrgency);
        ETtitle = (EditText) findViewById(R.id.EditTextTitle);
        ETlocaiton = (EditText)findViewById(R.id.EditTextLocation);
        ETdescription = (EditText)findViewById(R.id.EditTextDesc);
        ETbestby = (EditText) findViewById(R.id.EditTextBestby);
        sendButton = (Button) findViewById(R.id.ButtonSendRequest);
        pickDateTimeBtn = (ImageButton) findViewById(R.id.calendar);

        //get userID
//        Intent intent = getIntent();
//        requesterID = intent.getIntExtra(MainActivity.KEY, 0);
        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        requesterID = sharedPreferences.getInt("userId", 0);
        Log.d(TAG, String.valueOf(requesterID));

        //control the display of switch text
        prioritySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    prioritySwitch.setText("Urgent");
                }else{
                    prioritySwitch.setText("Not Urgent");
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = ETtitle.getText().toString();
                location = ETlocaiton.getText().toString();
                description = ETdescription.getText().toString();
                bestby = ETbestby.getText().toString();
                urgent = prioritySwitch.isChecked();
                sendHelpRequest(title, location, description, bestby, urgent, requesterID, view);
//                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                view.getContext().startActivity(intent);
            }
        });

        pickDateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newDialogFragment = new DatePickFragment();
                newDialogFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

    }

    public void sendHelpRequest(String title, String location, String description, String bestby, Boolean urgent, Integer userID, View v){

        String priority;
        if(urgent){
            priority = "HIGH";
        }else{
            priority = "LOW";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date bestByDate = null;

        try {
            bestByDate = sdf.parse(bestby);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(TAG, sdf.format(bestByDate));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bestByDate);

        JSONObject bestByJson = null;

        try {
            bestByJson = new JSONObject()
                    .put("year", calendar.get(Calendar.YEAR))
                    .put("month", calendar.get(Calendar.MONTH))
                    .put("day", calendar.get(Calendar.DAY_OF_MONTH))
                    .put("hours", calendar.get(Calendar.HOUR_OF_DAY))
                    .put("minutes", calendar.get(Calendar.MINUTE));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonHelpRequest = new JSONObject();
        try {
            jsonHelpRequest
                    .put("title", title)
                    .put("description", description)
                    .put("location", location)
                    .put("priority", priority)
                    .put("bestBy", bestByJson)
                    .put("requesterId", userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray items = responseJson.getJSONArray("items");
                    if (items.optBoolean(0)) {
                        Toast.makeText(SendRequestActivity.this, "Request sent successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SendRequestActivity.this, "Send request fail", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        };
//        SendHelpRequest sendHelpRequest = new SendHelpRequest(title, location, description, bestby, urgent, userID, responseListener, responseErrorListener);
        Log.d(TAG, String.valueOf(userID));
        SendHelpRequest sendHelpRequest = new SendHelpRequest(title, location, description, bestby, urgent, userID, responseListener, responseErrorListener);
        VolleyQueueSingleton.getInstance(SendRequestActivity.this.getApplicationContext()).addToRequestQueue(sendHelpRequest);

        Toast.makeText(SendRequestActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SendRequestActivity.this.getApplicationContext(), MainActivity.class);
        SendRequestActivity.this.startActivity(intent);
    }

    private static int setYear = 2017;
    private static int setMonth = 1;
    private static int setDay = 1;
    private static int setHour = 12;
    private static int setMinute = 0;

    public static class DatePickFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, setYear, setMonth, setDay);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            DialogFragment newDialogFragment = TimePickFragment.newInstance(i, i1, i2);
            newDialogFragment.show(getFragmentManager(), "TimePicker");
        }
    }

    public static class TimePickFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        int[] date;

        public static TimePickFragment newInstance(int year, int month, int day){
            TimePickFragment newTimePickFragment = new TimePickFragment();
            Bundle args = new Bundle();
            int[] date = {year, month, day};
            args.putIntArray("date", date);
            newTimePickFragment.setArguments(args);
            return newTimePickFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            date = getArguments().getIntArray("date");
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, setHour, setMinute, true);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            setYear = date[0];
            setMonth = date[1];
            setDay = date[2];
            setHour = i;
            setMinute = i1;

            Calendar calendar = new GregorianCalendar(setYear, setMonth, setDay, setHour, setMinute);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ETbestby.setText(sdf.format(calendar.getTime()));
        }
    }

}
