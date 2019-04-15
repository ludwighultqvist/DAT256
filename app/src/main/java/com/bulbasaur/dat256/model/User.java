package com.bulbasaur.dat256.model;

public class User {
    private String firtName;
    private String lastName;
    private String phoneNumber;
    Country country;

    public User(String firstName, String lastName, String phoneNumber){
        this.firtName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
