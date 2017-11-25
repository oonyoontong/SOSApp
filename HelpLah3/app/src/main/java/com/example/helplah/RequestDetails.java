package com.example.helplah;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RequestDetails extends AppCompatActivity {
    private TextView requestTitle;
    private TextView requestDesc;
    private TextView requesterIDtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        requestTitle = (TextView) findViewById(R.id.requestTiTleDetail);
        requestDesc = (TextView) findViewById(R.id.requestDescriptionDetail);
        requesterIDtext = (TextView) findViewById(R.id.requestRequesterID);

        String requesterID = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUESTER_ID);
        requesterIDtext.setText(requesterID);
    }
}
