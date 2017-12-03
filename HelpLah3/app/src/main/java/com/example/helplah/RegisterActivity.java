package com.example.helplah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.VolleyQueueSingleton;
import volleys.RegisterWebRequest;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText registerDisplayName;
    private EditText registerUsername;
    private EditText registerUserpass;

    private final String TAG = "register_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //declare widget references
        registerButton = (Button)findViewById(R.id.register_button);
        registerDisplayName = (EditText)findViewById(R.id.register_displayname);
        registerUsername = (EditText)findViewById(R.id.register_id);
        registerUserpass = (EditText)findViewById(R.id.register_pass);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String displayName = registerDisplayName.getText().toString();
                final String username = registerUsername.getText().toString();
                final String userpass = registerUserpass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray userID = jsonObject.getJSONArray("items");
                            if(userID.optInt(0) != 0){
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                };
                RegisterWebRequest registerWebRequest = new RegisterWebRequest(displayName, username, userpass, responseListener, responseErrorListener);
                VolleyQueueSingleton.getInstance(RegisterActivity.this.getApplicationContext()).addToRequestQueue(registerWebRequest);
            }
        });
    }
}
