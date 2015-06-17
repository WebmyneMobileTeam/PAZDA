package com.xitij.adzap.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {


    public static void setCity(Context ctx,String city){
        Prefs.with(ctx).save("city",city);
    }

    public static void setState(Context ctx,String state){
        Prefs.with(ctx).save("state",state);
    }

    public static String getCity(Context ctx){
        String city = Prefs.with(ctx).getString("city", "");
        return city;
    }

    public static String getState(Context ctx){
        String state = Prefs.with(ctx).getString("state","");
        return state;
    }


    public static boolean isLogin(Context ctx){
        boolean isLogin = true;
        int selectedLanguage = Prefs.with(ctx).getInt("login", 0);
        if(selectedLanguage == 1){
            isLogin = true;
        }else{
            isLogin = false;
        }
        return isLogin;
    }

    public static void setLogin(Context ctx, boolean isLogin){

        int set = 0;
        if(isLogin == true){
            set = 1;
        }else{
            set = 0;
        }
        Prefs.with(ctx).save("login",set);
    }

    public static void setRecentcoins(Context ctx, String coinValue){
        Prefs.with(ctx).save("recentCoins",coinValue);
    }

    public static String getRecentcoins(Context ctx){
        String coins = Prefs.with(ctx).getString("recentCoins","0");
        return coins;
    }



    public static void setPendingEmailVerification(Context ctx, boolean status){
        Prefs.with(ctx).save("emailstatus",status);
    }

    public static boolean isPendingEmailVerification(Context ctx){
        boolean status = Prefs.with(ctx).getBoolean("emailstatus", true);
        return status;
    }


    public static void setReferenceCode(Context ctx, String coinValue){
        Prefs.with(ctx).save("referenceCode",coinValue);
    }

    public static String getReferenceCode(Context ctx){
        String code = Prefs.with(ctx).getString("referenceCode", "123ABC");
        return code;
    }





    public static void setBankListPos(Context ctx, int posValue){
        Prefs.with(ctx).save("banklistpos",posValue);
    }

    public static int getBankListPos(Context ctx){
        int pos = Prefs.with(ctx).getInt("banklistpos", 0);
        return pos;
    }

    public static void setPositionForWallpaper(Context ctx, int pos){
        Prefs.with(ctx).save("posWallpaperCounter",pos);
    }

    public static int gettPositionForWallpaper(Context ctx){
        int pos = Prefs.with(ctx).getInt("posWallpaperCounter",0);
        return pos;
    }


    public static void setChangeBackground(Context ctx, boolean Value){
        Prefs.with(ctx).save("isChnageBackground",Value);
    }

    public static boolean getChangeBackground(Context ctx){
        boolean value = Prefs.with(ctx).getBoolean("isChnageBackground", false);
        return value;
    }

    public static void setLocalScreenBackground(Context ctx, boolean Value){
        Prefs.with(ctx).save("isLockScreenBackground",Value);
    }

    public static boolean getLocalScreenBackground(Context ctx){
        boolean value = Prefs.with(ctx).getBoolean("isLockScreenBackground",false);
        return value;
    }


    public static boolean isLightTheme(Context ctx){
        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme", 0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }
        return isLight;
    }

    public static String getBackGroudColor(Context ctx){

        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }

        String background = "";

        if(isLight == true){

            background = "#ffffff";
        }else{
            background = "#252525";
        }

        return background;

    }

    public static String getBackGroudColorText(Context ctx){

        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
        if(selectedLanguage == 0){
            isLight = true;
        }else{
            isLight = false;
        }

        String background = "";

        if(isLight == true){

            background = "#252525";
        }else{
            background = "#ffffff";
        }

        return background;

    }

    public static int getMessageFontSize(Context ctx){
        String selectedFontSize = Prefs.with(ctx).getString("selected_font_size", "18");
        int size = Integer.parseInt(selectedFontSize);
        return size;
    }

    public static Typeface getTypeFaceCapsuula(Context ctx){

        Typeface typeface = null;
        typeface = Typeface.createFromAsset(ctx.getAssets(),"Capsuula.ttf");

        return  typeface;

    }

    public static Typeface getTypeFaceCalibri(Context ctx){

        Typeface typeface = null;
        typeface = Typeface.createFromAsset(ctx.getAssets(),"calibri.ttf");

        return  typeface;

    }

    public static void setLockImageAdID(Context ctx,String id){
        Prefs.with(ctx).save("LockImageID",id);
    }

    public static void setLockImageAdCoins(Context ctx,String coin){
        Prefs.with(ctx).save("LockImageCoin",coin);
    }

    public static void setWallpaperImageAdID(Context ctx,String id){
        Prefs.with(ctx).save("WallpaperImageID",id);
    }

    public static void setWallpaperImageAdCoins(Context ctx,String coin){
        Prefs.with(ctx).save("WallpaperImageCoin",coin);
    }


    public static String getLockImageAdID(Context ctx){
        String value = Prefs.with(ctx).getString("LockImageID", "");
        return value;
    }
    public static String getLockImageAdCoins(Context ctx){
        String value = Prefs.with(ctx).getString("LockImageCoin", "");
        return value;
    }

    public static String getWallpaperImageAdID(Context ctx){
        String value = Prefs.with(ctx).getString("WallpaperImageID", "");
        return value;
    }
    public static String getWallpaperImageAdCoins(Context ctx){
        String value = Prefs.with(ctx).getString("WallpaperImageCoin", "");
        return value;
    }

    public static void setcityID(Context ctx,String id){
        Prefs.with(ctx).save("cityID",id);
    }

    public static String getcityID(Context ctx){
        String value = Prefs.with(ctx).getString("cityID", "");
        return value;
    }
}
