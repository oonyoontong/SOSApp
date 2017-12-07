package com.example.helplah;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.VolleyQueueSingleton;

public class Maintab1Fragment extends Fragment{

    private static final String TAG = "Tab1Fragment";
    private final String GET_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/all";

    private RecyclerView recyclerView;
    private ListRequestAdapter adapter;
    private List<ListRequest> listRequests;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("Tab1", context.toString());
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maintab1_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTab1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Maintab1Fragment.this.getActivity()));

        if(listRequests != null){
            Log.d(TAG, "listrequest not null");
            adapter = new ListRequestAdapter(listRequests, Maintab1Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }


        //determine type of layout within recyclerView


        //Get requests from server
//        JsonObjectRequest jsonObjectRequestTab1 = new JsonObjectRequest(Request.Method.GET, GET_REQUEST_URL, null, new Response.Listener<JSONObject>(){
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
//
//        VolleyQueueSingleton.getInstance(Maintab1Fragment.this.getContext()).addToRequestQueue(jsonObjectRequestTab1);
        //Get data




    }

    public void updateRecyclerView(List<ListRequest> requestDatas){
        listRequests = requestDatas;
//        for(ListRequest listRequest : listRequests){
//            Log.d("Tab1", listRequest.getHead() + " " + listRequest.getDescription() + " " + listRequest.getRequesterID());
//            Log.d("Tab1", Maintab1Fragment.this.context.toString());
//        }

        //adapter = new ListRequestAdapter(listRequests, Maintab1Fragment.this.getActivity());
        if(adapter != null){
            Log.d(TAG, "adapter not null");
            adapter.setListRequests(listRequests);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }else{
            Log.d(TAG, "adapter null");
            Log.d(TAG, Maintab1Fragment.this.getActivity().toString());
            adapter = new ListRequestAdapter(listRequests, Maintab1Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }
    }
}
