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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import adapter.ListRequestAdapter2;
import utils.VolleyQueueSingleton;

public class MRequestDetail extends AppCompatActivity {
    private TextView requestTitle;
    private TextView requestDesc;
    private TextView requestLocation;
    private TextView requestBestby;
    private TextView requesterIDtext;
    private Button deleteRqBtn;

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
        setContentView(R.layout.content_m_request_details);

        requestTitle = (TextView) findViewById(R.id.mrequestTitleDetail);
        requestDesc = (TextView) findViewById(R.id.mrequestDescriptionDetail);
        requestLocation = (TextView) findViewById(R.id.mrequestLocationDetail);
        requestBestby = (TextView) findViewById(R.id.mrequestBestByDetail);
        requesterIDtext = (TextView) findViewById(R.id.mrequestRequesterDetail);


        requesterID = getIntent().getIntExtra(ListRequestAdapter2.ViewHolder3.REQUESTER_ID3, 0);
        title = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder3.REQUEST_TITLE3);
        description = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder3.REQUEST_DESCRIPTION3);
        location = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder3.REQUEST_LOCATION3);
        bestby = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder3.REQUEST_BESTBY3);
        rqid = getIntent().getIntExtra(ListRequestAdapter2.ViewHolder3.REQUEST_ID3, 0);
        username = getIntent().getStringExtra(ListRequestAdapter2.ViewHolder3.REQUEST_USERNAME3);

        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 0);

        requestTitle.setText(title);
        requestDesc.setText(description);
        requestLocation.setText(location);
        requestBestby.setText(bestby);
        requesterIDtext.setText(username);

        deleteRqBtn = (Button) findViewById(R.id.delete_request_btn);
        deleteRqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MRequestDetail.this).create();
                alertDialog.setTitle("Help Confirmation");
                alertDialog.setMessage("Confirm accept ah? Don't back out mcb");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK, NP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        JSONObject acceptParams = new JSONObject();
//                        try {
//                            acceptParams.put("acceptId", mainActivity.userID)
//                                    .put("rqId", rqid);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        String url = DELETE_REQUEST_URL + rqid;
                        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("items");
                                    if(jsonArray.optBoolean(0)){
                                        Log.d("AcceptRequestDetails", "task deleted successfully");
                                        Toast.makeText(MRequestDetail.this, "Macam tu la. Help more people!", Toast.LENGTH_LONG).show();
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


                        VolleyQueueSingleton.getInstance(MRequestDetail.this.getApplicationContext()).addToRequestQueue(deleteRequest);
                        dialogInterface.dismiss();

                        Intent taskAccepted = new Intent(MRequestDetail.this, MainActivity.class);
                        finish();
                        startActivity(taskAccepted);
                    }
                });
                alertDialog.show();
            }
        });

    }
}
