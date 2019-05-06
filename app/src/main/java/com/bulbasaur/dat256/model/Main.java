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

        for (MeetUp m : meetUpsWithinMapView) {
            System.out.println(m.getId() + " " + m.getName());
        }
    }

    public void updateMapFriends(List<User> friends) {
        updateMapGeneric(friendsWithinMapView, friends);
    }

    public <T> void updateMapGeneric(List<T> into, List<T> from) {
        for (int i = into.size() - 1; i >= 0; i--) {
            T existingItem = into.get(i);
            if (!from.contains(existingItem)) {
                into.remove(existingItem);
            }
        }

        for (int i = from.size() - 1; i >= 0; i--) {
            T newItem = from.get(i);
            if (!into.contains(newItem)) {
                into.add(newItem);
            }
        }
    }

    public List<MeetUp> getMeetUpsWithinMapView() {
        return meetUpsWithinMapView;
    }
}
