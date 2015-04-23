package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
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
import com.xitij.adzap.widget.BadgeView;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class FriendsScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private TextView txtRefCode,txtFriend,txtCoins;

    private String REFERAL_CODE = "123456ABC";
    BadgeView badgeFreinds,badgeCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        txtFriend = (TextView)findViewById(R.id.txtFriend);
        txtCoins = (TextView)findViewById(R.id.txtCoins);
        txtRefCode = (TextView)findViewById(R.id.txtRefCode);
        toolbar_Title.setText("Friends");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();



        txtRefCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text="Won't you like to get gifts every time you're bored ?\nUse my referal code : "+REFERAL_CODE+" to get bonous coins.\nhttp://wallet.sm.kdmg";

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        setBadges();
    }


    private void setBadges(){

        int badgeColor = Color.parseColor("#F95D0C");


        badgeFreinds = new BadgeView(this, txtFriend);
        badgeCoins  = new BadgeView(this, txtCoins);

        badgeFreinds.setText("12");
        badgeFreinds.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeFreinds.setBadgeBackgroundColor(badgeColor);

        badgeCoins.setText("0");
        badgeCoins.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeCoins.setBadgeBackgroundColor(badgeColor);

        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
        anim.setInterpolator(new BounceInterpolator());
        anim.setDuration(1000);
        badgeFreinds.toggle(anim, null);
        badgeCoins.toggle(anim, null);

    }
    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(FriendsScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }






//end of main class
}
