package com.bulbasaur.dat256.model;

import java.io.Serializable;

public class MeetUp implements Serializable {

    private long id;

    private String name, description;

    private Coordinates coords;

    public MeetUp() {
        this.coords = new Coordinates();
    }

    public MeetUp(long id, String name, Coordinates coordinates, String description) {
        this.id = id;
        this.name = name;
        this.coords = coordinates;
        this.description = description;
    }

    public MeetUp(long id, String name, double latitude, double longitude, String description) {
        this.id = id;
        this.name = name;
        this.coords = new Coordinates();
        this.coords.lat = latitude;
        this.coords.lon = longitude;

        System.out.println(this.coords.lat);
        System.out.println(this.coords.lon);
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return coords.lon;
    }

    public double getLatitude() {
        return coords.lat;
    }

    public String getDescription() {
        return description;
    }

    public Coordinates getCoordinates() {
        return coords;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coords = coordinates;
    }
}
