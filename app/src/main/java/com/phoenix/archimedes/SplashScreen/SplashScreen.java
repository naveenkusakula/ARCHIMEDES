package com.phoenix.archimedes.SplashScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.phoenix.archimedes.GoogleAuthentication.GPlusFragment;
import com.phoenix.archimedes.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        Thread myThread = new Thread(){
            @Override
            public void run(){

                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), GPlusFragment.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}