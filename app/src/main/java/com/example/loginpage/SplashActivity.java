package com.example.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);
                if(isFirstTime){
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent =new Intent(SplashActivity.this,OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                else{
                   Intent intent =new Intent(SplashActivity.this,DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }

           }
        },5000);


    }
}