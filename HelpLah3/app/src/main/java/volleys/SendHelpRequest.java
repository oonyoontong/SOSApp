package volleys;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SendHelpRequest extends StringRequest{
    private static final String HELP_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/request/new";
    private Map<String, String> params;

public SendHelpRequest(String title, String location, String descripiton, String bestby, Boolean priority, Integer requesterID, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST, HELP_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("title", title);
        params.put("location", location);
        params.put("description", descripiton);
        params.put("besBy", bestby);
        if(priority){
            //high priority
            params.put("priority", "HIGH");
        }else{
            params.put("priority", "LOW");
        }
        params.put("requesterID",String.valueOf(requesterID));

    }

    public Map<String, String> getParams() {
        return params;
    }
}
