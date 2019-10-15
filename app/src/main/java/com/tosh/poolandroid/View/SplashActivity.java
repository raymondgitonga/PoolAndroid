package com.tosh.poolandroid.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import com.tosh.poolandroid.R;
import com.tosh.poolandroid.View.AppIntro.AppIntroActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private static final String TAG = "SplashActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.splash_progress);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String value=(mSharedPreference.getString("email", "default"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                runProgressBar();

                if(value.equals("default")){
                    startApp();
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }).start();
    }

    private void runProgressBar(){
        for(int progress =0; progress<100; progress+=10){
            try {
                Thread.sleep(300);
                progressBar.setProgress(progress);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void startApp(){
        Intent intent = new Intent(SplashActivity.this, AppIntroActivity.class);
        startActivity(intent);
    }
}
