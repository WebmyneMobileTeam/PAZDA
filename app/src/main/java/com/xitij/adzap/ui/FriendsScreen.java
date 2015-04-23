package com.xitij.adzap.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class FriendsScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Friends");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();



    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(FriendsScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }






//end of main class
}
