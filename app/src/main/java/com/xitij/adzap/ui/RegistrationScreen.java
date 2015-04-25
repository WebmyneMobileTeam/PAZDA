package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegistrationScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private EditText etEmail,etName,etPassword,etCnfPassword,etPhone,etReferalCode;
    private TextView txtBtnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etName = (EditText)findViewById(R.id.etName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etCnfPassword = (EditText)findViewById(R.id.etCnfPassword);
        etPhone = (EditText)findViewById(R.id.etPhone);
        txtBtnLogin = (TextView)findViewById(R.id.txtBtnLogin);
        etReferalCode= (EditText)findViewById(R.id.etReferalCode);

        init();

        txtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processValidateData();
            }
        });


    }

    private void init(){
        Typeface tf = PrefUtils.getTypeFaceCalibri(RegistrationScreen.this);
        etEmail.setTypeface(tf);
        etName.setTypeface(tf);
        etPassword.setTypeface(tf);
        etCnfPassword.setTypeface(tf);
        etPhone.setTypeface(tf);
        etReferalCode.setTypeface(tf);
        txtBtnLogin.setTypeface(tf);

    }

private void processValidateData(){

        if(isEdiTextEmpty(etEmail)){
            etEmail.setError("Please Enter Email  !!!");
        }else if(isEdiTextEmpty(etName)){
            etName.setError("Please Enter Name !!!");
        }else if(isEdiTextEmpty(etPassword)){
            etPassword.setError("Please Enter Password !!!");
        }else if(isEdiTextEmpty(etCnfPassword)){
            etCnfPassword.setError("Please Enter Confirm Password !!!");
        }else if(isEdiTextEmpty(etPhone)){
            etPhone.setError("Please Enter Mobile no. !!!");
        }else if(!isEmailMatch(etEmail)){
            Toast.makeText(RegistrationScreen.this,"Please Enter Valid Email address !!!",Toast.LENGTH_LONG).show();
        }else if (!isPasswordMatch(etPassword, etCnfPassword)) {
            Toast.makeText(RegistrationScreen.this,"Password do not macth !!!",Toast.LENGTH_LONG).show();
        }else{
            processRegister();
        }

    }


private void processRegister(){



    try{
        JSONObject userobj = new JSONObject();
        userobj.put("EmailId",etEmail.getText().toString().trim());
        userobj.put("Image","");
        userobj.put("Name",etName.getText().toString().trim());
        userobj.put("Password",etCnfPassword.getText().toString().trim());
        userobj.put("Phone",etPhone.getText().toString().trim());
        userobj.put("ReferanceCode",etReferalCode.getText().toString().trim());
        userobj.put("UserId",0);

        Log.e("Req Resgister",userobj.toString());

        dialog= new CircleDialog(RegistrationScreen.this,0);
        dialog.setCancelable(false);
        dialog.show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_REGISTERATION, userobj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jobj) {
                dialog.dismiss();
                String response = jobj.toString();
                Log.e("Response Register: ", "" + response);

                try{

                    JSONObject obj = new JSONObject(response);

                    if(obj.getString("Response").equalsIgnoreCase("0")){

                      /*  User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(SignUpActivity.this, "user_pref",0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();
*/

                        Toast.makeText(RegistrationScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                        Intent iCOnfirmSignUp = new Intent( RegistrationScreen.this ,LoginScreen.class );
                        startActivity(iCOnfirmSignUp);
                        finish();
                    }

                    else {
                        Toast.makeText(RegistrationScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                      }

                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                Log.e("error : ", error + "");
                Toast.makeText(RegistrationScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        MyApplication.getInstance().addToRequestQueue(req);

    }catch(Exception e){

    }


}


    public boolean isPasswordMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
    }

    public boolean isEmailMatch(EditText param1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }


    public boolean isEdiTextEmpty(EditText et){

        boolean isEmpty = false;

        if(et.getText() == null || et.getText().toString().equalsIgnoreCase("")){
            isEmpty = true;
        }

        return isEmpty;

    }

//end of main class
}
