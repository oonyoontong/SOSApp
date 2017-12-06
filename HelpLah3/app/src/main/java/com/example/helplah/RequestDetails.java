package com.example.helplah;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RequestDetails extends AppCompatActivity {
    private TextView requestTitle;
    private TextView requestDesc;
    private TextView requestLocation;
    private TextView requestBestby;
    private TextView requesterIDtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        requestTitle = (TextView) findViewById(R.id.requestTitleDetail);
        requestDesc = (TextView) findViewById(R.id.requestDescriptionDetail);
        requestLocation = (TextView) findViewById(R.id.requestLocationDetail);
        requestBestby = (TextView) findViewById(R.id.requestBestByDetail);


        Integer requesterID = getIntent().getIntExtra(ListRequestAdapter.ViewHolder.REQUESTER_ID, 0);
        String title = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_TITLE);
        String description = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_DESCRIPTION);
        String location = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_LOCATION);
        String bestby = getIntent().getStringExtra(ListRequestAdapter.ViewHolder.REQUEST_BESTBY);

        requestTitle.setText(title);
        requestDesc.setText(description);
        requestLocation.setText(location);
        requestBestby.setText(bestby);
    }
}
