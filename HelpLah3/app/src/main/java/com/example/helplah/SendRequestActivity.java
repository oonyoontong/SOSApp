package com.example.helplah;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONObject;

import utils.VolleyQueueSingleton;
import volleys.SendHelpRequest;

public class SendRequestActivity extends AppCompatActivity {

    private Switch prioritySwitch;
    private EditText ETtitle;
    private EditText ETlocaiton;
    private EditText ETdescription;
    private EditText ETbestby;
    private Button sendButton;

    private String title;
    private String location;
    private String description;
    private String bestby;
    private Boolean urgent;

    public ListRequest listRequest;
    private static FragmentManager fragmentManager;

    private Integer requesterID;

    private static final String TAG = "sendrequestActivity";


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

        //get userID
        Intent intent = getIntent();
        requesterID = intent.getIntExtra(MainActivity.KEY, 0);

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
    }

    public void sendHelpRequest(String title, String location, String description, String bestby, Boolean urgent, Integer userID, View v){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

            }
        };

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        };
        SendHelpRequest sendHelpRequest = new SendHelpRequest(title, location, description, bestby, urgent, userID, responseListener, responseErrorListener);

        VolleyQueueSingleton.getInstance(SendRequestActivity.this.getApplicationContext()).addToRequestQueue(sendHelpRequest);

        Toast.makeText(SendRequestActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SendRequestActivity.this, MainActivity.class);
        SendRequestActivity.this.startActivity(intent);

    }

}
