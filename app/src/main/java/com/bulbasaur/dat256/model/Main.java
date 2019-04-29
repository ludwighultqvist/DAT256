package com.bulbasaur.dat256.model;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private User currentUser;

    private List<MeetUp> meetUpsWithinMapView;
    private List<User> friendsWithinMapView;

    public Main() {
        meetUpsWithinMapView = new ArrayList<>();
        friendsWithinMapView = new ArrayList<>();
    }

    public void logIn(User loggedInUser) {
        currentUser = loggedInUser;
    }

    public void updateMap(List<MeetUp> meetUps, List<User> friends) {
        updateMapGeneric(meetUpsWithinMapView, meetUps);
        updateMapGeneric(friendsWithinMapView, friends);
    }

    public void updateMapMeetUps(List<MeetUp> meetUps) {
        updateMapGeneric(meetUpsWithinMapView, meetUps);

        System.out.println("updating map meetups");
    }

    public void updateMapFriends(List<User> friends) {
        updateMapGeneric(friendsWithinMapView, friends);
    }

    public <T> void updateMapGeneric(List<T> into, List<T> from) {
        for (T existingItem : into) {
            if (!from.contains(existingItem)) {
                into.remove(existingItem);
            }
        }

        for (T newItem : from) {
            if (!into.contains(newItem)) {
                into.add(newItem);
            }
        }
    }
}
