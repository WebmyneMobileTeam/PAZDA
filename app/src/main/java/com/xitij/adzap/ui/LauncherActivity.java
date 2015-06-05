package com.xitij.adzap.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AdvancedSpannableString;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.AppLocationService;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.CITYSTATELIST;
import com.xitij.adzap.model.Offers;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;


public class LauncherActivity extends ActionBarActivity {
    boolean isLogin;
    // The minimum distance to change Updates in meters
    long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        fetchCityState();

   /*     new CountDownTimer(2500,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {


            }
        }.start();
*/


    }


    void fetchCityState(){

        new CallWebService(AppConstants.GET_CITY_STATE_LIST , CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("response loc", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("1")) {

                        CITYSTATELIST cityobj = new GsonBuilder().create().fromJson(response, CITYSTATELIST.class);

                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LauncherActivity.this, "user_pref", 0);
                        complexPreferences.putObject("current_location", cityobj);
                        complexPreferences.commit();



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

                } catch (Exception e) {

                }


            }

            @Override
            public void error(VolleyError error) {
                Log.e("volly er", error.toString());
            }
        }.start();


    }

    @Override
    protected void onResume() {
        super.onResume();

        getCellTowerInfo();

    }

    public  void getCellTowerInfo() {


        AppLocationService appLocationService = new AppLocationService(LauncherActivity.this);

        Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            String provoide = nwLocation.getProvider();

            try {
                Geocoder geocoder = new Geocoder(LauncherActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getLocality();
                String countryName = addresses.get(0).getCountryName();



                PrefUtils.setCity(LauncherActivity.this,cityName);
                PrefUtils.setState(LauncherActivity.this,cityName);

                Toast.makeText(
                        getApplicationContext(),
                        "Mobile Location (NW): \nLatitude: " + latitude
                                + "\nLongitude: " + longitude + "\ncityName:" + cityName + "\ncountryName:" + countryName,
                        Toast.LENGTH_LONG).show();


            }catch (Exception e){
                Log.e("exc",e.toString());
            }

        } else {
          Toast.makeText(LauncherActivity.this,"Network error !!!",Toast.LENGTH_SHORT).show();
        }


    }



}
