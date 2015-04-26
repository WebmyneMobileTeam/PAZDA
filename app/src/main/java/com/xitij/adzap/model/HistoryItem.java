package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

public class HistoryItem {

    @SerializedName("ClosingBal")
    public String ClosingBal;

    @SerializedName("CreaditedBal")
    public String CreaditedBal;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("LogId")
    public long LogId;

    @SerializedName("TransDate")
    public String TransDate;


}
