package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
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
    private Offers currentOffer;

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

                        currentOffer = new GsonBuilder().create().fromJson(response, Offers.class);
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



        offerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Image Path",""+currentOffer.ViewAdz.get(position).ImagePath);
                Log.e("Video Path",""+currentOffer.ViewAdz.get(position).VideoPath);

                String tempPath = currentOffer.ViewAdz.get(position).VideoPath.toString();
                String subPath = tempPath.substring(tempPath.lastIndexOf("/")+1,tempPath.length());

                Log.e("Sub Path",subPath);

                Intent set = new Intent(GetOffersScreen.this,VideoPlayer.class);
                set.putExtra("url",AppConstants.BASE_URL_VIDEO+subPath);
                startActivity(set);
            }
        });
    }



//end of main class
}
