package com.hout.business.impl;

import android.content.Context;
import android.content.SharedPreferences;
import com.hout.business.UserService;
import com.hout.domain.entities.User;
import com.hout.integration.ServerIntegration;
import com.hout.integration.impl.ServerIntegrationImpl;

import java.io.Serializable;

public class UserServiceImpl implements UserService, Serializable{

    private static final String CONTEXT_PROPERTY = "com.hout.storage";
    private static final String USERID_CONTEXT_PROPERTY = "userId";
    private static final String USER_APIKEY_CONTEXT_PROPERTY = "apiKey";
    private static final String USER_NAME_CONTEXT_PROPERTY = "userName";

    transient Context context;

    ServerIntegration serverIntegration;

    User currentUser;

    public UserServiceImpl(Context context) {
        this.context = context;
        serverIntegration = new ServerIntegrationImpl();
    }

    public void createUser() {

    }

    @Override
    public boolean isUserRegistered() {
        if(currentUser != null) {
            return true;
        }

        setUserIdAndApiKeyFromStorage();

        return currentUser.getUserId() != 0;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
        saveDataInPreference();
    }

    private void saveDataInPreference() {
        SharedPreferences sharedPref = context.getSharedPreferences(CONTEXT_PROPERTY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(USERID_CONTEXT_PROPERTY, currentUser.getUserId());
        editor.putString(USER_APIKEY_CONTEXT_PROPERTY, currentUser.getApiKey());
        editor.putString(USER_NAME_CONTEXT_PROPERTY, currentUser.getName());
        editor.commit();
    }

    @Override
    public User getCurrentUser() throws Exception {
        if(currentUser != null && currentUser.getUserId() != 0L) {
            return currentUser;
        } else {
            return serverIntegration.getUserDetails(currentUser.getUserId(), currentUser.getApiKey());
        }


    }

    private void setUserIdAndApiKeyFromStorage() {
        SharedPreferences sharedContext = context.getSharedPreferences(CONTEXT_PROPERTY, 0);
        currentUser = new User();
        currentUser.setUserId(sharedContext.getLong(USERID_CONTEXT_PROPERTY, 0));
        currentUser.setApiKey(sharedContext.getString(USER_APIKEY_CONTEXT_PROPERTY, ""));
        currentUser.setName(sharedContext.getString(USER_NAME_CONTEXT_PROPERTY, ""));
    }

    public void deleteSharedPreferences() {
        SharedPreferences sharedPref = context.getSharedPreferences(CONTEXT_PROPERTY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(USERID_CONTEXT_PROPERTY);
        editor.remove(USER_APIKEY_CONTEXT_PROPERTY);
        editor.remove(USER_NAME_CONTEXT_PROPERTY);
        editor.commit();
    }
}
