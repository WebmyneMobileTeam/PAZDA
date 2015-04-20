package com.xitij.adzap.ui;

import android.content.Intent;
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
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class GetOffersScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getoffers_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Log ind");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        offerListView = (ListView)findViewById(R.id.offerList);



        init();

        processGetOffers();

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(GetOffersScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }




    private void processGetOffers() {
        dialog = new CircleDialog(GetOffersScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();


        new CallWebService(AppConstants.GET_OFFERS + 5, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {

                        Toast.makeText(GetOffersScreen.this, obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();

                        Offers currentOffer = new GsonBuilder().create().fromJson(response, Offers.class);
                        Log.e("offer size",""+currentOffer.ViewAdz.size());

                        OfferListAdapter adpater = new OfferListAdapter(GetOffersScreen.this,currentOffer);
                        offerListView.setAdapter(adpater);

                       /* //store current user and domain in shared preferences
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GetOffersScreen.this, "user_pref", 0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();*/
                    } else {
                        Toast.makeText(GetOffersScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
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

    public boolean isEmailMatch(EditText param1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isEdiTextEmpty(EditText et) {

        boolean isEmpty = false;

        if (et.getText() == null || et.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }

        return isEmpty;

    }

//end of main class
}
