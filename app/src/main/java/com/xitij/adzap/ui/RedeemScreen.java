package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.GsonBuilder;
import com.xitij.adzap.R;
import com.xitij.adzap.base.MyApplication;
import com.xitij.adzap.helpers.AppConstants;
import com.xitij.adzap.helpers.CallWebService;
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.BankList;
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
    private EditText spRedeemMoney;
    private static final String[] amount = new String[] {
            "Select", "INR 100", "INR 200", "INR 500", "INR 1000"
    };
    BankList currentBankList;
    User currentUser;
    int pos;
    double redeemAmount=0.0;
    boolean isOk =false;

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
        spRedeemMoney = (EditText)findViewById(R.id.spRedeemMoney);



        txtSelectBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reward = new Intent(RedeemScreen.this,BankListScreen.class);
                startActivity(reward);
            }
        });


        txtRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // 1 Rs = 15 Coins
                Float coins = Float.valueOf(currentUser.Balance);
                Float temp_rupees = coins /AppConstants.coinRate;
                // String final_rate = String.format("%.2f", temp_rupees);

                Log.e("bal",""+currentUser.Balance);
                Log.e("rupees",""+temp_rupees);
                double userBalance = Double.parseDouble(currentUser.Balance);


                if(spRedeemMoney.getText().toString().length()==0){
                    Toast.makeText(RedeemScreen.this,"Please enter valid amount for redeem",Toast.LENGTH_LONG).show();
                    isOk=false;
                }else if(currentBankList == null) {
                    Toast.makeText(RedeemScreen.this,"Please Select Bank Details !!!",Toast.LENGTH_LONG).show();

                }
               /* else if(temp_rupees<redeemAmount){
                    Toast.makeText(RedeemScreen.this,"You dont'have Sufficient amount !!!",Toast.LENGTH_LONG).show();
                }*/
                else {

                    redeemAmount = Double.valueOf(spRedeemMoney.getText().toString().trim());

                    processRedeem();
                }
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(RedeemScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current_user", User.class);


        ComplexPreferences complexPreferences2= ComplexPreferences.getComplexPreferences(RedeemScreen.this, "user_pref", 0);
        currentBankList = complexPreferences2.getObject("current_bank", BankList.class);

        pos =  PrefUtils.getBankListPos(RedeemScreen.this);
        if(currentBankList == null){
            txtBankName.setVisibility(View.GONE);
            txtAcc.setVisibility(View.GONE);
        }else{
            txtBankName.setVisibility(View.VISIBLE);
            txtAcc.setVisibility(View.VISIBLE);
            txtBankName.setText(currentBankList.Bank.get(pos).BankName);
            txtAcc.setText(currentBankList.Bank.get(pos).ACNO);
        }

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(RedeemScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }

    private void processRedeem(){


        try{

            JSONObject userobj = new JSONObject();
            userobj.put("ACNO",currentBankList.Bank.get(pos).ACNO);
            userobj.put("AccountPersonName",currentBankList.Bank.get(pos).AccountPersonName);
            userobj.put("AccountType",currentBankList.Bank.get(pos).AccountType);
            userobj.put("Address",currentBankList.Bank.get(pos).Address);
            userobj.put("BankBranch",currentBankList.Bank.get(pos).BankBranch);

            userobj.put("Amount",String.valueOf(redeemAmount));

            double coins = redeemAmount * AppConstants.coinRate;

            userobj.put("Coins",String.valueOf(coins));
            userobj.put("Date","");

            userobj.put("BankName",currentBankList.Bank.get(pos).BankName);
            userobj.put("CustId",currentUser.UserId);
            userobj.put("IFSCNo",currentBankList.Bank.get(pos).IFSCNo);

            Log.e("Req redeem", userobj.toString());

            dialog= new CircleDialog(RedeemScreen.this,0);
            dialog.setCancelable(false);
            dialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.GENERATE_INVOICE, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    dialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response redeem: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("Response").equalsIgnoreCase("0")){

                           /* User currentUser2 = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref",0);
                            complexPreferences.putObject("current_user", currentUser2);
                            complexPreferences.commit();
*/

                            Toast.makeText(RedeemScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                            Intent iCOnfirmSignUp = new Intent( RedeemScreen.this ,RewardsScreen.class );
                            startActivity(iCOnfirmSignUp);
                            finish();
                        }

                        else {
                            Toast.makeText(RedeemScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    Log.e("error : ", error + "");
                    Toast.makeText(RedeemScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){
                Log.e("exc",e.toString());
        }


    }





//end of main class
}
