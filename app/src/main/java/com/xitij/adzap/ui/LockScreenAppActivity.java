package com.xitij.adzap.ui;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
import com.xitij.adzap.model.AdImageList;
import com.xitij.adzap.model.GeoLocation;
import com.xitij.adzap.model.User;
import com.xitij.adzap.widget.CircleDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LockScreenAppActivity extends Activity {
    boolean isImageSucessfullyLoaded=false;
    /** Called when the activity is first created. */
    AdImageList adImageList;
	  KeyguardManager.KeyguardLock k1;
	   boolean inDragMode;
 	   int selectedImageViewX;
 	   int selectedImageViewY;
 	   int windowwidth;
 	   int windowheight;
       String saveImagePath;
 	   ImageView droid,phone,home;
 	  //int phone_x,phone_y;
 	   int home_x,home_y;
 	   int[] droidpos;
    private RelativeLayout relativeMain;
    private ImageView imgLock;
    public int counter = 0;

 	  private LayoutParams layoutParams;

	/* @Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG| WindowManager.LayoutParams.FLAG_FULLSCREEN);

          super.onAttachedToWindow();
	 }*/
/*@Override
protected void onNewIntent(Intent intent) {
	// TODO Auto-generated method stub
	super.onNewIntent(intent);
	 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);

}*/

    @Override
    protected void onResume() {
        super.onResume();
        loadImageFromStorage("/data/data/com.xitij.adzap/app_ADZAP");


        processloadImageLists();
    }

    public void onCreate(Bundle savedInstanceState) {

    	   super.onCreate(savedInstanceState);
    	   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_FULLSCREEN);

                

    	   setContentView(R.layout.main);
    	   droid =(ImageView)findViewById(R.id.droid);
           relativeMain=(RelativeLayout)findViewById(R.id.relativeMain);
           imgLock = (ImageView)findViewById(R.id.imgLock);



    	   System.out.println("measured width"+droid.getMeasuredWidth());
    	   System.out.println(" width"+droid.getWidth());


    	   if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
    	      // Toast.makeText(this, "" + "kill activityy", Toast.LENGTH_SHORT).show();
    	        	finish();
    	    	}

        try{
     // initialize receiver


        startService(new Intent(this,MyService.class));




  /*      KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        k1 = km.newKeyguardLock("IN");
        k1.disableKeyguard();*/
        StateListener phoneStateListener = new StateListener();
        TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        windowwidth=getWindowManager().getDefaultDisplay().getWidth();
        System.out.println("windowwidth"+windowwidth);
        windowheight=getWindowManager().getDefaultDisplay().getHeight();
        System.out.println("windowheight"+windowheight);

        MarginLayoutParams marginParams2 = new MarginLayoutParams(droid.getLayoutParams());

        marginParams2.setMargins((windowwidth/24)*10,((windowheight/32)*8),0,0);

        //marginParams2.setMargins(((windowwidth-droid.getWidth())/2),((windowheight/32)*8),0,0);
        RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

        droid.setLayoutParams(layoutdroid);

        /* phone =(ImageView)findViewById(R.id.phone);
        MarginLayoutParams marginParams = new MarginLayoutParams(phone.getLayoutParams());
         marginParams.setMargins(0,windowheight/32,windowwidth/24,0);
         LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(marginParams);
         phone.setLayoutParams(layoutParams1);
*/

         LinearLayout homelinear = (LinearLayout)findViewById(R.id.homelinearlayout);
        // homelinear.setPadding(0,0,0,(windowheight/32)*3);
         home =(ImageView)findViewById(R.id.home);

         MarginLayoutParams marginParams1 = new MarginLayoutParams(home.getLayoutParams());

         marginParams1.setMargins((windowwidth/24)*10,0,(windowheight/32)*8,0);
        // marginParams1.setMargins(((windowwidth-home.getWidth())/2),0,(windowheight/32)*10,0);
         LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(marginParams1);

         home.setLayoutParams(layout);




         droid.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				   layoutParams = (LayoutParams) v.getLayoutParams();

				switch(event.getAction())
		         {

		         case MotionEvent.ACTION_DOWN:
		        	 int[] hompos=new int[2];
		        	// int[] phonepos=new int[2];
		        	 droidpos=new int[2];
			         //phone.getLocationOnScreen(phonepos);
			         home.getLocationOnScreen(hompos);
			         home_x=hompos[0];
			         home_y=hompos[1];
			       //  phone_x=phonepos[0];
			        // phone_y=phonepos[1];


		        	 break;
		         case MotionEvent.ACTION_MOVE:
		             int x_cord = (int)event.getRawX();
		             int y_cord = (int)event.getRawY();

		             if(x_cord>windowwidth-(windowwidth/24)){x_cord=windowwidth-(windowwidth/24)*2;}
		             if(y_cord>windowheight-(windowheight/32)){y_cord=windowheight-(windowheight/32)*2;}

		             layoutParams.leftMargin = x_cord ;
		             layoutParams.topMargin = y_cord;

		             droid.getLocationOnScreen(droidpos);
		             v.setLayoutParams(layoutParams);

		             if(((x_cord-home_x)<=(windowwidth/24)*5 && (home_x-x_cord)<=(windowwidth/24)*4)&&((home_y-y_cord)<=(windowheight/32)*5))
		              {
		                 System.out.println("home overlapps");
		                 System.out.println("homeee" + home_x + "  " + (int) event.getRawX() + "  " + x_cord + " " + droidpos[0]);

		            	 System.out.println("homeee" + home_y + "  " + (int) event.getRawY() + "  " + y_cord + " " + droidpos[1]);

		            	 v.setVisibility(View.GONE);




                          try {
                                if (adImageList.Img.size() != 0) {
                                    if (isImageSucessfullyLoaded) {
                                        processEarnCoins();
                                    }
                                }

                                //Toast.makeText(LockScreenAppActivity.this, "Screen on", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                Log.e("exc unlock",e.toString());
                            }

		            	// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
		                finish();
		              }

		             else{
		            	 System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidpos[0]);

		            	 System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidpos[1]);


		            	 System.out.println("home notttt overlapps");
		             }
		             /* if(((x_cord-phone_x)>=128 && (x_cord-phone_x)<=171 )&&((phone_y-y_cord)<=10))
		              {
		            	  System.out.println("phone overlapps");
                       finish();
		              }
		              else{
		            	     System.out.println(phone_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidpos[0]);

			            	 System.out.println(phone_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidpos[1]);


			            	 System.out.println("phone not overlapps" +
			            	 		" overlapps");
		              }*/
		            // v.invalidate();




		             break;
		         case MotionEvent.ACTION_UP:


		        	    int x_cord1 = (int)event.getRawX();
			             int y_cord2 = (int)event.getRawY();

			             if(((x_cord1-home_x)<=(windowwidth/24)*5 && (home_x-x_cord1)<=(windowwidth/24)*4)&&((home_y-y_cord2)<=(windowheight/32)*5))
			              {
			                 System.out.println("home overlapps");
			                 System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord1+" "+droidpos[0]);

			            	 System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord2+" "+droidpos[1]);

			            	// startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
			               //finish();
			              }
			             else{

			            	 layoutParams.leftMargin = (windowwidth/24)*10;
				             layoutParams.topMargin = (windowheight/32)*8;
				             v.setLayoutParams(layoutParams);


			             }




		         }

				return true;
			}
		});




