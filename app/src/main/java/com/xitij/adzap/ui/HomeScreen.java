package com.xitij.adzap.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.CheckBalance;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class HomeScreen extends ActionBarActivity implements View.OnClickListener{

    private ViewGroup menuEarnCoins,menuRewards,menuFriends,menuHistory;
    private  ImageView settings;
    private TextView txtCoin,txtINR;
    private CircleDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBalance();
    }

    private void setupBalance(){
        dialog = new CircleDialog(HomeScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();


        new CallWebService(AppConstants.CHECK_BALANCE + 10008, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        CheckBalance chkBalance = new GsonBuilder().create().fromJson(response, CheckBalance.class);

                        if(chkBalance.ClosingBal == null){
                            txtCoin.setText("0");
                            txtINR.setText("₹ 0");
                        }else{
                            txtCoin.setText(chkBalance.ClosingBal);
                            txtINR.setText("₹ "+chkBalance.ClosingBal);
                        }


                    } else {
                        Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void error(VolleyError error) {
                Log.e("volly er", error.toString());
                dialog.dismiss();
            }
        }.start();
    }

    private void setupUI() {


        txtCoin = (TextView)findViewById(R.id.txtCoin);
        txtINR = (TextView)findViewById(R.id.txtINR);

        settings = (ImageView)findViewById(R.id.settings);
        menuHistory = (ViewGroup)findViewById(R.id.menuFour);
        menuEarnCoins = (ViewGroup)findViewById(R.id.menuOne);
        menuFriends = (ViewGroup)findViewById(R.id.menuThree);
        menuRewards = (ViewGroup)findViewById(R.id.menuTwo);

        menuHistory.setOnClickListener(this);
        menuEarnCoins.setOnClickListener(this);
        menuFriends.setOnClickListener(this);
        menuRewards.setOnClickListener(this);

        setRewards();
        setEarnCoins();
        setFriends();
        setHistory();


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent set = new Intent(HomeScreen.this,SettingsScreen.class);
                startActivity(set);
            }
        });



    }

    private void setRewards() {

        ImageView img = (ImageView)menuRewards.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuRewards.findViewById(R.id.itemHomeText);
        txt.setTypeface(PrefUtils.getTypeFaceCalibri(HomeScreen.this));
        img.setImageResource(R.drawable.rewards);
        txt.setText("REWARDS");


    }

    private void setEarnCoins() {

        ImageView img = (ImageView)menuEarnCoins.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuEarnCoins.findViewById(R.id.itemHomeText);
        txt.setTypeface(PrefUtils.getTypeFaceCalibri(HomeScreen.this));
        img.setImageResource(R.drawable.earn_coins);
        txt.setText("EARN COINS");

    }


    private void setFriends() {

        ImageView img = (ImageView)menuFriends.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuFriends.findViewById(R.id.itemHomeText);
        txt.setTypeface(PrefUtils.getTypeFaceCalibri(HomeScreen.this));
        img.setImageResource(R.drawable.friends);
        txt.setText("FRIENDS");
    }

    private void setHistory() {

        ImageView img = (ImageView)menuHistory.findViewById(R.id.itemHomeImage);
        TextView txt = (TextView)menuHistory.findViewById(R.id.itemHomeText);
        txt.setTypeface(PrefUtils.getTypeFaceCalibri(HomeScreen.this));
        img.setImageResource(R.drawable.history);
        txt.setText("HISTORY");

    }


    @Override
    public void onClick(View v) {

        if(PrefUtils.isLogin(HomeScreen.this)){

        }else{
            Intent iLogin = new Intent(HomeScreen.this,LoginScreen.class);
            startActivity(iLogin);
        }


        switch (v.getId()){
            case R.id.menuOne:
                Intent offer = new Intent(HomeScreen.this,GetOffersScreen.class);
                startActivity(offer);
                break;
        }


    }
}
