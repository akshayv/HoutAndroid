package com.hout.domain.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class Meetup implements Serializable {

    long id;
    String description;
    Date createdDate;
    Venue finalizedLocation;
    Date finalizedDate;
    boolean suggestionAllowed;
    boolean twitterSharing;
    boolean facebookSharing;

    public Meetup() {
    }

    public Meetup(long id, String description, Date createdDate, Venue finalizedLocation, Date finalizedDate,
                  boolean suggestionAllowed, boolean twitterSharing,boolean facebookSharing) {
        this.id = id;
        this.description = description;
        this.createdDate = createdDate;
        this.finalizedLocation = finalizedLocation;
        this.finalizedDate = finalizedDate;
        this.suggestionAllowed = suggestionAllowed;
        this.twitterSharing = twitterSharing;
        this.facebookSharing = facebookSharing;
    }

    public Meetup(JSONObject meetupJSON) throws JSONException, ParseException {
        setDescription(meetupJSON.getString("description"));
        setId(meetupJSON.getLong("id"));
        setCreatedDate(new Date(meetupJSON.getLong("createdDate")));
        if(!meetupJSON.getString("finalizedDate").equals("null")) {
            setFinalizedLocation(new Venue(meetupJSON.getJSONObject("finalizedLocation")));
            setFinalizedDate(new Date(meetupJSON.getLong("finalizedDate")));
        }
        setFacebookSharing(meetupJSON.getBoolean("facebookSharing"));
        setTwitterSharing(meetupJSON.getBoolean("twitterSharing"));
        setSuggestionAllowed(meetupJSON.getBoolean("suggestionsAllowed"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Venue getFinalizedLocation() {
        return finalizedLocation;
    }

    public void setFinalizedLocation(Venue finalizedLocation) {
        this.finalizedLocation = finalizedLocation;
    }

    public Date getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(Date finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    public boolean isSuggestionAllowed() {
        return suggestionAllowed;
    }

    public void setSuggestionAllowed(boolean suggestionAllowed) {
        this.suggestionAllowed = suggestionAllowed;
    }

    public boolean isTwitterSharing() {
        return twitterSharing;
    }

    public void setTwitterSharing(boolean twitterSharing) {
        this.twitterSharing = twitterSharing;
    }

    public boolean isFacebookSharing() {
        return facebookSharing;
    }

    public void setFacebookSharing(boolean facebookSharing) {
        this.facebookSharing = facebookSharing;
    }
}
