package com.bulbasaur.dat256.model;

import com.bulbasaur.dat256.R;

import java.io.Serializable;
import java.util.Calendar;

public class MeetUp implements Serializable {

    private enum Categories{
        SPORTS("Sports", R.color.sportsColor, R.drawable.sports_example),
        FOOD("Food", R.color.foodColor, R.drawable.food_example),
        EDUCATION("Education", R.color.eduColor, R.drawable.education_example),
        PARTY("Party", R.color.partyColor, R.drawable.party_example),
        GAMES("Games", R.color.gamesColor, R.drawable.games_example);

        private String categoty;
        private int color;
        private int pic;
        private Categories(String category, int color, int pic){
            this.categoty = category;
            this.color = color;
            this.pic = pic;
        }

    }

    private long id;

    //owner - either person or group?

    private String name, description;

    private Coordinates coords;

    private int maxAttendees;

    private Calendar start;

    private Calendar end;

    private Categories category;

    public MeetUp() {
        this.coords = new Coordinates();
    }

    public MeetUp(long id, String name, Coordinates coordinates, String description) {
        this.id = id;
        this.name = name;
        this.coords = coordinates;
        this.description = description;
    }

    public MeetUp(long id, String name, double latitude, double longitude, String description, Categories category, int maxAttendees, Calendar start, Calendar end) {
        this.id = id;
        this.name = name;
        this.coords = new Coordinates();
        this.coords.lat = latitude;
        this.coords.lon = longitude;

        System.out.println(this.coords.lat);
        System.out.println(this.coords.lon);
        this.description = description;
        this.category = category;
        this.maxAttendees = maxAttendees;
        this.start = start;
        this.end = end;
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

    public Categories getCategory(){
        return category;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public int getMaxAttendees(){
        return maxAttendees;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coords = coordinates;
    }

    public Categories getCategoryFromString(String s){
        switch (s){
            case "Sports":
                return Categories.SPORTS;
            case "Food":
                return  Categories.FOOD;
            case "Games":
                return  Categories.GAMES;
            case "Party":
                return Categories.PARTY;
            case "Education":
                return  Categories.EDUCATION;

        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
