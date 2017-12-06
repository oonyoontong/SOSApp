package com.example.helplah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.VolleyQueueSingleton;
import utils.hashFunction;
import volleys.LoginWebRequest;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText username;
    private EditText userpass;
    private TextView register;
    private CheckBox cbRememberme;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean checkFlag;
    private final String TAG = "login_activity";
    public static final String KEY = "intent_userid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declare widget references
        loginButton = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.user_id);
        userpass = (EditText) findViewById(R.id.user_pass);
        register = (TextView) findViewById(R.id.register_click);
        cbRememberme = (CheckBox) findViewById(R.id.checkbox);

        checkFlag = cbRememberme.isChecked();
        cbRememberme.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                checkFlag = isCheck;
            }
        });

        sharedPreferences = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
                                //check whether checkbox is ticked
                                if(checkFlag){
                                    Log.d("sharedPreferences", "saving username and pass");
                                    editor.putString("username", userName);
                                    editor.putString("password", hashFunction.hash(userPass));
                                    editor.apply();
                                }
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra(KEY, items.optInt(0));

                                LoginActivity.this.startActivity(intent);
                                finish();
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

        String user_name = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if(!user_name.equals("") && !password.equals("")){
            Response.Listener<String> responseListener = new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonRequest = new JSONObject(response);
                        JSONArray items = jsonRequest.getJSONArray("items");
                        if(items.optInt(0) != 0){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "username/password has changed. Please login again.", Toast.LENGTH_LONG).show();
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

            LoginWebRequest loginWebRequest = new LoginWebRequest(user_name, password, responseListener, responseErrorListener);
            VolleyQueueSingleton.getInstance(LoginActivity.this.getApplicationContext()).addToRequestQueue(loginWebRequest);
        }
    }
}
