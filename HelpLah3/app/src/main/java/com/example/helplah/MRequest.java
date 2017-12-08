package com.example.helplah;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import utils.VolleyQueueSingleton;

public class MRequest {
    private String title;
    private String description;
    private String location;
    private String bestby;
    private Boolean urgent;
    private Integer requesterID;
    private Integer rqid;
    private String displayName;
    //private Integer usertoken;
    private String usertoken;
    private Context context;

    private static final String GETNAME_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/";


    public MRequest(String title, String description, String location, String bestby, Integer requesterID, Integer rqid, Context context) {
        this.title = title;
        this.description = description;
        this.requesterID = requesterID;
        this.bestby = bestby;
        this.location = location;
        this.rqid = rqid;
        this.context = context;
//        this.displayName = displayName;
//        this.token = token;

        String url = GETNAME_URL + "user/get?id=" + requesterID;
        JsonObjectRequest getDisplaynameRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    displayName = response.getString("username");
                    Log.d("listitem", displayName);
                    usertoken = response.getString("token");
                    Log.d("listitem", usertoken);
                    //usertoken = Integer.parseInt(token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("listitem", "Error" + error.getMessage());
            }
        });
        VolleyQueueSingleton.getInstance(context).addToRequestQueue(getDisplaynameRequest);


    }

    public MRequest(){
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBestby(String bestby) {
        this.bestby = bestby;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getBestby() {
        return bestby;
    }

    public Integer getRequesterID() {
        return requesterID;
    }

    public Integer getRqid() {
        return rqid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public Context getContext() {
        return context;
    }
}

