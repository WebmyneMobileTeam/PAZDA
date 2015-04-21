package com.xitij.adzap.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xitij.adzap.R;
import com.xitij.adzap.helpers.PrefUtils;

public class HomeScreen extends ActionBarActivity implements View.OnClickListener{

    private ViewGroup menuEarnCoins,menuRewards,menuFriends,menuHistory;
    private  ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupUI();
    }

    private void setupUI() {


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
