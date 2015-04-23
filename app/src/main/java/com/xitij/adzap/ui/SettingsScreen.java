package com.xitij.adzap.ui;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import com.xitij.adzap.R;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.widget.CircleDialog;

public class SettingsScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private Toolbar toolbar;
    private Switch swLock,swBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView subTitle_toolbar = (TextView)toolbar.findViewById(R.id.toolbar_title);

        subTitle_toolbar.setText("Settings");


        swLock = (Switch)findViewById(R.id.swLock);
        swBackground = (Switch)findViewById(R.id.swBackground);
        init();



        swLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swLock.isChecked()){
                    processSetLock();
                }else{
                    processSetUnLock();
                }
            }
        });

        swBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(swBackground.isChecked()){
                  //  processSetLock();
                }else{
                    //processSetUnLock();
                }
            }
        });


    }

    private void processSetLock(){
        ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();

        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));





    }
    private void processSetUnLock(){
        ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").reenableKeyguard();
         unregisterReceiver(mybroadcast);
    }






    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(SettingsScreen.this);

    }


    BroadcastReceiver mybroadcast = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("inside service", "inside");
            if(intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                Intent localIntent = new Intent(context, LockScreen.class);
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
                context.startActivity(localIntent);
            }
        }
    };



//end of main class
}
