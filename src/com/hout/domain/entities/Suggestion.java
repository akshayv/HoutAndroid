package com.hout.domain.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Suggestion implements Serializable{

    long id;

    long suggestedUserId;

    Venue venue;

    Date date;

    Set<Long> acceptedUserIds = new HashSet<Long>();

    Set<Long> undecidedUserIds = new HashSet<Long>();

    Set<Long> rejectedUserIds = new HashSet<Long>();

    public Suggestion() {
    }

    public Suggestion(Venue venue, Date date) {
        this.venue = venue;
        this.date = date;
    }

    public Suggestion(JSONObject suggestionJSON) throws JSONException, ParseException {
        setId(suggestionJSON.getLong("id"));
        setSuggestedUserId(suggestionJSON.getLong("suggestedUserId"));
        setVenue(new Venue(suggestionJSON.getJSONObject("venue")));
        setDate(new Date(suggestionJSON.getLong("date")));
        JSONArray acceptedJSON = suggestionJSON.getJSONArray("acceptedUserIds");
        for(int n = 0; n < acceptedJSON.length(); n++)
        {
            getAcceptedUserIds().add(acceptedJSON.getLong(n));
        }
        JSONArray undecidedJSON = suggestionJSON.getJSONArray("undecidedUserIds");
        for(int n = 0; n < undecidedJSON.length(); n++)
        {
            getUndecidedUserIds().add(undecidedJSON.getLong(n));
        }
        JSONArray rejectedJSON = suggestionJSON.getJSONArray("rejectedUserIds");
        for(int n = 0; n < rejectedJSON.length(); n++)
        {
            getRejectedUserIds().add(rejectedJSON.getLong(n));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSuggestedUserId() {
        return suggestedUserId;
    }

    public void setSuggestedUserId(long suggestedUserId) {
        this.suggestedUserId = suggestedUserId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Long> getAcceptedUserIds() {
        return acceptedUserIds;
    }

    public void setAcceptedUserIds(Set<Long> acceptedUserIds) {
        this.acceptedUserIds = acceptedUserIds;
    }

    public Set<Long> getUndecidedUserIds() {
        return undecidedUserIds;
    }

    public void setUndecidedUserIds(Set<Long> undecidedUserIds) {
        this.undecidedUserIds = undecidedUserIds;
    }

    public Set<Long> getRejectedUserIds() {
        return rejectedUserIds;
    }

    public void setRejectedUserIds(Set<Long> rejectedUserIds) {
        this.rejectedUserIds = rejectedUserIds;
    }
}
