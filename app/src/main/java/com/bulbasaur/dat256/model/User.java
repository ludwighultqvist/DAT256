package com.bulbasaur.dat256.model;

import java.util.ArrayList;

public class User {
    private String firtName;
    private String lastName;
    private String phoneNumber;
    Country country;
    private int score;
    private ArrayList<MeetUp> createdMeetUps = new ArrayList<>();
    private ArrayList<MeetUp> joinedMeetUps = new ArrayList<>();
    private Coordinates coordinates;

    public User(String firstName, String lastName, String phoneNumber){
        this.firtName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        score = 0;

    }


}
