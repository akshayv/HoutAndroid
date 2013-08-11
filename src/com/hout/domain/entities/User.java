package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User extends UserMin implements Serializable {

    String apiKey;

    public User() {
    }

    public User(long userId, String apiKey, String name, String contactNumber) {
        super(userId, name, contactNumber);
        this.apiKey = apiKey;
    }

    public User(JSONObject ob) throws JSONException {
        setApiKey(ob.getString("apiKey"));
        setContactNumber(ob.getString("contactNumber"));
        setUserId(ob.getLong("id"));
        setName(ob.getString("name"));
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

