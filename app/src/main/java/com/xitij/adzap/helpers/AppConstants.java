package com.xitij.adzap.helpers;

/**
 * Created by Krishna on 19-04-2015.
 */
public class AppConstants {

    // Base url for the webservice


    // 1 Rs = 15 Coins
    public static int coinRate = 15;


    public static String BASE_URL_PROFILE_IMAGE = "http://www.johnsite.com.accu17.com/adzapp/ProfileImages/";

    public static String BASE_URL_IMAGE = "http://www.johnsite.com.accu17.com/adzapp/images/";
    public static String BASE_URL_VIDEO = "http://johnsite.com.accu17.com/adzapp/video/";

    public static String BASE_URL = "http://johnsite.com.accu17.com/RewardMe/";

    public static String USER_LOGIN = BASE_URL + "Login.svc/json/CheckLogin/";
    public static String USER_REGISTERATION = BASE_URL + "Login.svc/json/RegisterUser";


    public static String VIEW_PROFILE = BASE_URL + "Login.svc/json/ViewProfile/";


    public static String CHECK_BALANCE  = BASE_URL + "ViewAdz.svc/json/CheckBalance/";

    public static String GET_OFFERS = BASE_URL + "ViewAdz.svc/json/GetOffers/";
    public static String REQUEST_POINTS = BASE_URL + "ViewAdz.svc/json/UpdateWallet";


    public static String GET_AD_IMAGES= BASE_URL + "ViewAdz.svc/json/GetADzImages/";

    public static String UPDATE_PROFILE= BASE_URL + "Login.svc/json/UpDateProfile";

    public static String VIEW_HISTORY = BASE_URL + "ViewAdz.svc/json/ViewHistory/";

    public static String VIEW_INVOICE = BASE_URL + "Invoice.svc/json/ViewInvoice/";
    public static String VIEW_BANK_DETAILS= BASE_URL + "Invoice.svc/json/ViewBanks/";

    public static String GENERATE_INVOICE = BASE_URL + "Invoice.svc/json/GenerateInvoice";
    public static String SAVE_BANK_DETAILS= BASE_URL + "Invoice.svc/json/SaveBankInfo";
    public static String UPDATE_BANK_DETAILS= BASE_URL + "Invoice.svc/json/UpdateBank";

    public static String RESEND_VERIFICATION_CODE= BASE_URL + "Login.svc/json/ResendEmail/";

    public static String GET_CITY_STATE_LIST= BASE_URL + "ViewAdz.svc/json/GetStateAndCityList";


    /*********  FTP IP ***********//*
    public static final String ftpPath="192.168.1.4";

    *//*********  FTP USERNAME ***********//*
    public static final String ftpUsername="androidftp";

    *//*********  FTP PASSWORD ***********//*
    public static final String ftpPassword="1234567890";*/

//
//    /*********  FTP IP ***********/
   public static final String ftpPath="173.248.137.197";
//    /*********  FTP USERNAME ***********/
   public static final String ftpUsername="johnsite";
//    /*********  FTP PASSWORD ***********/
   public static final String ftpPassword="C0a7eNb90r";
    /*********  FTP image download ***********/
    public static final String fileDownloadPath="http://ws-srv-net.in.webmyne.com/applications/Android/RiteWayServices/Images/";
}
