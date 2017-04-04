package com.mpfarmer.facebookintegration.model;

import java.io.Serializable;

/**
 * Created by mpfarmer on 4/3/2017.
 */

public class Friend implements Serializable{
    private String id;
    private String name;
    private Picture picture;
    private String type = "friends";
    public Friend(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public Picture getPicture() {
        return picture;
    }

    public String getType() {
        return type;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }


}
