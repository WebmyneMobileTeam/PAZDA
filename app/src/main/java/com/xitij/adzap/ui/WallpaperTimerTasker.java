package com.xitij.adzap.ui;

import java.io.IOException;
import java.util.TimerTask;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.xitij.adzap.R;

/**
 * Created by Krishna on 25-04-2015.
 */
public class WallpaperTimerTasker extends TimerTask {

    private Context context;
    private Handler mHandler = new Handler();
    public WallpaperTimerTasker(Context con) {
        this.context = con;
    }
    public int [] imagesID = {R.raw.bg,R.raw.bgfive,R.raw.bgfour,R.raw.bgsix,R.raw.bgthree,R.raw.bgtwo,R.raw.default_wallpaper};
    public int counter = 0;
    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {

                        if(counter==imagesID.length){
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

                    }
                });
            }
        }).start();
    }
}
