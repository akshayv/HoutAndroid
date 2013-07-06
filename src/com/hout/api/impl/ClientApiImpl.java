package com.hout.api.impl;

import android.content.Context;
import com.hout.api.ClientApi;
import com.hout.business.UserService;
import com.hout.business.impl.UserServiceImpl;
import com.hout.domain.entities.Meetup;
import com.hout.domain.entities.Notification;
import com.hout.domain.entities.SuggestionStatus;
import com.hout.domain.entities.User;
import com.hout.integration.ServerIntegration;
import com.hout.domain.entities.Suggestion;
import com.hout.integration.impl.ServerIntegrationImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ClientApiImpl implements ClientApi,Serializable {

    ServerIntegration serverIntegration;

    UserService userService;

    public ClientApiImpl(Context context) {
        super();
        userService = new UserServiceImpl(context);
        serverIntegration = new ServerIntegrationImpl();
    }

    @Override
    public void createMeetup(String description, String suggestedLocation,
                             Date suggestedDate, boolean isFaceBookSharing,
                             boolean isTwitterSharing, boolean isSuggestionAllowed, List<Long> inviteeIds)
                            throws Exception {
        User currentUser = userService.getCurrentUser();
        serverIntegration.createMeetupOnServer(currentUser.getUserId(), currentUser.getApiKey(),
                description, suggestedLocation,
                new Date(), isFaceBookSharing,
                isTwitterSharing, isSuggestionAllowed, inviteeIds);
    }

    @Override
    public List<Meetup> getMeetupsForTimeRange(Date fromDate, Date toDate) throws Exception {
        User currentUser = userService.getCurrentUser();
        return serverIntegration.getMeetupsForTimeRange(currentUser.getUserId(),
                currentUser.getApiKey(), fromDate, toDate);
    }

    @Override
    public List<Notification> getNotifications() throws Exception {
        User currentUser = userService.getCurrentUser();
        return serverIntegration.getNotifications(currentUser.getUserId(), currentUser.getApiKey());
    }

    @Override
    public List<Suggestion> getSuggestionsForMeetup(long meetupId) throws Exception {
        User currentUser = userService.getCurrentUser();
        return serverIntegration.getSuggestionsForMeetup(currentUser.getUserId(), currentUser.getApiKey(), meetupId);
    }

    @Override
    public User getUserDetails() throws Exception {
        return userService.getCurrentUser();
    }

    @Override
    public void RSVPToSuggestion(long meetupId, long suggestionId, SuggestionStatus status) throws Exception{
        User currentUser = userService.getCurrentUser();
        serverIntegration.RSVPToSuggestionOnServer(currentUser.getUserId(), currentUser.getApiKey(),
                meetupId, suggestionId, status);
    }

    @Override
    public void addSuggestionForMeetup(long meetupId, String suggestedLocation,
                                       Date suggestedDate) throws Exception{
        User currentUser = userService.getCurrentUser();
        serverIntegration.addSuggestionForMeetupOnServer(currentUser.getUserId(), currentUser.getApiKey(),
                meetupId, suggestedLocation, suggestedDate);
    }

    @Override
    public SuggestionStatus getUserSuggestionStatus(Suggestion suggestion) throws Exception {
        User currentUser = userService.getCurrentUser();
        if(suggestion.getAcceptedUserIds().contains(currentUser.getUserId())) {
            return SuggestionStatus.YES;
        } else if(suggestion.getRejectedUserIds().contains(currentUser.getUserId())) {
            return SuggestionStatus.NO;
        }
        return SuggestionStatus.UNDECIDED;
    }

    @Override
    public Meetup getMeetupDetails(long meetupId) throws Exception{
        User currentUser = userService.getCurrentUser();
        return serverIntegration.getMeetupDetails(currentUser.getUserId(), currentUser.getApiKey(),
                meetupId);
    }

    @Override
    public void createNewUserOnServer(String userName, Long contactNumber) throws Exception {
        User user = serverIntegration.createUserOnServer(userName, contactNumber);
        userService.setCurrentUser(user);
    }

    @Override
    public boolean isCurrentUserRegistered() {
        userService.deleteSharedPreferences();
        return userService.isUserRegistered();
    }
}
