package com.example.helplah;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

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
                sendHelpRequest(title, location, description, bestby, urgent, view);
//                Intent intent = new Intent(view.getContext(), MainActivity.class);
//                view.getContext().startActivity(intent);
            }
        });
    }

    public void sendHelpRequest(String title, String location, String description, String bestby, Boolean urgent, View v){
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("location", location);
        data.putString("description", description);
        data.putString("bestby", bestby);
        data.putBoolean("priority", urgent);

        Intent intent = new Intent(v.getContext(), Maintab3Fragment.class);

//        Fragment myrequests = new Maintab3Fragment();
//        myrequests.setArguments(data);
//
//        fragmentManager.beginTransaction().addToBackStack(null);
//        fragmentManager.beginTransaction().replace(R.id.maintab3_fragment, myrequests).commit();

        //TODO: broadcast request to other users
    }

}
