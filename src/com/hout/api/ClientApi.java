package com.hout.api;

import com.hout.domain.entities.Meetup;
import com.hout.domain.entities.Notification;
import com.hout.domain.entities.SuggestionStatus;
import com.hout.domain.entities.User;
import com.hout.domain.entities.Suggestion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ClientApi extends Serializable {

    public void createMeetup(String description, String suggestedLocation,
                             Date suggestedDate, boolean isFaceBookSharing,
                             boolean isTwitterSharing, boolean isSuggestionAllowed,
                             List<Long> inviteeIds) throws Exception;

    public List<Meetup> getMeetupsForTimeRange(Date fromDate, Date toDate) throws Exception;

    public List<Notification> getNotifications() throws Exception;

    public List<Suggestion> getSuggestionsForMeetup(long meetupId) throws Exception;

    public User getUserDetails() throws Exception;

    public void RSVPToSuggestion(long meetupId, long suggestionId, SuggestionStatus status) throws Exception;

    public void addSuggestionForMeetup(long meetupId, String suggestedLocation, Date suggestedDate) throws Exception;

    public SuggestionStatus getUserSuggestionStatus(Suggestion suggestion) throws Exception;

    public Meetup getMeetupDetails(long meetupId) throws Exception;

    public void createNewUserOnServer(String text, Long contactNumber) throws Exception;

    public boolean isCurrentUserRegistered();
}
