package com.xitij.adzap.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {

    public static boolean isLogin(Context ctx){
        boolean isLogin = true;
        int selectedLanguage = Prefs.with(ctx).getInt("login",0);
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


    public static boolean isLightTheme(Context ctx){
        boolean isLight = true;
        int selectedLanguage = Prefs.with(ctx).getInt("selected_theme",0);
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




}
