package com.example.helplah;

/**
 * Created by Shameena on 10/12/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static int SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(Splash.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }


        }, SPLASH);
    }}


//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                //set the new Content of your activity
//                setContentView(R.layout.activity_login);
//            }
//        }.start();
//    }
//}

