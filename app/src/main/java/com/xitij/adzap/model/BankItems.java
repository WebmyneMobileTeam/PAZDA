package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

public class BankItems {

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


    @SerializedName("BankBranch")
    public String BankBranch;

    @SerializedName("BankId")
    public long BankId;


    @SerializedName("BankName")
    public String BankName;

    @SerializedName("CustId")
    public long CustId;


}
