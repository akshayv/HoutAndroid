package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Venue implements Serializable {

    String address;
    String location;

    public Venue() {
    }

    public Venue(JSONObject venueJSON) throws JSONException {
        setAddress(venueJSON.getString("address"));
        setLocation(venueJSON.getString("location"));
    }

    public Venue(String address, String location) {
        this.address = address;
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
