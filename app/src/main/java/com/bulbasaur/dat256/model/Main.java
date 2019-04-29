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
        updateMap(meetUpsWithinMapView, meetUps);
        updateMap(friendsWithinMapView, friends);
    }

    public <T> void updateMap(List<T> into, List<T> from) {
        for (T existingItem : into) {
            if (!from.contains(existingItem)) {
                from
            }
        }


        for (MeetUp existingMeetUp : meetUpsWithinMapView) {
            if (!meetUps.contains(existingMeetUp)) {
                meetUpsWithinMapView.remove(existingMeetUp);
            }
        }

        for (MeetUp newMeetUp : meetUps) {
            if (!meetUpsWithinMapView.contains(newMeetUp)) {
                meetUpsWithinMapView.add(newMeetUp);
            }
        }
    }
}
