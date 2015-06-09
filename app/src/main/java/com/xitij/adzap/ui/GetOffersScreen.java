package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.OfferListAdapter;
import com.xitij.adzap.helpers.AdvancedSpannableString;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.GeoLocation;
import com.xitij.adzap.model.Offers;
import com.xitij.adzap.model.User;
import com.xitij.adzap.model.VideoOffers;
import com.xitij.adzap.model.VideoOffersItems;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.ArrayList;

public class GetOffersScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private Offers currentOffer;
    private TextView txtCoin;
    private View emptyView;
    private ImageView earnCoin;
    private LinearLayout linearList,linearEmpty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getoffers_screen);

        linearList = (LinearLayout)findViewById(R.id.linearList);
        linearEmpty= (LinearLayout)findViewById(R.id.linearEmpty);
      /*  setContentView(R.layout.offer_item_emptyview);

        txtCoin = (TextView)findViewById(R.id.txtCoin);
        earnCoin= (ImageView) findViewById(R.id.earnCoin);

        int col = Color.parseColor("#FFFFE6");
        earnCoin.setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
        earnCoin.setImageResource(R.drawable.earn_coins);
*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Offers");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


       // emptyView = ((LayoutInflater) GetOffersScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.offer_item_emptyview, null, false);
        txtCoin = (TextView)findViewById(R.id.txtCoin);


        offerListView = (ListView)findViewById(R.id.offerList);



        //init();



    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(GetOffersScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        processGetOffers();
    }

    private void processGetOffers() {
        dialog = new CircleDialog(GetOffersScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GetOffersScreen.this, "user_pref", 0);
        final User currentUser = complexPreferences.getObject("current_user", User.class);


        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(GetOffersScreen.this, "user_pref", 0);
        GeoLocation gl = complexPreferences2.getObject("current_location", GeoLocation.class);

        Log.e("link",AppConstants.GET_OFFERS + currentUser.UserId+"/"+gl.cityID);
        new CallWebService(AppConstants.GET_OFFERS + currentUser.UserId+"/"+gl.cityID, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());



                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {

                        linearList.setVisibility(View.VISIBLE);
                        linearEmpty.setVisibility(View.GONE);

                       // Toast.makeText(GetOffersScreen.this, obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();

                        currentOffer = new GsonBuilder().create().fromJson(response, Offers.class);
                        Log.e("offer size",""+currentOffer.ViewAdz.size());
                      /*  VideoOffers vdoOffer = new VideoOffers();

                         for(int i=0;i<currentOffer.ViewAdz.size();i++){
                            if(!currentOffer.ViewAdz.get(i).VideoPath.equalsIgnoreCase("")){


                                VideoOffersItems vi = new VideoOffersItems();
                                vi.AdId = currentOffer.ViewAdz.get(i).AdId;

                                vdoOffer.ViewAdz.add(vi);
                            }
                        }

                       Log.e("video offer size",""+vdoOffer.ViewAdz.size());*/


                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(GetOffersScreen.this, "user_pref", 0);
                        complexPreferences.putObject("current_offer", currentOffer);
                        complexPreferences.commit();

                        OfferListAdapter adpater = new OfferListAdapter(GetOffersScreen.this,currentOffer);
                        offerListView.setAdapter(adpater);

                    } else {

                        linearList.setVisibility(View.GONE);
                        linearEmpty.setVisibility(View.VISIBLE);

                        String recentCoins = PrefUtils.getRecentcoins(GetOffersScreen.this);
                        String mainString  = "You have recently earned "+recentCoins+" coins";
                        AdvancedSpannableString adv = new AdvancedSpannableString(mainString);
                        int col = Color.parseColor("#F95D0C");
                        adv.setColor(col,recentCoins);

                        txtCoin.setText(adv);
                      //  Toast.makeText(GetOffersScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("exv",e.toString());
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
                set.putExtra("pos", position);
                startActivity(set);
            }
        });
    }



//end of main class
}
