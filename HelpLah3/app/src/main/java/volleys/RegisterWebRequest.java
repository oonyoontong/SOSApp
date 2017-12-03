package volleys;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import utils.hashFunction;

public class RegisterWebRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/user/new";
    private Map<String, String> params;

    public RegisterWebRequest(String displayname, String username, String password, Response.Listener<String> listener, Response.ErrorListener errorlistener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorlistener);
        params = new HashMap<>();
        params.put("displayName", displayname);
        params.put("username", username);
        params.put("passHash", hashFunction.hash(password));
        params.put("token", "0");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
