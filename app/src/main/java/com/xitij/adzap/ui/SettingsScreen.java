package com.xitij.adzap.ui;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.xitij.adzap.R;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.widget.CircleDialog;

public class SettingsScreen extends ActionBarActivity {

    private CircleDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(SettingsScreen.this);

    }





//end of main class
}
