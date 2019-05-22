package com.bulbasaur.dat256.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    Country country;
    private int score;
    private List<String> friends = new ArrayList<>();
    private ArrayList<String> createdMeetUps = new ArrayList<>();
    private ArrayList<String> joinedMeetUps = new ArrayList<>();
    private Coordinates coordinates;

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

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId(){
        return id;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }


    public ArrayList<String> getCreatedMeetUps() {
        return createdMeetUps;
    }

    public ArrayList<String> getJoinedMeetUps() {
        return joinedMeetUps;
    }


    public void addFriend(String newFriendID){
        friends.add(newFriendID);
    }

    public List<String> getFriends() {
        return friends;
    }

    public boolean hasFriend(String possibleFriendID) {
        return friends.contains(possibleFriendID);
    }

    public void addCreatedMeetUp(String MeetUpID){
        createdMeetUps.add(MeetUpID);
    }

    public void addJoinedMeetUp(String MeetUpID){
        joinedMeetUps.add(MeetUpID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;

        return this.id.equals(((User) obj).id);
    }
}
