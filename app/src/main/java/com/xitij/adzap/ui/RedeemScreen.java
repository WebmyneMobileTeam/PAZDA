package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.User;
import com.xitij.adzap.model.ViewInvoice;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class RedeemScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private ViewInvoice cuurentInvoice;
    private TextView txtRedeem,txtSelectBank,txtBankName,txtAcc;
    private View emptyView;
    private ImageView earnCoin;
    private Spinner spRedeemMoney;
    private static final String[] amount = new String[] {
            "Select", "INR 100", "INR 200", "INR 500", "INR 1000"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Redeem Money");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();

        txtAcc = (TextView)findViewById(R.id.txtAcc);
        txtBankName = (TextView)findViewById(R.id.txtBankName);
        txtSelectBank = (TextView)findViewById(R.id.txtSelectBank);
        txtRedeem = (TextView)findViewById(R.id.txtRedeem);
        spRedeemMoney = (Spinner)findViewById(R.id.spRedeemMoney);

        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, amount);
        spRedeemMoney.setAdapter(spinadapter);


        txtSelectBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reward = new Intent(RedeemScreen.this,BankListScreen.class);
                startActivity(reward);
            }
        });

        getRewards();

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(RedeemScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }



    private void getRewards(){
        dialog = new CircleDialog(RedeemScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(RedeemScreen.this, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);

        new CallWebService(AppConstants.VIEW_INVOICE + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response invoice", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        cuurentInvoice = new GsonBuilder().create().fromJson(response, ViewInvoice.class);

                     /*   HistoryListAdapter adpater = new HistoryListAdapter(RewardsScreen.this,cuurentInvoice);
                        listHistory.setAdapter(adpater);*/
                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(RedeemScreen.this, "Network Error, Please Try again.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }.start();
    }


//end of main class
}
