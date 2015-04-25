package com.xitij.adzap.model;

/**
 * Created by Krishna on 19-04-2015.
 */
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OffersItems {

    @SerializedName("AdId")
    public int AdId;

    @SerializedName("Coins")
    public String Coins;

    @SerializedName("Description")
    public String Description;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("Icon")
    public String Icon;

    @SerializedName("ImagePath")
    public String ImagePath;

    @SerializedName("VideoPath")
    public String VideoPath;
}
