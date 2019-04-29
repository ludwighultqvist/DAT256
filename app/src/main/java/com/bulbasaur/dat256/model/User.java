package com.bulbasaur.dat256.model;

import android.content.Context;
import android.graphics.Bitmap;

import static com.bulbasaur.dat256.model.MeetUp.getBitmapFromVectorDrawable;

public class User {
    private String firtName;
    private String lastName;
    private String phoneNumber;
    Country country;
    private Coordinates coordinates;

    public User(String firstName, String lastName, String phoneNumber, double lat, double lon){
        this.firtName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.coordinates = new Coordinates();
        this.coordinates.lat = lat;
        this.coordinates.lon = lon;
    }

    public double getLongitude() {
        return coordinates.lon;
    }

    public double getLatitude() {
        return coordinates.lat;
    }
}
