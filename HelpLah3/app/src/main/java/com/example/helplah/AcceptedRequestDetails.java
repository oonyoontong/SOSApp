package com.example.helplah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import adapter.ListRequestAdapter2;
import utils.VolleyQueueSingleton;

public class AcceptedRequestDetails extends AppCompatActivity {
    private TextView requestTitle;
    private TextView requestDesc;
    private TextView requestLocation;
    private TextView requestBestby;
    private TextView requesterIDtext;
    private Button finishTaskBtn;

    private Integer requesterID;
    private String title;
    private String description;
    private String location;
    private String bestby;
    private Integer rqid;
    private Integer userID;
    private String username;

    SharedPreferences sharedPreferences;
    private static final String DELETE_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/delete?RQID=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_request_details);

        requestTitle = (TextView) findViewById(R.id.acceptedReqTitleDetail);
        requestDesc = (TextView) findViewById(R.id.acceptedReqDescriptionDetail);
        requestLocation = (TextView) findViewById(R.id.acceptedReqLocationDetail);
        requestBestby = (TextView) findViewById(R.id.acceptedReqBestByDetail);
        requesterIDtext = (TextView) findViewById(R.id.acceptedReqRequesterDetail);

        requesterID = getIntent().getIntExtra(ListRequestAdapter2.ViewHolder2.REQUESTER_ID2, 0);
        title = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder2.REQUEST_TITLE2);
        description = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder2.REQUEST_DESCRIPTION2);
        location = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder2.REQUEST_LOCATION2);
        bestby = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder2.REQUEST_BESTBY2);
        rqid = getIntent().getIntExtra(ListRequestAdapter2.ViewHolder2.REQUEST_ID2, 0);
        username = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder2.REQUEST_USERNAME2);

        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 0);

        requestTitle.setText(title);
        requestDesc.setText(description);
        requestLocation.setText(location);
        requestBestby.setText(bestby);
        requesterIDtext.setText(username);

        finishTaskBtn = (Button) findViewById(R.id.task_finish_btn);
        finishTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(AcceptedRequestDetails.this).create();
                alertDialog.setTitle("Task Done Confirmation");
                alertDialog.setMessage("Confirm done ar? Don't pangseh");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ya chop chop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = DELETE_REQUEST_URL + rqid;
                        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("items");
                                    if(jsonArray.optBoolean(0)){
                                        Log.d("AcceptRequestDetails", "task deleted successfully");
                                        Toast.makeText(AcceptedRequestDetails.this, "Macam tu la. Help more people!", Toast.LENGTH_LONG).show();
                                    }else{
                                        Log.d("AcceptRequestDetails", "task not deleted successfully");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("requestdetailsActivity", "Error: " + error.getMessage());
                            }
                        });

                        VolleyQueueSingleton.getInstance(AcceptedRequestDetails.this.getApplicationContext()).addToRequestQueue(deleteRequest);
                        dialogInterface.dismiss();

                        Intent taskFinished = new Intent(AcceptedRequestDetails.this, MainActivity.class);
                        taskFinished.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(taskFinished);
                    }
                });
                alertDialog.show();

            }
        });
    }
}
