package com.bulbasaur.dat256.model;

import java.io.Serializable;

public class MeetUp implements Serializable {

    private long id;

    private String name;

    private double lon, lat;

    private String description;

    public MeetUp() {
        this(0, "Fest hos Hassan", 57.714957, 11.909446, "Yippie!");
    }

    public MeetUp(long id, String name, double latitude, double longitude, String description) {
        this.id = id;
        this.name = name;
        this.lat = latitude;
        this.lon = longitude;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public double getLongitude() {
        return lon;
    }

    public double getLatitude() {
        return lat;
    }

    public String getDescription() {
        return description;
    }
}
