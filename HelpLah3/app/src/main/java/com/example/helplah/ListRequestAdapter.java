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
        holder.textViewHead.setText(listRequest.getHead());
        holder.textViewDisc.setText(listRequest.getDescription());
    }

    @Override
    public int getItemCount() {
        return listRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewHead;
        public TextView textViewDisc;
        private final Context context;
        public static final String REQUESTER_ID = "REQUESTER_ID";

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
            String requesterID = listRequests.get(position).getRequesterID();
            intent.putExtra(REQUESTER_ID, requesterID);
            context.startActivity(intent);

        }
    }

}
