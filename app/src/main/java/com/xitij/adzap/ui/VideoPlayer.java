package com.xitij.adzap.ui;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class VideoPlayer extends ActionBarActivity {

    private CircleDialog dialog;
    private VideoView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        videoPlayer = (VideoView) findViewById(R.id.videoPlayer);

        init();
        String url = getIntent().getStringExtra("url");
        processGetVideoPlayer(url);

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(VideoPlayer.this);

    }




    private void processGetVideoPlayer(String path) {
        dialog = new CircleDialog(VideoPlayer.this, 0);
        dialog.setCancelable(false);
        dialog.show();


        try {
            MediaController mc = new MediaController(VideoPlayer.this);
            mc.setAnchorView(videoPlayer);
            mc.setMediaPlayer(videoPlayer);
            Uri uri = Uri.parse(path);
            videoPlayer.setMediaController(mc);
            videoPlayer.setVideoURI(uri);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoPlayer.requestFocus();
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                dialog.dismiss();
                videoPlayer.start();
            }
        });


    }


//end of main class
}
