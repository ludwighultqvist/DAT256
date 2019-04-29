package com.bulbasaur.dat256.model;

public class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    Country country;

    public User(String firstName, String lastName, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
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
}
