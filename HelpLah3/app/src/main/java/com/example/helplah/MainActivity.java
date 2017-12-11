package com.example.helplah;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.SectionPageAdapter;
import utils.VolleyQueueSingleton;

public class MainActivity extends AppCompatActivity {

    private final String GET_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/all";
    private final String BASE_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/";

    private static final String TAG = "MainActivity";

    public static final String KEY = "sendhelp_userid";

    private SectionPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Integer userID;
    private String userDisplayName;
    private String userToken;

    //keys for bundle
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String BESTBY_KEY = "bestby";
    public static final String REQUESTERID_KEY = "requesterID";

    private SectionPageAdapter adapter;

    private JSONObject queriedJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: Starting.");

        mViewPager = (ViewPager) findViewById(R.id.container);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //createTabIcons();


        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userID = sharedPreferences.getInt("userId", 0);
        if(userID == 0){
            Log.d(TAG, "user id got problem");
        }

        //get user display name
        String getUsername = BASE_URL + "user/get?id=" + userID;
        JsonObjectRequest getDisplaynameRequest = new JsonObjectRequest(Request.Method.GET, getUsername, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userDisplayName = response.getString("username");
                    Log.d(TAG, userDisplayName);
                    String token = response.getString("token");
                    Log.d(TAG, token);
                    //userToken = Integer.parseInt(token);
                    //update shared preferences with displayname and token
                    editor.putString("displayName", userDisplayName);
                    editor.putString("userToken", userToken);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error" + error.getMessage());
            }
        });
        VolleyQueueSingleton.getInstance(this).addToRequestQueue(getDisplaynameRequest);


        //get requests from server and parse data and send to tab fragments
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_REQUEST_URL, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                setupViewPager(mViewPager, response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        VolleyQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        //TODO: refresh button for recyclerview
//        if(!sharedPreferences.getString("displayName", "").equals("")){
//            Toast.makeText(this, "HELP LAH " + sharedPreferences.getString("displayName", "") + "!!", Toast.LENGTH_LONG).show();
//        }

    }

    private void createTabIcons(){
        //set tab icons
        LinearLayout tab1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tabs, null);
        ImageView tab1icon = (ImageView) findViewById(R.id.tabicon);
        tab1icon.setImageResource(R.drawable.ic_people_white_24dp);
        tabLayout.getTabAt(0).setCustomView(tab1icon);

        LinearLayout tab2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tabs, null);
        ImageView tab2icon = (ImageView) findViewById(R.id.tabicon);
        tab2icon.setImageResource(R.drawable.ic_tab3_24dp);
        tabLayout.getTabAt(1).setCustomView(tab2icon);

        LinearLayout tab3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tabs, null);
        ImageView tab3icon = (ImageView) findViewById(R.id.tabicon);
        tab3icon.setImageResource(R.drawable.ic_tab3_24dp);
        tabLayout.getTabAt(2).setCustomView(tab3icon);
    }

    private void queryData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_REQUEST_URL, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                queriedJsonObject = response;
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        VolleyQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void setupViewPager(ViewPager viewPager, JSONObject data){

        JSONArray dataArray = null;
        List<JSONObject> requestDataJsonList = new ArrayList<>();
        try {
            dataArray = data.getJSONArray("items");
            for(int i = 0; i < dataArray.length(); i ++){
                JSONObject requestData = dataArray.getJSONObject(i);
                requestDataJsonList.add(requestData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Maintab1Fragment maintab1Fragment = new Maintab1Fragment();
        Maintab2Fragment maintab2Fragment = new Maintab2Fragment();
        Maintab3Fragment maintab3Fragment = new Maintab3Fragment();

        List<Object> tab1RequestList = new ArrayList<>();
        List<ListRequest> tab2RequestList = new ArrayList<>();
        List<ListRequest> tab3RequestList = new ArrayList<>();

        for(JSONObject requestData : requestDataJsonList){
            try {
                String title = requestData.getString("title");
                String description = requestData.getString("description");
                String location = requestData.getString("location");
                String bestby = requestData.getString("bestBy");
                Integer requesterId = requestData.getInt("requesterId");
                Integer rqid = requestData.getInt("rqId");
                //String displayName;

                //String url = BASE_URL + "user/get?id=" + rqid;

                if(requestData.getString("priority").equals("LOW") && requestData.getInt("requesterId") != userID){
//                    Log.d("requestdetails", title);
//                    Log.d("requestdetails", description);
//                    Log.d("requestdetails", location);
//                    Log.d("requestdetails", bestby);
//                    Log.d("requestdetails", String.valueOf(requesterId));
//                    Log.d("requestdetails", String.valueOf(rqid));
                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid, this);
                    tab1RequestList.add(listRequest);
                }else if(requestData.getString("priority").equals("HIGH") && requestData.getInt("requesterId") != userID) {

                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid, this);
                    tab2RequestList.add(listRequest);
                }
//                }else{
//                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
//                    tab3RequestList.add(listRequest);
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(maintab1Fragment, "Help");
        adapter.addFragment(maintab2Fragment, "HelpLAH!");
        adapter.addFragment(maintab3Fragment, "HelpMe");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        if(tab1RequestList.size() != 0){
            Log.d(TAG, "got tab1requestlist");
            Maintab1Fragment tab1 = (Maintab1Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(viewPager.getId(), 0));
            tab1.updateRecyclerView(tab1RequestList);
        }
        if(tab2RequestList.size() != 0){
            Log.d(TAG, "got tab2requestlist");
            Maintab2Fragment tab2 = (Maintab2Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(viewPager.getId(), 1));
            tab2.updateRecyclerView(tab2RequestList);
        }

        //get user requests
        String getAllMyReqUrl = BASE_URL + "request/myrequests?id=" + userID;

        JsonObjectRequest getMyRequests = new JsonObjectRequest(Request.Method.GET, getAllMyReqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray dataArray = null;
                List<JSONObject> requestDataJsonList = new ArrayList<>();
                try {
                    dataArray = response.getJSONArray("items");
                    for(int i = 0; i < dataArray.length(); i ++){
                        JSONObject requestData = dataArray.getJSONObject(i);
                        requestDataJsonList.add(requestData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Object> tab3List = new ArrayList<>();

                for(JSONObject myReq : requestDataJsonList) {
                    try {
                        String title = myReq.getString("title");
                        String description = myReq.getString("description");
                        String location = myReq.getString("location");
                        String bestby = myReq.getString("bestBy");
                        Integer requesterId = myReq.getInt("requesterId");
                        Integer rqid = myReq.getInt("rqId");

                        MRequest listRequest = new MRequest(title, description, location, bestby, requesterId, rqid, MainActivity.this.getApplicationContext());
                        tab3List.add(listRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(tab3List.size() != 0){
                    Log.d(TAG, "got my reqlist");
                    Maintab3Fragment tab3 = (Maintab3Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(), 2));
                    tab3.updateRecyclerView(tab3List);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error.getMessage());
            }
        });

        VolleyQueueSingleton.getInstance(this).addToRequestQueue(getMyRequests);

        //check for requests accepted by user
        //TODO: change UI view of accepted requests - finish task btn

        String acceptedReqUrl = BASE_URL + "request/accepted?id=" + userID;

        JsonObjectRequest acceptedRequestRequest = new JsonObjectRequest(Request.Method.GET, acceptedReqUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray dataArray = null;
                List<JSONObject> requestDataJsonList = new ArrayList<>();
                try {
                    dataArray = response.getJSONArray("items");
                    for(int i = 0; i < dataArray.length(); i ++){
                        JSONObject requestData = dataArray.getJSONObject(i);
                        requestDataJsonList.add(requestData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Object> tab1List = new ArrayList<>();

                for(JSONObject acceptedReq : requestDataJsonList) {
                    try {
                        String title = acceptedReq.getString("title");
                        String description = acceptedReq.getString("description");
                        String location = acceptedReq.getString("location");
                        String bestby = acceptedReq.getString("bestBy");
                        Integer requesterId = acceptedReq.getInt("requesterId");
                        Integer rqid = acceptedReq.getInt("rqId");

                        AcceptedRequestList listRequest = new AcceptedRequestList(title, description, location, bestby, requesterId, rqid, MainActivity.this.getApplicationContext());
                        tab1List.add(listRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(tab1List.size() != 0){
                    Log.d(TAG, "got accepted reqlist");
                    Maintab1Fragment tab1 = (Maintab1Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(), 0));
                    tab1.addListRequest(tab1List);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error.getMessage());
            }
        });

        VolleyQueueSingleton.getInstance(this).addToRequestQueue(acceptedRequestRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void refreshRecyclerViews(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_REQUEST_URL, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                setupViewPager(mViewPager, response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        VolleyQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private String getFragmentTag(int viewPagerId, int fragmentPosition){
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.logout:
                sharedPreferences.edit().clear().apply();
                Intent logout = new Intent(this, LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                finish();
                return true;
            case R.id.send_request:
                Intent srintent = new Intent(this, SendRequestActivity.class);
                //srintent.putExtra(KEY, userID);
                this.startActivity(srintent);
                return true;
            case R.id.messages:
                return true;
            case R.id.refresh:
                refreshRecyclerViews();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
