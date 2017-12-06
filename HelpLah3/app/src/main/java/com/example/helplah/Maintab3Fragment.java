package com.example.helplah;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.VolleyQueueSingleton;

public class Maintab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";
    private final String GET_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/all";

    private RecyclerView recyclerView;
    //TODO: create separate adapter for tab3 fragment
    private ListRequestAdapter adapter;
    private List<ListRequest> listRequests;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maintab3_fragment, container, false);

        return view;
    }

//        Bundle data = this.getArguments();
//        if (data != null){
//            String title = data.getString("title");
//            String location = data.getString("location");
//            String description = data.getString("description");
//            String bestby = data.getString("bestby");
//            Boolean urgent = data.getBoolean("priority");
//
//            ListRequest listRequest = new ListRequest();
//            listRequest.setTitle(title);
//            listRequest.setLocation(location);
//            listRequest.setDescription(description);
//            listRequest.setBestby(bestby);
//            listRequest.setUrgent(urgent);
//
//            listRequests.add(listRequest);
//            adapter.notifyDataSetChanged();
//        }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTab3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Maintab3Fragment.this.getActivity()));

        if(listRequests != null){
            adapter = new ListRequestAdapter(listRequests, Maintab3Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }
    }


//        JsonObjectRequest jsonObjectRequestTab3 = new JsonObjectRequest(Request.Method.GET, GET_REQUEST_URL, null, new Response.Listener<JSONObject>(){
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//            }
//        });
//        VolleyQueueSingleton.getInstance(Maintab3Fragment.this.getContext()).addToRequestQueue(jsonObjectRequestTab3);

        //create list of request that will be put into a list adapter


    public void updateRecyclerView(List<ListRequest> requestDatas){
        listRequests = requestDatas;
        if(adapter != null){
            Log.d(TAG, "adapter not null");
            adapter.setListRequests(listRequests);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }else{
            Log.d(TAG, "adapter null");
            adapter = new ListRequestAdapter(listRequests, Maintab3Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }
    }
}
