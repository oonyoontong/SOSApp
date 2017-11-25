package com.example.helplah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText username;
    private EditText userpass;
    private TextView register;

    private final String TAG = "login_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declare widget references
        loginButton = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.user_id);
        userpass = (EditText) findViewById(R.id.user_pass);
        register = (TextView) findViewById(R.id.register_click);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = username.getText().toString();
                final String userPass = userpass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRequest = new JSONObject(response);
                            JSONArray items = jsonRequest.getJSONArray("items");
                            if(items.optInt(0) != 0){
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Wrong username or password. Try Again.", Toast.LENGTH_SHORT).show();
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

                LoginWebRequest loginWebRequest = new LoginWebRequest(userName, userPass, responseListener, responseErrorListener);
                VolleyQueueSingleton.getInstance(LoginActivity.this.getApplicationContext()).addToRequestQueue(loginWebRequest);

            }
        });
    }


}
