package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BankList {

    @SerializedName("Bank")
    public ArrayList<BankItems> Bank;

}
