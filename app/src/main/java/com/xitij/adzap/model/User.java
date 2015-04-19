package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;
public class User {

    @SerializedName("Balance")
    public String Balance;

    @SerializedName("EmailId")
    public String EmailId;

    @SerializedName("Image")
    public String Image;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Phone")
    public String Phone;


    @SerializedName("UserId")
    public long UserId;
}
