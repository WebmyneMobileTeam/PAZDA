package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.HistoryListAdapter;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.History;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.BadgeView;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class HistoryScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private TextView txtRefCode,txtFriend,txtCoins;

    private String REFERAL_CODE = "123456ABC";
    BadgeView badgeFreinds,badgeCoins;
    private String txtFriendsValue,txtFreindsCoinsValue;
    private History historyObject;
    private ListView listHistory;
    private LinearLayout linearEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_screen);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_Title.setText("History");

        listHistory = (ListView)findViewById(R.id.listHistory);

        linearEmpty = (LinearLayout)findViewById(R.id.historyemptyView);
        getHistory();

    }



    private void getHistory(){
        dialog = new CircleDialog(HistoryScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(HistoryScreen.this, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);



        new CallWebService(AppConstants.VIEW_HISTORY + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        historyObject = new GsonBuilder().create().fromJson(response, History.class);

                        HistoryListAdapter adpater = new HistoryListAdapter(HistoryScreen.this,historyObject);
                        listHistory.setAdapter(adpater);

                        if(historyObject.Transcation.size()==0){
                            listHistory.setEmptyView(linearEmpty);
                        }
                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(HistoryScreen.this, "Network Error, Please Try again.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }.start();
    }



//end of main class
}
