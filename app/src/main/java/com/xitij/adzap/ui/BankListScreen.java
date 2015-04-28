package com.xitij.adzap.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.adapters.BankListAdapter;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.BankList;
import com.xitij.adzap.model.User;
import com.xitij.adzap.model.ViewInvoice;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class BankListScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView bankList;
    private Toolbar toolbar;
    private BankList cuurentBankList;
    private TextView txtAddBank;

    private ImageView earnCoin;
    private LinearLayout linearList,linearEmpty;
    View emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banklist_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);
        toolbar_Title.setText("Bank Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
        txtAddBank = (TextView)findViewById(R.id.txtAddBank);
        bankList = (ListView)findViewById(R.id.bankList);

        emptyView = ((LayoutInflater) BankListScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.banklist_empty_view, null, false);


        bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            PrefUtils.setBankListPos(BankListScreen.this,position);

                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(BankListScreen.this, "user_pref",0);
                            complexPreferences.putObject("current_bank", cuurentBankList);
                            complexPreferences.commit();

                            finish();
            }
        });


        txtAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reward = new Intent(BankListScreen.this,AddBankScreen.class);
                startActivity(reward);
            }
        });



    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(BankListScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBankList();
    }

    private void getBankList(){
        dialog = new CircleDialog(BankListScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(BankListScreen.this, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);

        new CallWebService(AppConstants.VIEW_BANK_DETAILS + currentUser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response banklist", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        cuurentBankList = new GsonBuilder().create().fromJson(response, BankList.class);

                       BankListAdapter adpater = new BankListAdapter(BankListScreen.this,cuurentBankList);
                        bankList.setAdapter(adpater);

                        if(cuurentBankList.Bank.size()==0){
                            bankList.setEmptyView(emptyView);
                        }

                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(BankListScreen.this, "Network Error, Please Try again.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }.start();
    }


//end of main class
}
