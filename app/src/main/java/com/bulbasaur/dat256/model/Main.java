package com.bulbasaur.dat256.model;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String TEMP_CURRENT_USER_ID = "ERvd4vk6kchlrvvauceXd2DnnLB3";

    private User currentUser;
    private static Main instance;

    private List<MeetUp> meetUpsWithinMapView;
    private List<User> friendsWithinMapView;

    public Main() {
        meetUpsWithinMapView = new ArrayList<>();
        friendsWithinMapView = new ArrayList<>();
    }

    public void logIn(User loggedInUser) {
        currentUser = loggedInUser;
    }

    public void updateMapMeetUp(MeetUp meetUp) {
        if (!meetUpsWithinMapView.contains(meetUp)) {
            meetUpsWithinMapView.add(meetUp);

            System.out.println("MeetUp Added to Map: " + meetUp.getId() + " " + meetUp.getName());
        }
    }

    public void removeOldMeetUps(MapBounds bounds) {
        for (int i = meetUpsWithinMapView.size() - 1; i >= 0; i--) {
            MeetUp m = meetUpsWithinMapView.get(i);

            if (m.getCoordinates().lat < bounds.getBottomLeft().lat
                    || m.getCoordinates().lat > bounds.getTopRight().lat
                    || m.getCoordinates().lon < bounds.getBottomLeft().lon
                    || m.getCoordinates().lon > bounds.getTopRight().lon) {
                meetUpsWithinMapView.remove(i);
            }
        }
    }

    public List<MeetUp> getMeetUpsWithinMapView() {
        return meetUpsWithinMapView;
    }

    public static Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }
}
