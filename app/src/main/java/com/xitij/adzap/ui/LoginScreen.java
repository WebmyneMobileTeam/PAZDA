package com.xitij.adzap.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginScreen extends ActionBarActivity {

    private CircleDialog dialog;
    private EditText etUname, etPassword;
    private TextView txtBtnLogin;
    private TextView txtLoginNotRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        etUname = (EditText) findViewById(R.id.etUname);
        etPassword = (EditText) findViewById(R.id.etPassword);
        txtBtnLogin = (TextView) findViewById(R.id.txtBtnLogin);

        txtLoginNotRegistered = (TextView)findViewById(R.id.txtLoginNotRegistered);

        txtLoginNotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReg = new Intent( LoginScreen.this ,RegistrationScreen.class );
                startActivity(iReg);
            }
        });

      //  etUname.setText("softeng.krishna@gmail.com");

        txtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processValidateData();
            }
        });

      //  init();

    }

    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(LoginScreen.this);
            etUname.setTypeface(tf);
            etPassword.setTypeface(tf);
            txtBtnLogin.setTypeface(tf);
    }

    private void processValidateData() {
        if (isEdiTextEmpty(etUname)) {
            etUname.setError("Please Enter User Email Id !!!");
        } else if (isEdiTextEmpty(etPassword)) {
            etPassword.setError("Please Enter Password !!!");
        } else if (!isEmailMatch(etUname)) {
            Toast.makeText(LoginScreen.this, "Please Enter Valid Email address !!!", Toast.LENGTH_LONG).show();
        } else {
            processLogin();
        }

    }


    private void processLogin() {
        dialog = new CircleDialog(LoginScreen.this, 0);
        dialog.setCancelable(false);
        dialog.show();


        String email = etUname.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        new CallWebService(AppConstants.USER_LOGIN + email + "/" + password, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {

                        User currentUser = new GsonBuilder().create().fromJson(response, User.class);
                        //store current user and domain in shared preferences
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();

                        PrefUtils.setLogin(LoginScreen.this,true);
                        PrefUtils.setReferenceCode(LoginScreen.this,currentUser.ReferanceCode);

                        Intent iHomeScreen = new Intent(LoginScreen.this,HomeScreen.class);
                        startActivity(iHomeScreen);
                        finish();



                    }else if((obj.getString("Response").equalsIgnoreCase("-1"))){


                        if((obj.getString("ResponseMsg").equalsIgnoreCase("InValid EMailId Or Password"))){

                            Toast.makeText(LoginScreen.this, "Invalid EmailId Or Password", Toast.LENGTH_LONG).show();

                        }
                       else{
                            Toast.makeText(LoginScreen.this, "Please verify your accounnt", Toast.LENGTH_LONG).show();

                            PrefUtils.setLogin(LoginScreen.this, false);

                            User currentUser = new GsonBuilder().create().fromJson(response, User.class);
                            //store current user and domain in shared preferences
                            ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginScreen.this, "user_pref", 0);
                            complexPreferences.putObject("current_user", currentUser);
                            complexPreferences.commit();


                            Intent iHomeScreen = new Intent(LoginScreen.this,EmailVerifcation.class);
                            startActivity(iHomeScreen);
                            finish();
                        }




                    } else {
                        Toast.makeText(LoginScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
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
