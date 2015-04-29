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
import com.xitij.adzap.adapters.BankListAdapter;
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

public class AddBankScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private ListView offerListView;
    private Toolbar toolbar;
    private ViewInvoice cuurentInvoice;
    private TextView txtSave;
    private View emptyView;
    private LinearLayout linearList,linearEmpty;
    private Spinner spAccountType;
    private static final String[] accType = new String[] {
            "Select", "Checking","Savings"
    };
    User currentUser;
    private BankList cuurentBankList;
    private EditText etBankName,etBankBranchName,etAccNo,etAccPersonName,etIFSCCode,etAddress;
    private boolean isNewBank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbank_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_Title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbar.setNavigationIcon(R.drawable.icon_back_blue);

        txtSave = (TextView)findViewById(R.id.txtSave);
        isNewBank = getIntent().getBooleanExtra("isNewBankADD",false);

        if(isNewBank){
            toolbar_Title.setText("Add Bank Details");
            txtSave .setText("SAVE DETAILS");
        }else{
            toolbar_Title.setText("Edit Bank Details");
            txtSave .setText("UPDATE DETAILS");
            getBankList();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initalizeViews();
        init();


    }

    private void initalizeViews(){
        spAccountType = (Spinner)findViewById(R.id.spAccountType);

        etBankName = (EditText)findViewById(R.id.etBankName);
        etBankBranchName = (EditText)findViewById(R.id.etBankBranchName);
        etAccNo = (EditText)findViewById(R.id.etAccNo);
        etAccPersonName = (EditText)findViewById(R.id.etAccPersonName);
        etIFSCCode = (EditText)findViewById(R.id.etIFSCCode);
        etAddress = (EditText)findViewById(R.id.etAddress);




        ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, accType);
        spAccountType.setAdapter(spinadapter);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNewBank){
                    processSaveBank();
                }else{
                   processUpdateBank();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current_user", User.class);

    }

private void processUpdateBank(){

        try{

            int spPos = spAccountType.getSelectedItemPosition();
            int banklistpos =  PrefUtils.getBankListPos(AddBankScreen.this);

            JSONObject userobj = new JSONObject();
            userobj.put("ACNO",etAccNo.getText().toString().trim());
            userobj.put("AccountPersonName",etAccPersonName.getText().toString().trim());
            userobj.put("AccountType",accType[spPos]);
            userobj.put("Address",etAddress.getText().toString().trim());
            userobj.put("BankBranch",etBankBranchName.getText().toString().trim());
            userobj.put("BankId",cuurentBankList.Bank.get(banklistpos).BankId);
            userobj.put("BankName",etBankName.getText().toString().trim());
            userobj.put("CustId",currentUser.UserId);
            userobj.put("IFSCNo",etIFSCCode.getText().toString().trim());

            Log.e("Req update bank", userobj.toString());

            dialog= new CircleDialog(AddBankScreen.this,0);
            dialog.setCancelable(false);
            dialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.UPDATE_BANK_DETAILS, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    dialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response upddate bank: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("Response").equalsIgnoreCase("0")){

                           /* User currentUser2 = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref",0);
                            complexPreferences.putObject("current_user", currentUser2);
                            complexPreferences.commit();
*/

                            Toast.makeText(AddBankScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                            Intent iCOnfirmSignUp = new Intent( AddBankScreen.this ,BankListScreen.class );
                            startActivity(iCOnfirmSignUp);
                            finish();
                        }

                        else {
                            Toast.makeText(AddBankScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    Log.e("error : ", error + "");
                    Toast.makeText(AddBankScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){

        }


    }

private void processSaveBank(){
        try{

            int spPos = spAccountType.getSelectedItemPosition();

            JSONObject userobj = new JSONObject();
            userobj.put("ACNO",etAccNo.getText().toString().trim());
            userobj.put("AccountPersonName",etAccPersonName.getText().toString().trim());
            userobj.put("AccountType",accType[spPos]);
            userobj.put("Address",etAddress.getText().toString().trim());
            userobj.put("BankBranch",etBankBranchName.getText().toString().trim());
            userobj.put("BankId",0);
            userobj.put("BankName",etBankName.getText().toString().trim());
            userobj.put("CustId",currentUser.UserId);
            userobj.put("IFSCNo",etIFSCCode.getText().toString().trim());

            Log.e("Req bank", userobj.toString());

            dialog= new CircleDialog(AddBankScreen.this,0);
            dialog.setCancelable(false);
            dialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SAVE_BANK_DETAILS, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    dialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response bank: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("Response").equalsIgnoreCase("0")){

                           /* User currentUser2 = new GsonBuilder().create().fromJson(response,User.class);
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref",0);
                            complexPreferences.putObject("current_user", currentUser2);
                            complexPreferences.commit();
*/

                            Toast.makeText(AddBankScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                            Intent iCOnfirmSignUp = new Intent( AddBankScreen.this ,BankListScreen.class );
                            startActivity(iCOnfirmSignUp);
                            finish();
                        }

                        else {
                            Toast.makeText(AddBankScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    Log.e("error : ", error + "");
                    Toast.makeText(AddBankScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){

        }


    }

 private void getBankList(){

        dialog = new CircleDialog(AddBankScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(AddBankScreen.this, "user_pref", 0);
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

                        fillBankdetails();

                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                }



            }

            @Override
            public void error(VolleyError error) {
                Toast.makeText(AddBankScreen.this, "Network Error, Please Try again.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }.start();
    }

private void fillBankdetails(){

    int banklistpos =  PrefUtils.getBankListPos(AddBankScreen.this);

    etAccNo.setText(cuurentBankList.Bank.get(banklistpos).ACNO.toString().trim());
    etAccPersonName.setText(cuurentBankList.Bank.get(banklistpos).AccountPersonName.toString().trim());

    if(cuurentBankList.Bank.get(banklistpos).AccountType.equalsIgnoreCase("Checking")){
         spAccountType.setSelection(1);
     }else if(cuurentBankList.Bank.get(banklistpos).AccountType.equalsIgnoreCase("Savings")){
        spAccountType.setSelection(2);
     }

    etAddress.setText(cuurentBankList.Bank.get(banklistpos).Address.toString().trim());
    etBankBranchName.setText(cuurentBankList.Bank.get(banklistpos).BankBranch.toString().trim());
    etBankName.setText(cuurentBankList.Bank.get(banklistpos).BankName.toString().trim());
    etIFSCCode.setText(cuurentBankList.Bank.get(banklistpos).IFSCNo.toString().trim());

}




    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(AddBankScreen.this);
          /*  etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);*/
    }




//end of main class
}
