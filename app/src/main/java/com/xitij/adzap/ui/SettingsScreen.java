package com.xitij.adzap.ui;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.AdImageList;
import com.xitij.adzap.model.CheckBalance;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private TextView txtLogout,txtSetWallpaper,txtContact;
    String saveImagePath;
    private CircleDialog cDialog;
    AdImageList adImageList;

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
        txtContact= (TextView)findViewById(R.id.txtContact);



        txtContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "support@adzapp.in", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));



/*
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "  support@adzapp.in");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                intent.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(intent, "Send Email"));*/
            }
        });

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

        if(PrefUtils.getLocalScreenBackground(SettingsScreen.this)){
            swLock.setChecked(true);
        }

    }



private void processloadImageLists(){

    cDialog = new CircleDialog(SettingsScreen.this, 0);
    cDialog.setCancelable(false);
    cDialog.show();

    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SettingsScreen.this, "user_pref", 0);
    User currentUser = complexPreferences.getObject("current_user", User.class);



    new CallWebService(AppConstants.GET_AD_IMAGES + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

        @Override
        public void response(String response) {
            cDialog.dismiss();
            Log.e("response", response.toString());

            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("Response").equalsIgnoreCase("0")) {
                    adImageList = new GsonBuilder().create().fromJson(response, AdImageList.class);


                } else {
                    //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {

            }



        }

        @Override
        public void error(VolleyError error) {
            Log.e("volly er", error.toString());
            cDialog.dismiss();
        }
    }.start();
    }

    private void processSetWallpaper(){

  /*      int counter = PrefUtils.gettPositionForWallpaper(SettingsScreen.this);

        if(counter == adImageList.Img.size()){
            counter=0;
        }

        Log.e("Image Path",""+adImageList.Img.get(counter).ImgPath.toString());

        String tempPath = adImageList.Img.get(counter).ImgPath.toString();
        String subPath = tempPath.substring(tempPath.lastIndexOf("/")+1,tempPath.length());

        Log.e("Sub Path",subPath);
        String imagUrl=subPath;
        myAsyncTask myWebFetch = new myAsyncTask(imagUrl,counter);
        myWebFetch.execute();*/

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
        Log.e("error in prr","dsd");
            e.printStackTrace();
        }
    }



    private void processUnSetWallpaper(){
        stopService(new Intent(SettingsScreen.this , ChangeWallpaperService.class));
        PrefUtils.setChangeBackground(SettingsScreen.this,false);


    }



    private void processSetLock(){
      /*  Intent i = new Intent(SettingsScreen.this,LockScreenAppActivity.class);
        startActivity(i);
*/

        startService(new Intent(this,MyService.class));

       /* ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();



        Intent i0 = new Intent();
        i0.setAction("com.xitij.adzap.ui.LockService");
        startService(i0);*/


       /* registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
*/
/*
        IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addDataType(String);
        registerReceiver(LockReciver, intentFilter);*/
        PrefUtils.setLocalScreenBackground(SettingsScreen.this, true);




    }
    private void processSetUnLock(){
        ((KeyguardManager)getSystemService(SettingsScreen.this.KEYGUARD_SERVICE)).newKeyguardLock("IN").reenableKeyguard();
      //   unregisterReceiver(mybroadcast);
stopService(new Intent(this,MyService.class));
        PrefUtils.setLocalScreenBackground(SettingsScreen.this,false);
    }






    class myAsyncTask extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog pDialog;
        boolean isImageSucessfullyLoaded=false;
        int counter;
        String imageUrl;
        myAsyncTask(String uurl,int count)
        {
            this.counter = count;
            this.imageUrl = uurl;
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
                //processsetImage();
                loadImageFromStorage(saveImagePath);

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
             //   http://www.allindiaflorist.com/imgs/arrangemen4.jpg
         /*       URL url = new URL("http://www.johnsite.com.accu17.com/ADZAPP/Images/d39683dd-36ac-47d5-b9f7-9163f9d4e298.JPG");

                //  URL url = new URL("http://www.androidbegin.com/wp-content/uploads/2013/07/HD-Logo.gif");
                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //and connect!
                urlConnection.connect();*/




                URL url = new URL(AppConstants.BASE_URL_IMAGE+imageUrl);
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                InputStream input = bufHttpEntity.getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();

                saveImagePath = saveToInternalSorage(bitmap);

                counter +=1;
                PrefUtils.setPositionForWallpaper(SettingsScreen.this,counter);


      /*          //set the path where we want to save the file
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
                fileOutput.close();*/

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


private void loadImageFromStorage(String path)    {

        try {
            File f=new File(path, "ADZAPWallpaper.jpg");
            Bitmap wallpaper = BitmapFactory.decodeStream(new FileInputStream(f));

            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(wallpaper);
                Toast.makeText(SettingsScreen.this,"Sucessfully Wallpaper set",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("error in prr","dsd");
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
private String saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ADZAP", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"ADZAPWallpaper.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }




//end of main class
}
