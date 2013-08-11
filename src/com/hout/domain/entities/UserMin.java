package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserMin implements Serializable {

    long userId;

    String name;

    String contactNumber;

    public UserMin() {
    }

    public UserMin(long userId, String name, String contactNumber) {
        this.userId = userId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public UserMin(JSONObject ob) throws JSONException {
        setContactNumber(ob.getString("contactNumber"));
        setUserId(ob.getLong("id"));
        setName(ob.getString("name"));
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
