package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Notification implements Serializable {

    String message;

    Long meetupId;

    public Notification() {
    }

    public Notification(JSONObject notificationJSON) throws JSONException {
        setMessage(notificationJSON.getString("notification"));
        setMeetupId(notificationJSON.getLong("meetupId"));
    }

    public Notification(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMeetupId() {
        return meetupId;
    }

    public void setMeetupId(Long meetupId) {
        this.meetupId = meetupId;
    }
}
