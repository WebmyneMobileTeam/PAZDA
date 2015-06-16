package com.xitij.adzap.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.AppLocationService;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.CITYSTATELIST;
import com.xitij.adzap.model.GeoLocation;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;


public class LauncherActivity extends ActionBarActivity {
    boolean isLogin;
    // The minimum distance to change Updates in meters
    long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    String CITYNAME;
    Location nwLocation;

    GoogleApiClient mGoogleApiClient;
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









    public  void getCellTowerInfo() {


        AppLocationService appLocationService = new AppLocationService(LauncherActivity.this);

        nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);


        new CountDownTimer(2500,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {


                if (nwLocation != null) {
                    double latitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();
                    String provoide = nwLocation.getProvider();

                    try {
                        Geocoder geocoder = new Geocoder(LauncherActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String cityName = addresses.get(0).getLocality();
                        String countryName = addresses.get(0).getCountryName();

                        CITYNAME  = cityName;

                        PrefUtils.setCity(LauncherActivity.this, cityName);
                        PrefUtils.setState(LauncherActivity.this, cityName);

               /* Toast.makeText(
                        getApplicationContext(),
                        "Mobile Location (NW): \nLatitude: " + latitude
                                + "\nLongitude: " + longitude + "\ncityName:" + cityName + "\ncountryName:" + countryName,
                        Toast.LENGTH_LONG).show();
*/

                        Toast.makeText(
                                getApplicationContext(),
                                "Your city is :- "+CITYNAME,
                                Toast.LENGTH_LONG).show();



                    }catch (Exception e){
                        Log.e("exc in taking n/w",e.toString());
                    }

                } else {
                    Toast.makeText(LauncherActivity.this,"Network error !!!",Toast.LENGTH_SHORT).show();
                }



            }
        }.start();




    }



}
