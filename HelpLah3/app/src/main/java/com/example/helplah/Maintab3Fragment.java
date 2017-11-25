package com.example.helplah;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Maintab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListRequest> listRequests;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maintab3_fragment, container, false);

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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTab3);
        recyclerView.setHasFixedSize(true);
        //determine type of layout within recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(Maintab3Fragment.this.getActivity()));

        //create list of request that will be put into a list adapter
        listRequests = new ArrayList<>();
        for(int i = 0; i <= 10; i++){
            ListRequest listRequest = new ListRequest(
                    "heading" + (i+2),
                    "Lorem ipsum dummy text", "1000" + i);
            listRequests.add(listRequest);
        }

        adapter = new ListRequestAdapter(listRequests, Maintab3Fragment.this.getActivity());

        recyclerView.setAdapter(adapter);

    }
}
