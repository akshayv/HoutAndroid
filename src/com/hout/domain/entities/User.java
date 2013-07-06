package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {

    long userId;

    String apiKey;

    String name;

    long contactNumber;

    public User() {
    }

    public User(long userId, String apiKey, String name, long contactNumber) {
        this.userId = userId;
        this.apiKey = apiKey;
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public User(JSONObject ob) throws JSONException {
        setApiKey(ob.getString("apiKey"));
        setContactNumber(ob.getLong("contactNumber"));
        setUserId(ob.getLong("id"));
        setName(ob.getString("name"));
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(long contactNumber) {
        this.contactNumber = contactNumber;
    }
}

