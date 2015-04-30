package com.xitij.adzap.ui;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.AdImageList;
import com.xitij.adzap.model.User;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;

/**
 * Created by Krishna on 25-04-2015.
 */
public class LockWallpaperTimerTasker extends TimerTask {
    String saveImagePath;
    AdImageList adImageList;
    private Context context;
    private Handler mHandler = new Handler();
    public LockWallpaperTimerTasker(Context con) {
        this.context = con;
    }
    public int counter = 0;
    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {


                        processloadImageLists();



                       /* if(counter==imagesID.length){
                            counter=0;
                        }

                        Toast.makeText(context, "Wallpaper Changed", Toast.LENGTH_SHORT).show();
                        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
                        try {
                            myWallpaperManager.setResource(imagesID[counter]);
                            counter = counter + 1;
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
*/
                    }
                });
            }
        }).start();
    }


    private void processloadImageLists(){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(context, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);


        new CallWebService(AppConstants.GET_AD_IMAGES + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        adImageList = new GsonBuilder().create().fromJson(response, AdImageList.class);


                        counter = PrefUtils.gettPositionForWallpaper(context);

                        if(counter == adImageList.Img.size()){
                            counter=0;
                        }

                        Log.e("Image Path",""+adImageList.Img.get(counter).ImgPath.toString());

                        String tempPath = adImageList.Img.get(counter).ImgPath.toString();
                        String subPath = tempPath.substring(tempPath.lastIndexOf("/")+1,tempPath.length());

                        Log.e("Sub Path",subPath);
                        String imagUrl=subPath;

                        myAsyncTask myWebFetch = new myAsyncTask(imagUrl,counter);
                        myWebFetch.execute();
                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Log.e("volly er", error.toString());
            }
        }.start();
    }



    class myAsyncTask extends AsyncTask<Void, Void, Void> {

        boolean isImageSucessfullyLoaded=false;
        int counter;
        String imageUrl;
        myAsyncTask(String uurl,int count)
        {
            this.counter = count;
            this.imageUrl = uurl;
            Log.e("inside","async task constructotr");

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e("inside","async task onpost");
            if(isImageSucessfullyLoaded){
                //processsetImage();
                loadImageFromStorage(saveImagePath);

            }else
                Toast.makeText(context,"Image not found or network error",Toast.LENGTH_LONG).show();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("inside","async task pnpre");
        }

        protected Void doInBackground(Void... arg0) {
            Log.e("inside","async onbackground start");
            try {
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
                PrefUtils.setPositionForWallpaper(context,counter);


                isImageSucessfullyLoaded = true;
                Log.e("inside","async onbackground end");
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


            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
            try {
                myWallpaperManager.setBitmap(wallpaper);
             //   Toast.makeText(context,"Sucessfully Wallpaper set",Toast.LENGTH_LONG).show();
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
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
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
