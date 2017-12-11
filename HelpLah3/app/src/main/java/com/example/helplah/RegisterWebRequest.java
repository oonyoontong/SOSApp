package com.example.helplah;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegisterWebRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "https://endpoint-dot-infosys-group2-4.appspot.com/_ah/api/sos/v1/user/new";
    private Map<String, String> params;

    public RegisterWebRequest(String displayname, String username, String password, Response.Listener<String> listener, Response.ErrorListener errorlistener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorlistener);
        params = new HashMap<>();
        params.put("displayName", displayname);
        params.put("username", username);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        params.put("passHash", sb.toString());
        params.put("token", "0");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
