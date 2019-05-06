package com.bulbasaur.dat256.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.bulbasaur.dat256.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.bulbasaur.dat256.viewmodel.MenuActivity.getBitmapFromVectorDrawable;

public class MeetUp implements Serializable {

    public enum Categories{
        SPORTS("Sports", R.color.sportsColor, R.color.sportsColor2, R.drawable.sports_example, R.drawable.map_icon_sports),
        FOOD("Food", R.color.foodColor, R.color.foodColor2, R.drawable.food_example, R.drawable.map_icon_food),
        EDUCATION("Education", R.color.eduColor, R.color.eduColor2, R.drawable.education_example, R.drawable.map_icon_education),
        PARTY("Party", R.color.partyColor, R.color.partyColor2, R.drawable.party_example, R.drawable.map_icon_party),
        GAMES("Games", R.color.gamesColor, R.color.gamesColor2, R.drawable.games_example, R.drawable.map_icon_games);

        public String categoryName;
        public int primaryColor;
        public int pic;
        public int icon;
        public int secondaryColor;

        Categories(String categoryName, int color, int secondaryColor, int pic, int icon){
            this.categoryName = categoryName;
            this.primaryColor = color;
            this.secondaryColor = secondaryColor;
            this.pic = pic;
            this.icon = icon;
        }

    }

    private String id;

    //owner - either person or group?

    private String name, description;

    private Coordinates coords;

    private long maxAttendees;

    private Calendar start;

    private Calendar end;

    private Categories category;

    private ArrayList<User> usersJoined = new ArrayList<>();

    public MeetUp() {
        this.coords = new Coordinates();
    }

    public MeetUp(String id, String name, Coordinates coordinates, String description) {
        this.id = id;
        this.name = name;
        this.coords = coordinates;
        this.description = description;
    }

    public MeetUp(String id, String name, Coordinates coordinates, String description, Categories category, long maxAttendees, Calendar start, Calendar end) {
        this.id = id;
        this.name = name;
        this.coords = coordinates;
        this.description = description;
        this.category = category;
        this.maxAttendees = maxAttendees;
        this.start = start;
        this.end = end;
    }

    public MeetUp(String id, String name, double latitude, double longitude, String description, Categories category, int maxAttendees, Calendar start, Calendar end) {
        this.id = id;
        this.name = name;
        this.coords = new Coordinates();
        this.coords.lat = latitude;
        this.coords.lon = longitude;
        this.description = description;
        this.category = category;
        this.maxAttendees = maxAttendees;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MeetUp)) return false;

        return this.id.equals(((MeetUp) obj).id);
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

    public long getMaxAttendees(){
        return maxAttendees;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coords = coordinates;
    }

    public static Categories getCategoryFromString(String s){
        if (s == null) return null;

        s = s.toLowerCase();

        switch (s){
            case "sports":
                return Categories.SPORTS;
            case "food":
                return  Categories.FOOD;
            case "games":
                return  Categories.GAMES;
            case "party":
                return Categories.PARTY;
            case "education":
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

    public void joinMeetup(User user){
        usersJoined.add(user);
    }

    public int getIconDrawable() {
        return category.icon;
    }

    public Bitmap getIconBitmap(Context context) {
        return getBitmapFromVectorDrawable(context, category.icon, category.primaryColor);
    }


}
