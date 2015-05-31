package com.xitij.adzap.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xitij.adzap.R;
import com.xitij.adzap.helpers.PrefUtils;


public class LauncherActivity extends ActionBarActivity {
    boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        new CountDownTimer(2500,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {

                if(PrefUtils.isLogin(LauncherActivity.this)){
                    Intent iHomeScreen = new Intent(LauncherActivity.this,HomeScreen.class);
                    startActivity(iHomeScreen);
                    finish();
                } else{
                    Intent iHomeScreen = new Intent(LauncherActivity.this,LoginScreen.class);
                    startActivity(iHomeScreen);
                    finish();
                }

            }
        }.start();



    }


}
