package volleys;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import utils.hashFunction;

public class LoginWebRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/user/authenticate";
    private Map<String, String> params;

    public LoginWebRequest(String username, String password, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("username", username);
        String passhash = hashFunction.hash(password);
        params.put("passHash", passhash);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
