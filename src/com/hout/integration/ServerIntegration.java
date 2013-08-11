package com.hout.integration;

import com.hout.domain.entities.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ServerIntegration {

    public boolean  createMeetupOnServer(long userId, String apiKey, String description, String suggestedLocation,
                                         Date suggestedDate, boolean isFaceBookSharing,
                                         boolean isTwitterSharing, boolean isSuggestionAllowed, List<Long> inviteeIds)
            throws Exception;

    public List<Meetup> getMeetupsForTimeRange(long userId, String apiKey, Date fromDate, Date toDate) throws Exception;

    public List<Notification> getNotifications(long userId, String apiKey) throws Exception;

    public List<Suggestion> getSuggestionsForMeetup(long userId, String apiKey, long meetupId) throws Exception;

    public User getUserDetails(long userId, String apiKey) throws Exception;

    public boolean addSuggestionForMeetupOnServer(long userId, String apiKey, long meetupId,
                                                  String suggestedLocation, Date suggestedDate) throws Exception;

    public boolean declineMeetupOnServer(long userId, String apiKey, long meetupId) throws Exception;

    public boolean addInviteesToMeetupOnServer( long userId, String apiKey, Set<Long> inviteeIds,
                                                long meetupId) throws Exception;

    public boolean RSVPToSuggestionOnServer(long userId, String apiKey, long meetupId,
                                            long suggestionId, SuggestionStatus status) throws Exception;

    public Meetup getMeetupDetails(long userId, String apiKey, long meetupId) throws Exception;


    public User createUserOnServer(String userName, String contactNumber) throws Exception;

    public Set<UserMin> getRegisteredUsers(long userId, String apiKey, Set<String> contactNumbers) throws
            Exception;
}
