package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckBalance {


   @SerializedName("ClosingBal")
    public String ClosingBal;


    @SerializedName("CreaditedBal")
    public String CreaditedBal;


    @SerializedName("OpeningBal")
    public String OpeningBal;


    @SerializedName("DebitedBal")
    public String DebitedBal;


    @SerializedName("LogId")
    public long LogId;
}
