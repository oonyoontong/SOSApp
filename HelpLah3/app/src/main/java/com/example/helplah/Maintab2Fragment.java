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

public class Maintab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListRequest> listRequests;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maintab2_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewTab2);
        recyclerView.setHasFixedSize(true);
        //determine type of layout within recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(Maintab2Fragment.this.getActivity()));

        //create list of request that will be put into a list adapter
        listRequests = new ArrayList<>();
        for(int i = 0; i <= 10; i++){
            ListRequest listRequest = new ListRequest(
                    "heading" + (i),
                    "Lorem ipsum dummy text", "1000" + i);
            listRequests.add(listRequest);
        }

        adapter = new ListRequestAdapter(listRequests, Maintab2Fragment.this.getActivity());

        recyclerView.setAdapter(adapter);

    }
}
