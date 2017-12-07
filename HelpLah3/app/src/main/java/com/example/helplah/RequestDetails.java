package com.example.helplah;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.StringRequest;
import com.example.helplah.MainActivity;

import utils.VolleyQueueSingleton;

public class RequestDetails extends AppCompatActivity {
    private TextView requestTitle;
    private TextView requestDesc;
    private TextView requestLocation;
    private TextView requestBestby;
    private TextView requesterIDtext;
    private Button acceptRqBtn;

    private Integer requesterID;
    private String title;
    private String description;
    private String location;
    private String bestby;
    private Integer rqid;
    private Integer userID;

    SharedPreferences sharedPreferences;

    private static final String ACCEPT_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/accept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        requestTitle = (TextView) findViewById(R.id.requestTitleDetail);
        requestDesc = (TextView) findViewById(R.id.requestDescriptionDetail);
        requestLocation = (TextView) findViewById(R.id.requestLocationDetail);
        requestBestby = (TextView) findViewById(R.id.requestBestByDetail);


        requesterID = getIntent().getIntExtra(ListRequestAdapter.ViewHolder.REQUESTER_ID, 0);
        title = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_TITLE);
        description = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_DESCRIPTION);
        location = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_LOCATION);
        bestby = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_BESTBY);
        rqid = getIntent().getIntExtra(ListRequestAdapter.ViewHolder.REQUEST_ID, 0);
        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userId", 0);

        requestTitle.setText(title);
        requestDesc.setText(description);
        requestLocation.setText(location);
        requestBestby.setText(bestby);

        acceptRqBtn = (Button) findViewById(R.id.accept_request_btn);
        acceptRqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(RequestDetails.this).create();
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
                        StringRequest acceptRequest = new StringRequest(Request.Method.POST, ACCEPT_REQUEST_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject responseJson = new JSONObject(response);
                                    JSONArray items = responseJson.getJSONArray("items");
                                    if (items.optBoolean(0)) {
                                        Intent intent = new Intent(RequestDetails.this, MainActivity.class);
                                        RequestDetails.this.startActivity(intent);
                                    } else {
                                        Toast.makeText(RequestDetails.this, "The Job has been taken by someone else", Toast.LENGTH_LONG).show();
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
                        }) {
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<String, String>();
                                Log.d("acceptRequest", String.valueOf(rqid));
                                Log.d("acceptRequest", String.valueOf(userID));

                                params.put("acceptId", String.valueOf(userID));
                                params.put("rqId", String.valueOf(rqid));

                                return params;
                            }
                        };

                        VolleyQueueSingleton.getInstance(RequestDetails.this.getApplicationContext()).addToRequestQueue(acceptRequest);
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }
}
