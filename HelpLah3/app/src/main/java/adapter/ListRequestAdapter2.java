package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helplah.AcceptedRequestDetails;
import com.example.helplah.AcceptedRequestList;
import com.example.helplah.ListRequest;
import com.example.helplah.MRequest;
import com.example.helplah.MRequestDetail;
import com.example.helplah.R;
import com.example.helplah.RequestDetails;

import java.util.List;

public class ListRequestAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listRequests;
    private Context context;

    private final int LISTREQUEST = 0, ACCEPTED_LISTREQUEST = 1;
    private final int MYREQUEST = 2;

    public ListRequestAdapter2(List<Object> listRequests, Context context) {
        this.listRequests = listRequests;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case LISTREQUEST:
                View v1 = inflater.inflate(R.layout.list_request, parent, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case ACCEPTED_LISTREQUEST:
                View v2 = inflater.inflate(R.layout.accepted_list_request, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            case MYREQUEST:
                View v3 = inflater.inflate(R.layout.mrequest, parent, false);
                viewHolder = new ViewHolder3(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.list_request, parent, false);
                viewHolder = new ViewHolder1(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case LISTREQUEST:
                ViewHolder1 vh1 = (ViewHolder1)holder;
                ListRequest listRequest = (ListRequest) listRequests.get(position);
                vh1.textViewHead.setText(listRequest.getTitle());
                vh1.textViewDisc.setText(listRequest.getDescription());
                vh1.textViewDisplayName.setText(listRequest.getDisplayName());
                break;
            case ACCEPTED_LISTREQUEST:
                ViewHolder2 vh2 = (ViewHolder2)holder;
                AcceptedRequestList acceptedRequestList = (AcceptedRequestList) listRequests.get(position);
                vh2.textViewHead2.setText(acceptedRequestList.getTitle());
                vh2.textViewDisc2.setText(acceptedRequestList.getDescription());
                vh2.textViewDisplayName2.setText(acceptedRequestList.getDisplayName());
                break;
            case MYREQUEST:
                ViewHolder3 vh3 = (ViewHolder3) holder;
                Log.d("listadapter", vh3.getClass().toString());
                MRequest myRequest = (MRequest) listRequests.get(position);
                vh3.textViewHead3.setText(myRequest.getTitle());
                vh3.textViewDisc3.setText(myRequest.getDescription());
                vh3.textViewDisplayName3.setText(myRequest.getDisplayName());
                break;

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(listRequests.get(position) instanceof  ListRequest){
            return LISTREQUEST;
        }else if(listRequests.get(position) instanceof  AcceptedRequestList){
            return ACCEPTED_LISTREQUEST;
        }else if(listRequests.get(position) instanceof  MRequest){
            return MYREQUEST;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return listRequests.size();
    }

    public void setListRequests(List<Object> listRequests) {
        this.listRequests = listRequests;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewHead;
        public TextView textViewDisc;
        public TextView textViewDisplayName;
        private final Context context;

        public static final String REQUESTER_ID = "REQUESTER_ID";
        public static final String REQUEST_TITLE = "REQUEST_TITLE";
        public static final String REQUEST_DESCRIPTION = "REQUEST_DESCRIPTION";
        public static final String REQUEST_LOCATION = "REQUEST_LOCATION";
        public static final String REQUEST_BESTBY = "REQUEST_BESTBY";
        public static final String REQUEST_ID = "REQUEST_ID";
        public static final String REQUEST_USERNAME = "REQUEST_USERNAME";

        //standard viewholder for requests
        public ViewHolder1(View itemView) {
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDisc = (TextView) itemView.findViewById(R.id.textviewDesc);
            textViewDisplayName = (TextView) itemView.findViewById(R.id.cardview_displayname);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RequestDetails.class);
            int position = getAdapterPosition();

            ListRequest listRequest = (ListRequest) listRequests.get(position);

            String title = listRequest.getTitle();
            String description = listRequest.getDescription();
            String location = listRequest.getLocation();
            String bestby = listRequest.getBestby();
            Integer requesterID = listRequest.getRequesterID();
            Integer rqid = listRequest.getRqid();
            String displayName = listRequest.getDisplayName();

            intent.putExtra(REQUEST_TITLE, title);
            intent.putExtra(REQUEST_DESCRIPTION, description);
            intent.putExtra(REQUEST_LOCATION, location);
            intent.putExtra(REQUEST_BESTBY, bestby);
            intent.putExtra(REQUESTER_ID, requesterID);
            intent.putExtra(REQUEST_ID, rqid);
            intent.putExtra(REQUEST_USERNAME, displayName);
            context.startActivity(intent);

        }
    }

    //viewholder for accepted request
    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewHead2;
        public TextView textViewDisc2;
        public TextView textViewDisplayName2;
        private final Context context;

        public static final String REQUESTER_ID2 = "REQUESTER_ID2";
        public static final String REQUEST_TITLE2 = "REQUEST_TITLE2";
        public static final String REQUEST_DESCRIPTION2 = "REQUEST_DESCRIPTION2";
        public static final String REQUEST_LOCATION2 = "REQUEST_LOCATION2";
        public static final String REQUEST_BESTBY2 = "REQUEST_BESTBY2";
        public static final String REQUEST_ID2 = "REQUEST_ID2";
        public static final String REQUEST_USERNAME2 = "REQUEST_USERNAME2";

        public ViewHolder2(View itemView) {
            super(itemView);
            textViewHead2 = (TextView) itemView.findViewById(R.id.textViewHead2);
            textViewDisc2 = (TextView) itemView.findViewById(R.id.textviewDesc2);
            textViewDisplayName2 = (TextView) itemView.findViewById(R.id.cardview_displayname2);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, AcceptedRequestDetails.class);
            int position = getAdapterPosition();

            AcceptedRequestList acceptedRequestList = (AcceptedRequestList) listRequests.get(position);

            String title = acceptedRequestList.getTitle();
            String description = acceptedRequestList.getDescription();
            String location = acceptedRequestList.getLocation();
            String bestby = acceptedRequestList.getBestby();
            Integer requesterID = acceptedRequestList.getRequesterID();
            Integer rqid = acceptedRequestList.getRqid();
            String displayName = acceptedRequestList.getDisplayName();

            intent.putExtra(REQUEST_TITLE2, title);
            intent.putExtra(REQUEST_DESCRIPTION2, description);
            intent.putExtra(REQUEST_LOCATION2, location);
            intent.putExtra(REQUEST_BESTBY2, bestby);
            intent.putExtra(REQUESTER_ID2, requesterID);
            intent.putExtra(REQUEST_ID2, rqid);
            intent.putExtra(REQUEST_USERNAME2, displayName);
            context.startActivity(intent);
        }
    }

    //viewholder for myrequests
    public class ViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewHead3;
        public TextView textViewDisc3;
        public TextView textViewDisplayName3;
        private final Context context;

        public static final String REQUESTER_ID3 = "REQUESTER_ID3";
        public static final String REQUEST_TITLE3 = "REQUEST_TITLE3";
        public static final String REQUEST_DESCRIPTION3 = "REQUEST_DESCRIPTION3";
        public static final String REQUEST_LOCATION3 = "REQUEST_LOCATION3";
        public static final String REQUEST_BESTBY3 = "REQUEST_BESTBY3";
        public static final String REQUEST_ID3 = "REQUEST_ID3";
        public static final String REQUEST_USERNAME3 = "REQUEST_USERNAME3";

        public ViewHolder3(View itemView) {
            super(itemView);
            textViewHead3 = (TextView) itemView.findViewById(R.id.textViewHead3);
            textViewDisc3 = (TextView) itemView.findViewById(R.id.textviewDesc3);
            textViewDisplayName3 = (TextView) itemView.findViewById(R.id.cardview_displayname3);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MRequestDetail.class);
            int position = getAdapterPosition();

            MRequest mRequestList = (MRequest) listRequests.get(position);

            String title = mRequestList.getTitle();
            String description = mRequestList.getDescription();
            String location = mRequestList.getLocation();
            String bestby = mRequestList.getBestby();
            Integer requesterID = mRequestList.getRequesterID();
            Integer rqid = mRequestList.getRqid();
            String displayName = mRequestList.getDisplayName();

            intent.putExtra(REQUEST_TITLE3, title);
            intent.putExtra(REQUEST_DESCRIPTION3, description);
            intent.putExtra(REQUEST_LOCATION3, location);
            intent.putExtra(REQUEST_BESTBY3, bestby);
            intent.putExtra(REQUESTER_ID3, requesterID);
            intent.putExtra(REQUEST_ID3, rqid);
            intent.putExtra(REQUEST_USERNAME3, displayName);
            context.startActivity(intent);
        }
    }

}
