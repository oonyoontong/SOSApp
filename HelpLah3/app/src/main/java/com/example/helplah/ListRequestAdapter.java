package com.example.helplah;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListRequestAdapter extends RecyclerView.Adapter<ListRequestAdapter.ViewHolder> {

    private List<ListRequest> listRequests;
    private Context context;

    public ListRequestAdapter(List<ListRequest> listRequests, Context context) {
        this.listRequests = listRequests;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_request, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListRequest listRequest = listRequests.get(position);
        holder.textViewHead.setText(listRequest.getTitle());
        holder.textViewDisc.setText(listRequest.getDescription());
    }

    @Override
    public int getItemCount() {
        return listRequests.size();
    }

    public void setListRequests(List<ListRequest> listRequests) {
        this.listRequests = listRequests;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewHead;
        public TextView textViewDisc;
        private final Context context;

        public static final String REQUESTER_ID = "REQUESTER_ID";
        public static final String REQUEST_TITLE = "REQUEST_TITLE";
        public static final String REQUEST_DESCRIPTION = "REQUEST_DESCRIPTION";
        public static final String REQUEST_LOCATION = "REQUEST_LOCATION";
        public static final String REQUEST_BESTBY = "REQUEST_BESTBY";
        public static final String REQUEST_ID = "REQUEST_ID";

        public ViewHolder(View itemView) {
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDisc = (TextView) itemView.findViewById(R.id.textviewDesc);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RequestDetails.class);
            int position = getAdapterPosition();

            String title = listRequests.get(position).getTitle();
            String description = listRequests.get(position).getDescription();
            String location = listRequests.get(position).getLocation();
            String bestby = listRequests.get(position).getBestby();
            Integer requesterID = listRequests.get(position).getRequesterID();
            Integer rqid = listRequests.get(position).getRqid();

            intent.putExtra(REQUEST_TITLE, title);
            intent.putExtra(REQUEST_DESCRIPTION, description);
            intent.putExtra(REQUEST_LOCATION, location);
            intent.putExtra(REQUEST_BESTBY, bestby);
            intent.putExtra(REQUESTER_ID, requesterID);
            intent.putExtra(REQUEST_ID, rqid);
            context.startActivity(intent);

        }
    }

}
