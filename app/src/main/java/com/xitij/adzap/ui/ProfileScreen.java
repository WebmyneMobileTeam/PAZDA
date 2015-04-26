package com.xitij.adzap.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xitij.adzap.helpers.ComplexPreferences;
import com.xitij.adzap.helpers.PrefUtils;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.BadgeView;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

public class ProfileScreen extends ActionBarActivity {
    private  ImageView imgProfile;
    private CircleDialog dialog;
    private EditText etPassword,etName,etCnfPassword,etPhone,etReferalCode;
    private TextView txtBtnLogin,txtEmail,txtName;
    private boolean isNewPassword=false;
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);

        etName = (EditText)findViewById(R.id.etName);
        txtEmail = (TextView)findViewById(R.id.txtEmail);


        txtName = (TextView)findViewById(R.id.txtName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etCnfPassword = (EditText)findViewById(R.id.etCnfPassword);
        etPhone = (EditText)findViewById(R.id.etPhone);
        txtBtnLogin = (TextView)findViewById(R.id.txtBtnLogin);


        init();
        txtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processValidateData();
            }
        });

    }

private void processValidateData() {
    if (isEdiTextEmpty(etName)) {
        etName.setError("Please Enter Name !!!");
    } else if (isEdiTextEmpty(etPhone)) {
        etPhone.setError("Please Enter Mobile no. !!!");
    }  else {

                if(etPassword.length()!=0 || etCnfPassword.length()!=0){
                    isNewPassword=false;
                }else{
                    if (isEdiTextEmpty(etPassword)) {
                        etPassword.setError("Please Enter Password !!!");
                    } else if (isEdiTextEmpty(etCnfPassword)) {
                        etCnfPassword.setError("Please Enter Confirm Password !!!");
                    } else if (!isPasswordMatch(etPassword, etCnfPassword)) {
                        Toast.makeText(ProfileScreen.this, "Password do not macth !!!", Toast.LENGTH_LONG).show();
                    }

                    isNewPassword=true;
                }
                processRegister();
    }
}



    private void init(){
            Typeface tf = PrefUtils.getTypeFaceCalibri(ProfileScreen.this);
        txtEmail.setTypeface(tf);
        etName.setTypeface(tf);
        etPassword.setTypeface(tf);
        etCnfPassword.setTypeface(tf);
        etPhone.setTypeface(tf);
        txtName.setTypeface(tf);
        txtBtnLogin.setTypeface(tf);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref", 0);
        currentUser = complexPreferences.getObject("current_user", User.class);

        fillDetails();


    }

    private void fillDetails(){
        txtName.setText(currentUser.Name);
        txtEmail.setText(currentUser.EmailId);
        etName.setText(currentUser.Name);
        etPhone.setText(currentUser.Phone);
    }

    private void processRegister(){

        try{
            JSONObject userobj = new JSONObject();
            //userobj.put("EmailId",currentUser.EmailId);
            userobj.put("Image","");
            userobj.put("Name",etName.getText().toString().trim());

            if(isNewPassword){
                userobj.put("Password",etCnfPassword.getText().toString().trim());
            }else {
                userobj.put("Password",currentUser.Password.toString().trim());
            }


            userobj.put("Phone",etPhone.getText().toString().trim());
            userobj.put("ReferanceCode",currentUser.ReferanceCode);
            userobj.put("UserId",currentUser.UserId);

            Log.e("Req profile", userobj.toString());

            dialog= new CircleDialog(ProfileScreen.this,0);
            dialog.setCancelable(false);
            dialog.show();

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.USER_REGISTERATION, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {
                    dialog.dismiss();
                    String response = jobj.toString();
                    Log.e("Response profile: ", "" + response);

                    try{

                        JSONObject obj = new JSONObject(response);

                        if(obj.getString("Response").equalsIgnoreCase("1")){

                        User currentUser2 = new GsonBuilder().create().fromJson(response,User.class);
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ProfileScreen.this, "user_pref",0);
                        complexPreferences.putObject("current_user", currentUser2);
                        complexPreferences.commit();


                            Toast.makeText(ProfileScreen.this,obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();

                            Intent iCOnfirmSignUp = new Intent( ProfileScreen.this ,HomeScreen.class );
                            startActivity(iCOnfirmSignUp);
                            finish();
                        }

                        else {
                            Toast.makeText(ProfileScreen.this,"Error - "+obj.getString("ResponseMsg").toString(),Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    Log.e("error : ", error + "");
                    Toast.makeText(ProfileScreen.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public boolean isPasswordMatch(EditText param1, EditText param2) {
        boolean isMatch = false;
        if (param1.getText().toString().equals(param2.getText().toString())) {
            isMatch = true;
        }
        return isMatch;
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
