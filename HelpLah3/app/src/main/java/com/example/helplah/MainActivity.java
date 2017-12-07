package com.example.helplah;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import utils.VolleyQueueSingleton;

public class MainActivity extends AppCompatActivity {

    private final String GET_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/all";

    private static final String TAG = "MainActivity";

    public static final String KEY = "sendhelp_userid";

    private SectionPageAdapter mSectionPageAdapter;

    private ViewPager mViewPager;

    SharedPreferences sharedPreferences;

    public Integer userID;

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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Log.d(TAG, "onCreate: Starting.");

//        boolean finish = getIntent().getBooleanExtra("finish", false);
//        if(finish){
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
//        }


        mViewPager = (ViewPager) findViewById(R.id.container);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        userID = sharedPreferences.getInt("userId", 0);
        if(userID == 0){
            Log.d(TAG, "user id got problem");
        }
        //receive intent from login page
//        Intent intent = getIntent();
//        userID = intent.getIntExtra(LoginActivity.KEY, 0);
        Log.d(TAG, String.valueOf(userID));

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

        List<ListRequest> tab1RequestList = new ArrayList<>();
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
                if(requestData.getString("priority").equals("LOW") && requestData.getInt("requesterId") != userID){
//                    Log.d("requestdetails", title);
//                    Log.d("requestdetails", description);
//                    Log.d("requestdetails", location);
//                    Log.d("requestdetails", bestby);
//                    Log.d("requestdetails", String.valueOf(requesterId));
//                    Log.d("requestdetails", String.valueOf(rqid));
                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                    tab1RequestList.add(listRequest);
                }else if(requestData.getString("priority").equals("HIGH") && requestData.getInt("requesterId") != userID){

                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                    tab2RequestList.add(listRequest);
                }else{
                    ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                    tab3RequestList.add(listRequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(maintab1Fragment, "Help");
        adapter.addFragment(maintab2Fragment, "HelpLAH!");
        adapter.addFragment(maintab3Fragment, "HelpMe");
        viewPager.setAdapter(adapter);

        if(tab1RequestList.size() != 0){
            maintab1Fragment.updateRecyclerView(tab1RequestList);
        }
        if(tab2RequestList.size() != 0){
            maintab2Fragment.updateRecyclerView(tab2RequestList);

        }
        if(tab3RequestList.size() != 0){
            maintab3Fragment.updateRecyclerView(tab3RequestList);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void refreshRecyclerViews(){
        queryData();
        if(queriedJsonObject != null){
            Log.d(TAG, "query succesful");
            JSONArray dataArray = null;
            List<JSONObject> requestDataJsonList = new ArrayList<>();
            try {
                dataArray = queriedJsonObject.getJSONArray("items");
                for(int i = 0; i < dataArray.length(); i ++){
                    JSONObject requestData = dataArray.getJSONObject(i);
                    requestDataJsonList.add(requestData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<ListRequest> tab1RequestList = new ArrayList<>();
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
                    if(requestData.getString("priority").equals("LOW") && requestData.getInt("requesterId") != userID){
//                        Log.d("requestdetails", title);
//                        Log.d("requestdetails", description);
//                        Log.d("requestdetails", location);
//                        Log.d("requestdetails", bestby);
//                        Log.d("requestdetails", String.valueOf(requesterId));
//                        Log.d("requestdetails", String.valueOf(rqid));
                        ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                        tab1RequestList.add(listRequest);
                    }else if(requestData.getString("priority").equals("HIGH") && requestData.getInt("requesterId") != userID){

                        ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                        tab2RequestList.add(listRequest);
                    }else{
                        ListRequest listRequest = new ListRequest(title, description, location, bestby, requesterId, rqid);
                        tab3RequestList.add(listRequest);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Maintab1Fragment maintab1Fragment = (Maintab1Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(), 0));
            Maintab2Fragment maintab2Fragment = (Maintab2Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(), 1));
            Maintab3Fragment maintab3Fragment = (Maintab3Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(mViewPager.getId(), 2));

            if(tab1RequestList.size() != 0){
                Log.d(TAG, "updating tab1");
                maintab1Fragment.updateRecyclerView(tab1RequestList);
            }
            if(tab2RequestList.size() != 0){
                Log.d(TAG, "updating tab2");
                maintab2Fragment.updateRecyclerView(tab2RequestList);

            }
            if(tab3RequestList.size() != 0){
                Log.d(TAG, "updating tab3");
                maintab3Fragment.updateRecyclerView(tab3RequestList);
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.commitNow();

            adapter.notifyDataSetChanged();

        }else{
            Log.d(TAG, "query unsuccesful");
        }
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
