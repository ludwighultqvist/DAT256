package com.bulbasaur.dat256.model;

public class MeetUp {

    private long id;

    private String name;

    private double lon, lat;

    private String description;

    public MeetUp() {
        this(0, "Fest hos Hassan", 0, 0, "Yippie!");
    }

    public MeetUp(long id, String name, double longitude, double latitude, String description) {
        this.id = id;
        this.name = name;
        this.lon = longitude;
        this.lat = latitude;
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
