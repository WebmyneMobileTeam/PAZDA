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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupUI();
    }

    private void setupUI() {

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


    }
}
