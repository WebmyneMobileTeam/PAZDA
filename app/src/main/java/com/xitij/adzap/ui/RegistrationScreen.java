package com.xitij.adzap.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.xitij.adzap.model.CITYSTATELIST;
import com.xitij.adzap.model.Cities;
import com.xitij.adzap.model.GeoLocation;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.graphics.Color.parseColor;

public class RegistrationScreen extends ActionBarActivity {
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private CircleDialog dialog;
    private EditText etEmail,etName,etPassword,etCnfPassword,etPhone,etReferalCode,etDateofBirth;
    private TextView txtBtnLogin;
    RadioButton rdMale,rdFemale;
    Spinner spState,spCity;
    String gender="Male";
    int statePOS,cityPOS;
    CITYSTATELIST cityobj;
    private static final String[] accType = new String[] {
            "Select", "Checking","Savings"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        spState = (Spinner)findViewById(R.id.spState);
        spCity = (Spinner)findViewById(R.id.spCity);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etName = (EditText)findViewById(R.id.etName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etCnfPassword = (EditText)findViewById(R.id.etCnfPassword);
        etPhone = (EditText)findViewById(R.id.etPhone);
        txtBtnLogin = (TextView)findViewById(R.id.txtBtnLogin);
        etReferalCode= (EditText)findViewById(R.id.etReferalCode);
        etDateofBirth= (EditText)findViewById(R.id.etDateofBirth);

        rdMale= (RadioButton)findViewById(R.id.rdMale);
        rdFemale= (RadioButton)findViewById(R.id.rdFemale);
        //init();


        fetchCityState();



        txtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processValidateData();
            }
        });


        rdMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdMale.isChecked())
                    gender = "Male";
                else
                    gender = "Female";
            }
        });

        rdFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(rdFemale.isChecked())
                    gender = "Female";
                else
                    gender = "Male";
            }
        });

        etDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(RegistrationScreen.this, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDateofBirth.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                fromDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                fromDatePickerDialog.show();
            }
        });


        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            statePOS = position;
                ArrayList<Cities> cityArrayList = cityobj.Sate.get(position).Cities;
                CitySpinnerAdapter adp2 = new CitySpinnerAdapter(RegistrationScreen.this,cityArrayList);
                spCity.setAdapter(adp2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                cityPOS = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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







    void fetchCityState(){

        final  ProgressDialog dialog = new ProgressDialog(RegistrationScreen.this);
        dialog.setMessage("Loading details...");
        dialog.setCancelable(true);
        dialog.show();

        new CallWebService(AppConstants.GET_CITY_STATE_LIST , CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                dialog.dismiss();
                Log.e("response loc", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("ResponseId").equalsIgnoreCase("1")) {

                        cityobj = new GsonBuilder().create().fromJson(response, CITYSTATELIST.class);

                        StateSpinnerAdapter adp =  new StateSpinnerAdapter(RegistrationScreen.this,cityobj);
                        spState.setAdapter(adp);



                    }

                } catch (Exception e) {
                    Log.e("exc in city fetching",e.toString());
                }


            }

            @Override
            public void error(VolleyError error) {
                dialog.dismiss();
                Log.e("volly er", error.toString());
            }
        }.start();


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
        }else if(isEdiTextEmpty(etDateofBirth)){
            etDateofBirth.setError("Please Enter Birthday !!!");
        } else if(isEdiTextEmpty(etPhone)){
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
        userobj.put("Birthdate",etDateofBirth.getText().toString().trim());
        userobj.put("EmailId", etEmail.getText().toString().trim());
        userobj.put("Gender",gender);
        userobj.put("Image","");
        userobj.put("Name",etName.getText().toString().trim());
        userobj.put("Password",etCnfPassword.getText().toString().trim());
        userobj.put("Phone",etPhone.getText().toString().trim());
        userobj.put("ReferanceCode",etReferalCode.getText().toString().trim());
        userobj.put("UserId",0);
        userobj.put("Stateid", cityobj.Sate.get(statePOS).StateId);
        userobj.put("CityId",cityobj.Sate.get(statePOS).Cities.get(cityPOS).CityId);


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

                        User currentUser = new GsonBuilder().create().fromJson(response,User.class);
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(RegistrationScreen.this, "user_pref",0);
                        complexPreferences.putObject("current_user", currentUser);
                        complexPreferences.commit();

                        Toast.makeText(RegistrationScreen.this,"Verification code sent to your registered email id",Toast.LENGTH_LONG).show();

                        PrefUtils.setPendingEmailVerification(RegistrationScreen.this, true);


                        GeoLocation gl = new GeoLocation();
                        gl.cityID = String.valueOf(cityobj.Sate.get(statePOS).Cities.get(cityPOS).CityId);
                        gl.cityName = cityobj.Sate.get(statePOS).Cities.get(cityPOS).CityName;

                        gl.stateName = cityobj.Sate.get(statePOS).StateName;
                        gl.stateID = String.valueOf(cityobj.Sate.get(statePOS).StateId);

                        Log.e("insided if", "" + "if");

                        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(RegistrationScreen.this, "user_pref", 0);
                        complexPreferences2.putObject("current_location", gl);
                        complexPreferences2.commit();



                        String temp = String.valueOf(currentUser.CityId);
                        PrefUtils.setcityID(RegistrationScreen.this, temp);

                        Log.e("#####City id ", temp);


                        Intent iCOnfirmSignUp = new Intent( RegistrationScreen.this ,EmailVerifcation.class );
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


    public class StateSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;
        CITYSTATELIST values;

        public StateSpinnerAdapter(Context context,  CITYSTATELIST obj) {
            this.values = obj;
            activity = context;
        }

        public int getCount() {
            return values.Sate.size();
        }

        public Object getItem(int i) {
            return values.Sate.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(RegistrationScreen.this);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.Sate.get(position).StateName);
            txt.setTextColor(parseColor("#000000"));
            return txt;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewgroup) {

            TextView txt = new TextView(RegistrationScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setText(values.Sate.get(position).StateName);
            txt.setTextColor(parseColor("#000000"));
            return txt;
        }
    }


    public class CitySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;
        ArrayList<Cities> values;

        public CitySpinnerAdapter(Context context,  ArrayList<Cities> obj) {
            this.values = obj;
            activity = context;
        }

        public int getCount() {
            return values.size();
        }

        public Object getItem(int i) {
            return values.get(i).CityName;
        }

        public long getItemId(int i) {
            return (long) i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(RegistrationScreen.this);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).CityName);
            txt.setTextColor(parseColor("#000000"));
            return txt;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewgroup) {

            TextView txt = new TextView(RegistrationScreen.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(getResources().getDimension(R.dimen.spinner_text));
            txt.setText(values.get(position).CityName);
            txt.setTextColor(parseColor("#000000"));
            return txt;
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
