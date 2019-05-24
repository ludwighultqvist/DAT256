package com.bulbasaur.dat256.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private User currentUser;
    private static Main instance;

    private List<MeetUp> meetUpsWithinMapView;

    private List<User> friendsWithinMapView;

    private Map<MeetUp.Categories, Boolean> categoryFilters = new HashMap<>();

    public Main() {
        meetUpsWithinMapView = new ArrayList<>();
        friendsWithinMapView = new ArrayList<>();

        for (MeetUp.Categories c : MeetUp.Categories.values()) {
            categoryFilters.put(c, true);
        }
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

    public void updateMapFriends(User user){
        if(!friendsWithinMapView.contains(user)){
            friendsWithinMapView.add(user);

            System.out.println("User added to map:" + user.getId() + " " + user.getFirstName() + user.getLastName());
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

    public void removeFriends(MapBounds bounds){
        for (int i = friendsWithinMapView.size() - 1; i >= 0; i--) {
            User user = friendsWithinMapView.get(i);

            if (user.getCoordinates().lat < bounds.getBottomLeft().lat
                    || user.getCoordinates().lat > bounds.getTopRight().lat
                    || user.getCoordinates().lon < bounds.getBottomLeft().lon
                    || user.getCoordinates().lon > bounds.getTopRight().lon) {
                friendsWithinMapView.remove(i);
            }
        }
    }

    public List<MeetUp> getMeetUpsWithinMapView() {
        return meetUpsWithinMapView;
    }

    public List<User> getFriendsWithinMapView() {
        return friendsWithinMapView;
    }

    public static Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logOutCurrentUser() {
        currentUser = null;
    }

    public Map<MeetUp.Categories, Boolean> getCategoryFilters() {
        return categoryFilters;
    }
}
