package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.base.MyApplication;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class VideoPlayer extends ActionBarActivity {

    private CircleDialog dialog;
    private VideoView videoPlayer;
    private TextView txtTimer;
    private int counter=20;
    double finalDuration;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
        txtTimer = (TextView)findViewById(R.id.txtTimer);

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
            videoPlayer.setMediaController(null);
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

                int dur = mp.getDuration();
                
                

                dur = dur/1000;
                finalDuration = Math.floor(dur)+1;
                temp = (int)finalDuration;

                Toast.makeText(VideoPlayer.this,"video length is- "+temp,Toast.LENGTH_LONG).show();

                new CountDownTimer(temp*1000,1020){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        temp = temp - 1;
                        txtTimer.setText(""+temp);
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(VideoPlayer.this,"Time is complete",Toast.LENGTH_LONG).show();
                        processRegisterRewardCoins();
                    }
                }.start();
            }
        });

        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoPlayer.this,"Video is complete",Toast.LENGTH_LONG).show();
                Log.e("Video is complete","completed");

            }
        });


    }

private void processRegisterRewardCoins(){
    int adID = getIntent().getIntExtra("adID",0);
    //store current user and domain in shared preferences
    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(VideoPlayer.this, "user_pref", 0);
    User currentUser = complexPreferences.getObject("current_user", User.class);

    try{
        JSONObject userobj = new JSONObject();
        userobj.put("AdId",adID);
        userobj.put("Userid",currentUser.UserId);

        Log.e("Req Resgister",userobj.toString());

        dialog= new CircleDialog(VideoPlayer.this,0);
        dialog.setCancelable(false);
        dialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.REQUEST_POINTS, userobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                dialog.dismiss();
                String response = jobj.toString();
                Log.e("Response Register: ", "" + response);

                try{

                    JSONObject obj = new JSONObject(response);

                    if(obj.getString("Response").equalsIgnoreCase("0")){

                      /*  User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SignUpActivity.this, "user_pref",0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();
*/

                        Toast.makeText(VideoPlayer.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                        Intent video = new Intent( VideoPlayer.this ,GetOffersScreen.class);
                        startActivity(video);
                        finish();
                    }

                    else {
                        Toast.makeText(VideoPlayer.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Log.e("error : ", error + "");
                Toast.makeText(VideoPlayer.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        MyApplication.getInstance().addToRequestQueue(req);

    }catch(Exception e){

    }

}
//end of main class
}
