package com.bulbasaur.dat256.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    Country country;
    private int score;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> createdMeetUps = new ArrayList<>();
    private ArrayList<String> joinedMeetUps = new ArrayList<>();
    private transient LatLng coordinates;

    public User(String firstName, String lastName, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        score = 0;
    }

    public User(String ID){
        this.id = ID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getScore() {
        return String.valueOf(score);
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void addFriend(String newFriendID){
        friends.add(newFriendID);
    }

    public void addCreatedMeetUp(String MeetUpID){
        createdMeetUps.add(MeetUpID);
    }

    public void addJoinedMeetUp(String MeetUpID){
        joinedMeetUps.add(MeetUpID);
    }
}
