package com.mpfarmer.facebookintegration.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class Picture implements Serializable {

    @SerializedName("is_silhouette") public boolean isSilhouette;

    @SerializedName("url") public String url;
}
