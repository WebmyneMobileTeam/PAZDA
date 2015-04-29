package com.xitij.adzap.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class ProfileScreen extends ActionBarActivity {
    private  ImageView imgProfile;
    private CircleDialog dialog;
    private EditText etPassword,etName,etCnfPassword,etPhone,etReferalCode;
    private TextView txtBtnLogin,txtEmail,txtName;
    private boolean isNewPassword=false;

    private static final int CAMERA_REQUEST = 500;
    private static final int GALLERY_REQUEST = 300;
    private static final int PICK_MEDIA_REQUEST_CODE=3;
    private static final int SHARE_MEDIA_REQUEST_CODE = 9;
    final CharSequence[] items = { "Take Photo", "Choose from Gallery" };
    User currentUser;

    boolean NEW_PROFILE_IMAGE=false;
    static File ProfileImagePath;
    static String ProfileImageName;


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
               // processValidateData();

                Log.e("image name -->",String.valueOf(ProfileImageName));
                Log.e("image path -->",String.valueOf(ProfileImagePath));

                    uploadFile(ProfileImagePath);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUploadImageDialog();
            }
        });

    }

private void processValidateData() {
    if (isEdiTextEmpty(etName)) {
        etName.setError("Please Enter Name !!!");
    } else if (isEdiTextEmpty(etPhone)) {
        etPhone.setError("Please Enter Mobile no. !!!");
    }  else {

                if(etPassword.length()==0 || etCnfPassword.length()==0){
                    isNewPassword=false;
                    processRegister();
                }else{
                    if (isEdiTextEmpty(etPassword)) {
                        etPassword.setError("Please Enter Password !!!");
                    } else if (isEdiTextEmpty(etCnfPassword)) {
                        etCnfPassword.setError("Please Enter Confirm Password !!!");
                    } else if (!isPasswordMatch(etPassword, etCnfPassword)) {
                        Toast.makeText(ProfileScreen.this, "Password do not macth !!!", Toast.LENGTH_LONG).show();
                    }else{
                        isNewPassword=true;
                        processRegister();
                    }


                }

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

    private void processUploadImageDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileScreen.this);
        builder.setTitle("Upload Profile Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, PICK_MEDIA_REQUEST_CODE);
                    Log.e("Camera ","exit");

                } else if (items[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , PICK_MEDIA_REQUEST_CODE);
                }
            }
        });
        builder.show();


    }

    public void uploadFile(final File fileName){
        dialog= new CircleDialog(ProfileScreen.this,0);
        dialog.setCancelable(false);
        dialog.show();


        Log.e("filename--->",fileName+"");
        final FTPClient client = new FTPClient();
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client.connect(AppConstants.ftpPath,21);
                    client.login(AppConstants.ftpUsername, AppConstants.ftpPassword);
                    client.setType(FTPClient.TYPE_AUTO);
                   // client.changeDirectory("/Images/");


                    Log.e("client connected","sucslfully");
                   // client.upload(fileName, new MyTransferListener());
                    Log.e("filename",fileName+"");
                } catch (Exception e) {
                    Log.e("err try1 ", e.toString());

                    try {
                        client.disconnect(true);
                    } catch (Exception e2) {
                        Log.e("err try2 ", e2.toString());
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
            }
        }.execute();
    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Titlekris", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }





    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {


            Log.e("filename","Upload Started ");
            // Transfer started
//                Toast.makeText(getActivity(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {
            System.out.println(" transferred ..." );
            Log.e("filename","transferred");
        }

        public void completed() {
            // Transfer completed
            System.out.println(" completed ..." );
            Log.e("filename", "upload completed");


          /*  getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackBar bar = new SnackBar(getActivity(),"Profile Image Update Sucessfully...");
                    bar.show();
                }
            });*/

        }

        public void aborted() {
            // Transfer aborted
            System.out.println(" transfer aborted ,please try again..." );
//                Toast.makeText(getActivity()," transfer aborted ,please try again...", Toast.LENGTH_SHORT).show();

        }

        public void failed() {
            // Transfer failed
            System.out.println(" failed ..." );

        }

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
            userobj.put("EmailId",currentUser.EmailId);
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

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.UPDATE_PROFILE, userobj, new Response.Listener<JSONObject>() {

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
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(ProfileScreen.this, bmp);
                ProfileImageName = String.valueOf(tempUri);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                ProfileImagePath = new File(getRealPathFromURI(tempUri));

                // Convert ByteArray to Bitmap::
                final Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                imgProfile.setBackground(null);
                imgProfile.setImageBitmap(bitmap);
                NEW_PROFILE_IMAGE=true;
            }
            else{
                Toast.makeText(ProfileScreen.this,"Image uploading failed from Internal Storage",Toast.LENGTH_LONG).show();
            }

        }
        else  if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                final Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                File file = new File(picturePath);
                ProfileImagePath = file;
                ProfileImageName= file.getName();
                imgProfile.setBackground(null);
                imgProfile.setImageBitmap(thumbnail);
                NEW_PROFILE_IMAGE=true;

            }
            else{
               Toast.makeText(ProfileScreen.this,"Image uploading failed from SD Card",Toast.LENGTH_LONG).show();
            }
        }

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
