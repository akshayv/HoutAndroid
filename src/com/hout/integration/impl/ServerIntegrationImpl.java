package com.hout.integration.impl;

import android.os.StrictMode;
import com.hout.domain.entities.*;
import com.hout.integration.ServerIntegration;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerIntegrationImpl implements ServerIntegration, Serializable {

    String baseUrl = "http://hout-houtcom.rhcloud.com/rest/hout/";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    @Override
    public boolean createMeetupOnServer(long userId, String apiKey,
         String description, String suggestedLocation,
         Date suggestedDate, boolean isFaceBookSharing,
         boolean isTwitterSharing, boolean isSuggestionAllowed,
         List<Long> inviteeIds) throws Exception {

        String url = baseUrl + "createMeetup";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("description", description));
        parameterList.add(new BasicNameValuePair("suggestedLocation", suggestedLocation));
        parameterList.add(new BasicNameValuePair("suggestedDate", df.format(suggestedDate)));
        parameterList.add(new BasicNameValuePair("isFacebookSharing", String.valueOf(isFaceBookSharing)));
        parameterList.add(new BasicNameValuePair("isTwitterSharing", String.valueOf(isTwitterSharing)));
        parameterList.add(new BasicNameValuePair("isSuggestionsAllowed", String.valueOf(isSuggestionAllowed)));

        url = addParameters(url, parameterList);

        for(Long inviteeId : inviteeIds) {
            url += "&inviteeIds=" + inviteeId;
        }

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return true;
    }

    @Override
    public List<Meetup> getMeetupsForTimeRange(long userId, String apiKey,
                Date fromDate, Date toDate) throws Exception {
        String url = baseUrl + "getMeetups";

        List<Meetup> meetups = new ArrayList<Meetup>();

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("fromDate", df.format(fromDate)));
        parameterList.add(new BasicNameValuePair("toDate", df.format(toDate)));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            JSONArray ar = ob.getJSONArray("meetups");
            for(int n = 0; n < ar.length(); n++)
            {
                JSONObject object = ar.getJSONObject(n);
                meetups.add(new Meetup(object));
            }
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return meetups;
    }

    @Override
    public List<Notification> getNotifications(long userId, String apiKey) throws Exception {
        String url = baseUrl + "getNotifications";

        List<Notification> notifications = new ArrayList<Notification>();

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            JSONArray ar = ob.getJSONArray("notifications");
            for(int n = 0; n < ar.length(); n++)
            {
                JSONObject object = ar.getJSONObject(n);
                notifications.add(new Notification(object));
            }
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return notifications;
    }

    @Override
    public List<Suggestion> getSuggestionsForMeetup(long userId, String apiKey, long meetupId) throws Exception {

        String url = baseUrl + "findSuggestions";

        List<Suggestion> suggestions = new ArrayList<Suggestion>();

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));
        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            JSONArray ar = ob.getJSONArray("suggestions");
            for(int n = 0; n < ar.length(); n++)
            {
                JSONObject object = ar.getJSONObject(n);
                suggestions.add(new Suggestion(object));
            }
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return suggestions;
    }

    @Override
    public User getUserDetails(long userId, String apiKey) throws Exception {
        String url = baseUrl + "getUserDetails";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            return new User(ob);
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    @Override
    public boolean addSuggestionForMeetupOnServer(long userId, String apiKey, long meetupId, String suggestedLocation,
                                                  Date suggestedDate) throws Exception {
        String url = baseUrl + "addSuggestion";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));
        parameterList.add(new BasicNameValuePair("suggestedLocation", suggestedLocation));
        parameterList.add(new BasicNameValuePair("suggestedDate", df.format(suggestedDate)));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return true;
    }

    @Override
    public boolean declineMeetupOnServer(long userId, String apiKey, long meetupId) throws Exception {
        String url = baseUrl + "declineMeetup";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return true;
    }

    @Override
    public boolean addInviteesToMeetupOnServer(long userId, String apiKey, Set<Long> inviteeIds,
                                               long meetupId) throws Exception {
        String url = baseUrl + "addInvitee";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));

        url = addParameters(url, parameterList);

        for(Long inviteeId : inviteeIds) {
            url += "&inviteeIds=" + inviteeId;
        }

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return true;
    }

    @Override
    public boolean RSVPToSuggestionOnServer(long userId, String apiKey, long meetupId, long suggestionId,
                                            SuggestionStatus status) throws Exception {
        String url = baseUrl + "RSVP";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));
        parameterList.add(new BasicNameValuePair("suggestionId", String.valueOf(suggestionId)));
        parameterList.add(new BasicNameValuePair("status", status.toString()));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        return true;
    }

    @Override
    public Meetup getMeetupDetails(long userId, String apiKey, long meetupId) throws Exception {
        String url = baseUrl + "getMeetup";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("meetupId", String.valueOf(meetupId)));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            return new Meetup(ob);
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    @Override
    public User createUserOnServer(String userName, String contactNumber) throws Exception {

        String url = baseUrl + "createUser";

        String apiKey = new BigInteger(130, new SecureRandom()).toString(32);
        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("name", userName));
        parameterList.add(new BasicNameValuePair("profilePictureLocation", "here"));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));
        parameterList.add(new BasicNameValuePair("contactNumber", String.valueOf(contactNumber)));

        url = addParameters(url, parameterList);

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();

        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
            return new User(ob.getLong("userId"), apiKey, userName, contactNumber);
        } else{
           response.getEntity().getContent().close();
           throw new IOException(statusLine.getReasonPhrase());
        }
    }

    @Override
    public Set<UserMin> getRegisteredUsers(long userId, String apiKey, Set<String> contactNumbers) throws Exception {
        String url = baseUrl + "getUsers";

        List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
        parameterList.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        parameterList.add(new BasicNameValuePair("apiKey", apiKey));

        url = addParameters(url, parameterList);

        for (String contactNumber : contactNumbers) {
            url += "&contactNumber=" + contactNumber;
        }

        HttpResponse response = getResponse(url);
        StatusLine statusLine = response.getStatusLine();
        Set<UserMin> users = new HashSet<UserMin>();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            String result = out.toString();
            JSONObject ob = new JSONObject(result);
                JSONArray ar = ob.getJSONArray("users");
            for (int n = 0; n < ar.length(); n++) {
                JSONObject object = ar.getJSONObject(n);
                users.add(new UserMin(object));
            }
        } else {
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
        return users;
    }

    private String addParameters(String url, List<NameValuePair> parameterList) {
        url += "?";
        url += URLEncodedUtils.format(parameterList, "utf-8");
        return url;
    }

    private HttpResponse getResponse(String url) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpGet request = new HttpGet(url);

        HttpClient httpclient = new DefaultHttpClient();
        return httpclient.execute(request);
    }
}
