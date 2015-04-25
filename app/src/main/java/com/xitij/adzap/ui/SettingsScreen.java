package com.xitij.adzap.ui;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xitij.adzap.R;
import com.xitij.adzap.helpers.PrefUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsScreen extends ActionBarActivity {

    private Toolbar toolbar;
    private Switch swLock,swBackground;
    private TextView txtLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView subTitle_toolbar = (TextView)toolbar.findViewById(R.id.toolbar_title);


        subTitle_toolbar.setText("Settings");

        txtLogout = (TextView)findViewById(R.id.txtLogout);
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
                    processSetWallpaper();
                }else{
                    processUnSetWallpaper();
                }
            }
        });



        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(SettingsScreen.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure want to logout?")
                        .setConfirmText("Yes, Logout me")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.setTitleText("Logged out")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                PrefUtils.setLogin(SettingsScreen.this,false);

                                                Intent iHomeScreen = new Intent(SettingsScreen.this,LoginScreen.class);
                                                iHomeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(iHomeScreen);
                                                finish();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(PrefUtils.getChangeBackground(SettingsScreen.this)){
            swBackground.setChecked(true);
        }

        if(PrefUtils.getLocalScreenBackground(SettingsScreen.this)){
            swLock.setChecked(true);
        }

    }

    private void processSetWallpaper(){

      /*  myAsyncTask myWebFetch = new myAsyncTask();
        myWebFetch.execute();
*/
        startService(new Intent(SettingsScreen.this , ChangeWallpaperService.class));
        PrefUtils.setChangeBackground(SettingsScreen.this,true);
    }

    private void processsetImage(){

        Bitmap wallpaper = BitmapFactory.decodeFile("/sdcard/ADZAP/ADZAPWallpaper.JPG");
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(wallpaper);
            Toast.makeText(SettingsScreen.this,"Sucessfully Wallpaper set",Toast.LENGTH_LONG).show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }



    private void processUnSetWallpaper(){
        stopService(new Intent(SettingsScreen.this , ChangeWallpaperService.class));
        PrefUtils.setChangeBackground(SettingsScreen.this,false);
      /*WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setResource(R.raw.default_wallpaper);
        } catch (IOException e) {

            e.printStackTrace();
        }*/



    }



    private void processSetLock(){
        ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();

        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        PrefUtils.setLocalScreenBackground(SettingsScreen.this, true);




    }
    private void processSetUnLock(){
        ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").reenableKeyguard();
         unregisterReceiver(mybroadcast);

        PrefUtils.setLocalScreenBackground(SettingsScreen.this,false);
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


    class myAsyncTask extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog pDialog;
        boolean isImageSucessfullyLoaded=false;
        myAsyncTask()
        {
            pDialog = new SweetAlertDialog(SettingsScreen.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Setting up Wallpaper");
            pDialog.setCancelable(false);
           // pDialog.show();
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(isImageSucessfullyLoaded){
                processsetImage();

            }else
                Toast.makeText(SettingsScreen.this,"Image not found or network error",Toast.LENGTH_LONG).show();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded


             //   URL url = new URL("http://johnsite.com.accu17.com/ADZAPP/Images/51a870d4-6665-400c-b0cd-5d336174241d.JPG");

                URL url = new URL("http://www.androidbegin.com/wp-content/uploads/2013/07/HD-Logo.gif");
                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //and connect!
                urlConnection.connect();

                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.

                File dir = new File(SDCardRoot.getAbsolutePath()+ "/ADZAP/");
                dir.mkdirs();
                File file = new File(dir,"ADZAPWallpaper.JPG");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    //updateProgress(downloadedSize, totalSize);

                }
                //close the output stream when done
                fileOutput.close();

                isImageSucessfullyLoaded = true;

                //catch some possible errors...
            } catch (MalformedURLException e) {
                isImageSucessfullyLoaded=false;
                e.printStackTrace();

                Log.e("exc1",e.toString());
            } catch (IOException e) {
                isImageSucessfullyLoaded=false;

                e.printStackTrace();
                Log.e("exc2",e.toString());
            }catch (Exception e){
                isImageSucessfullyLoaded=false;

                Log.e("exc3",e.toString());
            }

            return null;
        }
    }

//end of main class
}
