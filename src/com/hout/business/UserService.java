package com.hout.business;

import com.hout.domain.entities.User;

public interface UserService {

    public User getCurrentUser() throws Exception;

    public boolean isUserRegistered();

    public void setCurrentUser(User user);

    public void deleteSharedPreferences();
}
