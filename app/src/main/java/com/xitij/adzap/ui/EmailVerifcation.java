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

public class EmailVerifcation extends ActionBarActivity {

    private CircleDialog dialog;
    private EditText etCode;
    private TextView txtBtnVerify;
    private TextView txtResendCode,txtNewSignup;
    User currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        etCode = (EditText) findViewById(R.id.etCode);
        txtBtnVerify = (TextView) findViewById(R.id.txtBtnVerify);
        txtResendCode = (TextView)findViewById(R.id.txtResendCode);
        txtNewSignup= (TextView)findViewById(R.id.txtNewSignup);
    //  init();


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EmailVerifcation.this, "user_pref", 0);
        currentuser   = complexPreferences.getObject("current_user", User.class);


        txtNewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReg = new Intent( EmailVerifcation.this ,RegistrationScreen.class );
                startActivity(iReg);
                finish();
            }
        });


        txtResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processResendCode();
            }
        });

        txtBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdiTextEmpty(etCode)) {
                    Toast.makeText(EmailVerifcation.this, "Please enter Verification code !!!", Toast.LENGTH_LONG).show();
                }//else if(!etCode.getText().toString().trim().equalsIgnoreCase(currentuser.verifyCode)){
                else if(!etCode.getText().toString().trim().equalsIgnoreCase("k")){
                    Toast.makeText(EmailVerifcation.this, "Please enter Correct Verification code !!!", Toast.LENGTH_LONG).show();
                }
                else {

                    PrefUtils.setLogin(EmailVerifcation.this, true);
                    PrefUtils.setPendingEmailVerification(EmailVerifcation.this,false);

                    Intent iReg = new Intent(EmailVerifcation.this, HomeScreen.class);
                    startActivity(iReg);
                    finish();
                }
            }
        });

    }



    private void processResendCode() {
        dialog = new CircleDialog(EmailVerifcation.this, 0);
        dialog.setCancelable(false);
        dialog.show();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EmailVerifcation.this, "user_pref", 0);
        User currentuser   = complexPreferences.getObject("current_user", User.class);



        new CallWebService(AppConstants.RESEND_VERIFICATION_CODE +currentuser.UserId, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {

                        User currentUser = new GsonBuilder().create().fromJson(response, User.class);
                        //store current user and domain in shared preferences
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(EmailVerifcation.this, "user_pref", 0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();


                        Toast.makeText(EmailVerifcation.this,"Verification code sent to your registered email id",Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EmailVerifcation.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
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


    public boolean isEdiTextEmpty(EditText et) {

        boolean isEmpty = false;

        if (et.getText() == null || et.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }

        return isEmpty;

    }

//end of main class
}
