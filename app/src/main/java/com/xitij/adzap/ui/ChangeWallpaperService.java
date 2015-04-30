package com.xitij.adzap.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Krishna on 25-04-2015.
 */
public class ChangeWallpaperService extends Service {


    Timer timer = new Timer();
    TimerTask updateProfile = new WallpaperTimerTasker(ChangeWallpaperService.this);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        timer.scheduleAtFixedRate(updateProfile, 0, 30000);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
      //  Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }

}
