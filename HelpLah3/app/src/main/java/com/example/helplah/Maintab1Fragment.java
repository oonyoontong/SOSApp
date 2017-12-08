package com.example.helplah;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import adapter.ListRequestAdapter;
import adapter.ListRequestAdapter2;

public class Maintab1Fragment extends Fragment{

    private static final String TAG = "Tab1Fragment";
    private final String GET_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/all";

    private RecyclerView recyclerView;
    private ListRequestAdapter2 adapter;
    private List<Object> listRequests;

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
//        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerviewTab1_2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTab1);

//        recyclerView2.setHasFixedSize(true);
//        recyclerView2.setLayoutManager(new LinearLayoutManager(Maintab1Fragment.this.getActivity()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Maintab1Fragment.this.getActivity()));

        if(listRequests != null){
            Log.d(TAG, "listrequest not null");
            adapter = new ListRequestAdapter2(listRequests, Maintab1Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }

    }

    public void addListRequest(List<Object> requests) {
        if(listRequests == null){
            listRequests = requests;
        }else{
            requests.addAll(listRequests);
        }
        updateRecyclerView(requests);

    }

    public void updateRecyclerView(List<Object> requestDatas){
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
            adapter = new ListRequestAdapter2(listRequests, Maintab1Fragment.this.getActivity());
            recyclerView.setAdapter(adapter);
        }
    }
}