/*
        Button close =(Button)findViewById(R.id.lockk);
        close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	//k1.reenableKeyguard();
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));


                finish();
            }
        });
*/



       //PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

      // PowerManager.WakeLock w1 =pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.FULL_WAKE_LOCK,"MyApp");
      // w1.acquire();
      // w1.release();
        }catch (Exception e) {
			// TODO: handle exception
		}

    }

    void processEarnCoins(){
        //store current user and domain in shared preferences
        ComplexPreferences complexPreferences2 = ComplexPreferences.getComplexPreferences(LockScreenAppActivity.this, "user_pref", 0);
        User currentUser = complexPreferences2.getObject("current_user", User.class);

        try{
            JSONObject userobj = new JSONObject();

            String Adid = PrefUtils.getLockImageAdID(LockScreenAppActivity.this);
            String coins = PrefUtils.getLockImageAdCoins(LockScreenAppActivity.this);


            userobj.put("AdId", Adid);
            userobj.put("UserId",String.valueOf(currentUser.UserId));
            userobj.put("Coins", coins);
            userobj.put("IsImage", 1);
            Log.e("Req earn coins", userobj.toString());



            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.REQUEST_POINTS, userobj, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jobj) {

                    String response = jobj.toString();
                    Log.e("Response  earn coins: ", "" + response);




                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {


                    Log.e("error : ", error + "");
                    Toast.makeText(LockScreenAppActivity.this,"Error - "+error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            MyApplication.getInstance().addToRequestQueue(req);

        }catch(Exception e){

        }
    }

    private void processloadImageLists(){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LockScreenAppActivity.this, "user_pref", 0);
        User currentUser = complexPreferences.getObject("current_user", User.class);

        String cityid = PrefUtils.getcityID(LockScreenAppActivity.this);


        Log.e("#####City id ",cityid);
        Log.e("request - ",AppConstants.GET_AD_IMAGES + currentUser.UserId+ currentUser.UserId+"/"+cityid);


        new CallWebService(AppConstants.GET_AD_IMAGES + currentUser.UserId+ currentUser.UserId+"/"+cityid, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {

                Log.e("response", response.toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("Response").equalsIgnoreCase("0")) {
                        adImageList = new GsonBuilder().create().fromJson(response, AdImageList.class);


                        counter = PrefUtils.gettPositionForWallpaper(LockScreenAppActivity.this);

                        if(counter == adImageList.Img.size()){
                            counter=0;
                        }

                        Log.e("Image Path",""+adImageList.Img.get(counter).ImgPath.toString());

                        PrefUtils.setLockImageAdID(LockScreenAppActivity.this, String.valueOf(adImageList.Img.get(counter).AdId));
                        PrefUtils.setLockImageAdCoins(LockScreenAppActivity.this, adImageList.Img.get(counter).Coins);

                        Log.e("Image ADid", "" + adImageList.Img.get(counter).AdId);
                        Log.e("Image Coin",""+adImageList.Img.get(counter).Coins);


                        String tempPath = adImageList.Img.get(counter).ImgPath.toString();
                        String subPath = tempPath.substring(tempPath.lastIndexOf("/")+1,tempPath.length());

                        Log.e("Sub Path",subPath);
                        String imagUrl=subPath;

                        myAsyncTask myWebFetch = new myAsyncTask(imagUrl,counter);
                        myWebFetch.execute();
                    } else {
                        //   Toast.makeText(HomeScreen.this, "Error - " + obj.getString("ResponseMsg").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("e", e.toString());
                }



            }

            @Override
            public void error(VolleyError error) {
                Log.e("volly er", error.toString());
            }
        }.start();
    }


    class myAsyncTask extends AsyncTask<Void, Void, Void> {


        int counter;
        String imageUrl;
        myAsyncTask(String uurl,int count)
        {
            this.counter = count;
            this.imageUrl = uurl;
            Log.e("inside","async task constructotr");

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e("inside","async task onpost");
            if(isImageSucessfullyLoaded){
                //processsetImage();
                loadImageFromStorage(saveImagePath);

            }else
                Toast.makeText(LockScreenAppActivity.this, "Image not found or network error", Toast.LENGTH_LONG).show();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("inside","async task pnpre");
        }

        protected Void doInBackground(Void... arg0) {
            Log.e("inside","async onbackground start");
            try {
                URL url = new URL(AppConstants.BASE_URL_IMAGE+imageUrl);
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                InputStream input = bufHttpEntity.getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();

                saveImagePath = saveToInternalSorage(bitmap);

                counter +=1;
                PrefUtils.setPositionForWallpaper(LockScreenAppActivity.this,counter);


                isImageSucessfullyLoaded = true;
                Log.e("inside","async onbackground end");
                //catch some possible errors...
            } catch (MalformedURLException e) {
                isImageSucessfullyLoaded=false;
                e.printStackTrace();

                Log.e("exc1",e.toString());
            } catch (IOException e) {
                isImageSucessfullyLoaded=false;

                e.printStackTrace();
                Log.e("exc2",e.toString());
            }catch (Exception e){
                isImageSucessfullyLoaded=false;

                Log.e("exc3",e.toString());
            }

            return null;
        }
    }

    private void loadImageFromStorage(String path)    {
        Log.e("main path of image",path);
        try {
            File f=new File(path, "ADZAPWallpaper.jpg");
            Bitmap wallpaper = BitmapFactory.decodeStream(new FileInputStream(f));


          //  WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                imgLock.setImageBitmap(wallpaper);
            }catch (Exception e){
                imgLock.setBackgroundResource(R.drawable.main_bg);
                Log.e("Image not found","excpetion raised");
            }

                   //   myWallpaperManager.setBitmap(wallpaper);
                //   Toast.makeText(context,"Sucessfully Wallpaper set",Toast.LENGTH_LONG).show();

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private String saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("ADZAP", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"ADZAPWallpaper.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }




    class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                	finish();



                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
    public void onSlideTouch( View view, MotionEvent event )
    {
    	 switch(event.getAction())
         {
         case MotionEvent.ACTION_DOWN:
        	 break;
         case MotionEvent.ACTION_MOVE:
             int x_cord = (int)event.getRawX();
             int y_cord = (int)event.getRawY();

             if(x_cord>windowwidth){x_cord=windowwidth;}
             if(y_cord>windowheight){y_cord=windowheight;}

             layoutParams.leftMargin = x_cord -25;
             layoutParams.topMargin = y_cord - 75;

             view.setLayoutParams(layoutParams);
             break;
         default:
             break;




         }

        //When the user pushes down on an ImageView
      /*  if ( event.getAction() == MotionEvent.ACTION_DOWN )
        {
           inDragMode = true; //Set a variable so we know we started draggin the imageView
           //Set the selected ImageView X and Y exact position
          selectedImageViewX = Math.abs((int)event.getRawX()-((ImageView)view).getLeft());
          selectedImageViewY = Math.abs((int)event.getRawY()-((ImageView)view).getTop());
           //Bring the imageView in front
           ((ImageView)view).bringToFront();
        }

        //When the user let's the ImageView go (raises finger)
        if ( event.getAction() == MotionEvent.ACTION_UP )
        {
           inDragMode = false; //Reset the variable which let's us know we're not in drag mode anymore
        }

        //When the user keeps his finger on te screen and drags it (slides it)
        if ( event.getAction() == MotionEvent.ACTION_MOVE )
        {
            //If we've started draggin the imageView
            if ( inDragMode )
            {
                //Get the imageView object
               // ImageView slide = (ImageView)findViewById(R.id.slide);
                //Get a parameters object (THIS EXAMPLE IS FOR A RELATIVE LAYOUT)
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
                //Change the position of the imageview accordingly
                params.setMargins((int)event.getRawX()-selectedImageViewX, (int)event.getRawY()-selectedImageViewY, 0, 0);
                //Set the new params
                view.setLayoutParams(params);

                //If we hit a limit with our imageView position
                /*if((int)event.getRawX())
                {
                    //Open another activity
                    Intent it = new Intent(Slide.this,NextActivity.class);
                    startActivity(it);
                }
            }
        }*/

    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
        return;
    }

    //only used in lockdown mode
    @Override
    protected void onPause() {
        super.onPause();

        // Don't hang around.
       // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Don't hang around.
       // finish();
    }





    @Override
  public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

    	if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_POWER)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)) {
    	    //this is where I can do my stuff
    	    return true; //because I handled the event
    	}
       if((keyCode == KeyEvent.KEYCODE_HOME)){

    	   return true;
        }

	return false;

    }

    public boolean dispatchKeyEvent(KeyEvent event) {
    	if (event.getKeyCode() == KeyEvent.KEYCODE_POWER ||(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)||(event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
    	    //Intent i = new Intent(this, NewActivity.class);
    	    //startActivity(i);
    	    return false;
    	}
    	 if((event.getKeyCode() == KeyEvent.KEYCODE_HOME)){

           System.out.println("alokkkkkkkkkkkkkkkkk");
      	   return true;
         }
    return false;
    }

	/*public void unloack(){

          finish();

	}*/
    public void onDestroy(){
       // k1.reenableKeyguard();

        super.onDestroy();
    }

}