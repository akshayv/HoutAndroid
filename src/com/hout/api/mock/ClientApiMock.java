package com.hout.api.mock;

import com.hout.api.ClientApi;
import com.hout.domain.entities.*;
import com.hout.domain.entities.Suggestion;

import java.io.Serializable;
import java.util.*;


public class ClientApiMock implements ClientApi, Serializable {

    List<Meetup> meetups = new ArrayList<Meetup>();
    List<Notification> notifications = new ArrayList<Notification>();
    long baseMeetupId =4;
    Map<Long, List<Suggestion>> meetupSuggestionMap = new HashMap<Long, List<Suggestion>>();
    User user = new User();

    public ClientApiMock() {

        user.setName("Akshay");
        user.setUserId(1L);
        user.setContactNumber("46720309984");
        user.setApiKey("akshay");

        Meetup meetup = new Meetup(1, "Movie Night! Iron Man 3", new Date(), new Venue("GV Vivo", "Vivo City Mall"),
                new Date(1371714300000L), true, false, false);
        meetups.add(meetup);
        meetup = new Meetup(2, "Test meetup2", new Date(), null, null, false, false, false);
        meetups.add(meetup);
        meetup = new Meetup(3, "Test meetup3", new Date(), null, null, true, true, true);
        meetups.add(meetup);
        meetup = new Meetup(4, "Test meetup4", new Date(), null, null, true, false, true);
        meetups.add(meetup);

        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        Suggestion suggestion = new Suggestion();
        suggestion.setId(1L);
        suggestion.setVenue(new Venue("Amanuesvagen Stockholm", "ICA Lappis"));
        suggestion.setDate(new Date());
        suggestion.setSuggestedUserId(1L);
        suggestions.add(suggestion);


        suggestion = new Suggestion();
        suggestion.setId(2L);
        suggestion.setVenue(new Venue("Visby Stockholm", "SF Biostaden Borgen"));
        suggestion.setDate(new Date());
        suggestion.setSuggestedUserId(2L);
        suggestions.add(suggestion);
        meetupSuggestionMap.put(1L, suggestions);

        suggestions = new ArrayList<Suggestion>();
        suggestion = new Suggestion();
        suggestion.setId(3L);
        suggestion.setVenue(new Venue("Vapianos@MedBPlatsen", "Vapianos@MedBPlatsen"));
        suggestion.setDate(new Date());
        suggestion.setSuggestedUserId(1L);
        suggestions.add(suggestion);
        meetupSuggestionMap.put(2L, suggestions);

        suggestions = new ArrayList<Suggestion>();
        suggestion = new Suggestion();
        suggestion.setId(4L);
        suggestion.setVenue(new Venue("BurgerKing@TCentral", "BurgerKing@TCentral"));
        suggestion.setDate(new Date());
        suggestion.setSuggestedUserId(1L);
        suggestions.add(suggestion);
        meetupSuggestionMap.put(3L, suggestions);


        suggestions = new ArrayList<Suggestion>();
        suggestion = new Suggestion();
        suggestion.setId(5L);
        suggestion.setVenue(new Venue("Friends Arena", "Friends Arena"));
        suggestion.setDate(new Date());
        suggestion.setSuggestedUserId(1L);
        suggestions.add(suggestion);
        meetupSuggestionMap.put(4L, suggestions);


        notifications.add(new Notification("New Meetup Has been created!"));
        notifications.add(new Notification("New Suggestion has been added!"));

    }

    @Override
    public void createMeetup(String description, String suggestedLocation, Date suggestedDate, boolean isFaceBookSharing, boolean isTwitterSharing, boolean isSuggestionAllowed, List<Long> inviteeIds) throws Exception {
        meetups.add(new Meetup(++baseMeetupId, description, new Date(), null, null, isSuggestionAllowed, isTwitterSharing, isFaceBookSharing));
        notifications.add(new Notification("New Meetup Has been created!"));
        if(suggestedDate != null && suggestedLocation !=null) {
            addSuggestionForMeetup(baseMeetupId, suggestedLocation, suggestedDate);
        }
    }

    @Override
    public List<Meetup> getMeetupsForTimeRange(Date fromDate, Date toDate) throws Exception {
        return meetups;
    }

    @Override
    public List<Notification> getNotifications() throws Exception {
        List<Notification> oldNotifications = new ArrayList<Notification>();
        oldNotifications.addAll(notifications);
        notifications = new ArrayList<Notification>();
        return oldNotifications;
    }

    @Override
    public List<Suggestion> getSuggestionsForMeetup(long meetupId) throws Exception {
        return meetupSuggestionMap.get(meetupId);
    }

    @Override
    public User getUserDetails() throws Exception {
        return user;
    }

    @Override
    public void RSVPToSuggestion(long meetupId, long suggestionId,
                                 SuggestionStatus status) throws Exception {
        List<Suggestion> suggestions = meetupSuggestionMap.get(meetupId);
        for(Suggestion suggestion : suggestions) {
            if(suggestion.getId() == suggestionId) {
                switch (status) {
                    case NO:
                        suggestion.getRejectedUserIds().add(user.getUserId());
                        break;
                    case YES:
                        suggestion.getAcceptedUserIds().add(user.getUserId());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void addSuggestionForMeetup(long meetupId, String suggestedLocation, Date suggestedDate) throws Exception {
        if(meetupSuggestionMap.get(meetupId) == null) {
            List<Suggestion> suggestions = new ArrayList<Suggestion>();
            suggestions.add(new Suggestion(new Venue(null, suggestedLocation), suggestedDate));
            meetupSuggestionMap.put(meetupId, suggestions);
        } else {
            meetupSuggestionMap.get(meetupId).add(new Suggestion(new Venue(null, suggestedLocation), suggestedDate));
        }
        notifications.add(new Notification("New Suggestion has been added!"));
    }

    @Override
    public SuggestionStatus getUserSuggestionStatus(Suggestion suggestion) {
        return SuggestionStatus.NO;
    }

    @Override
    public Meetup getMeetupDetails(long meetupId) {
        return meetups.get(0);
    }

    @Override
    public void createNewUserOnServer(String text, String contactNumber) throws Exception {
    }

    @Override
    public boolean isCurrentUserRegistered() {
        return true;
    }

    @Override
    public List<UserMin> getRegisteredUsers(Set<String> contactNumbers) throws Exception {
        List<UserMin> users = new ArrayList<UserMin>();
        users.add(new UserMin(1L, "Akshay Viswanathan", "+46720309984"));
        users.add(new UserMin(2L, "Akshay V (Singapore)", "+6585525776"));
        return users;
    }
}
