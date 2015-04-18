package com.xitij.adzap.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.xitij.adzap.R;

public class HomeScreen extends ActionBarActivity {

    private ViewGroup menuOne,menuTwo,menuThree,menuFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupUI();
    }

    private void setupUI() {

        menuFour = (ViewGroup)findViewById(R.id.menuFour);
        menuOne = (ViewGroup)findViewById(R.id.menuOne);
        menuThree = (ViewGroup)findViewById(R.id.menuThree);
        menuTwo = (ViewGroup)findViewById(R.id.menuTwo);

    }
}
