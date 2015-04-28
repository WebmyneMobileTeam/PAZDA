package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvoicesItems {

    @SerializedName("IFSCNo")
    public String IFSCNo;

    @SerializedName("ACNO")
    public String ACNO;

    @SerializedName("AccountPersonName")
    public String AccountPersonName;

    @SerializedName("AccountType")
    public String AccountType;

    @SerializedName("Address")
    public String Address;

    @SerializedName("Amount")
    public String Amount;

    @SerializedName("BankBranch")
    public String BankBranch;


    @SerializedName("BankName")
    public String BankName;

    @SerializedName("Coins")
    public String Coins;

    @SerializedName("CustId")
    public long CustId;

    @SerializedName("Date")
    public String Date;


    @SerializedName("Status")
    public String Status;


}
