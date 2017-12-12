package com.example.helplah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences p = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        TextView profile_name = (TextView)findViewById(R.id.profile_displayName);
        profile_name.setText(p.getString("displayName",""));
        TextView reputation = (TextView)findViewById(R.id.profile_reputation);
        Random rand = new Random();
        reputation.setText(Integer.toString(532+(int)(Math.random()*6000)) + " points");
    }


    public static void open(Context context) {
        context.startActivity(new Intent(context,ProfileActivity.class));
    }

}
