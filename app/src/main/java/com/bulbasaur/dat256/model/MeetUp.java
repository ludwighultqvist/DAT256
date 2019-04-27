package com.bulbasaur.dat256.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.bulbasaur.dat256.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class MeetUp implements Serializable {

    public enum Categories{
        SPORTS("Sports", R.color.sportsColor, R.color.sportsColor2, R.drawable.sports_example, R.drawable.map_icon_sports),
        FOOD("Food", R.color.foodColor, R.color.foodColor2, R.drawable.food_example, R.drawable.map_icon_food),
        EDUCATION("Education", R.color.eduColor, R.color.eduColor2, R.drawable.education_example, R.drawable.map_icon_education),
        PARTY("Party", R.color.partyColor, R.color.partyColor2, R.drawable.party_example, R.drawable.map_icon_party),
        GAMES("Games", R.color.gamesColor, R.color.gamesColor2, R.drawable.games_example, R.drawable.map_icon_games);

        public String categoryName;
        public int color;
        public int pic;
        public int icon;
        public int secondaryColor;

        private Categories(String categoryName, int color, int secondaryColor, int pic, int icon){
            this.categoryName = categoryName;
            this.color = color;
            this.secondaryColor = secondaryColor;
            this.pic = pic;
            this.icon = icon;
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

    public int getIconDrawable() {
        return category.icon;
    }

    public Bitmap getIconBitmap(Context context) {
        return getBitmapFromVectorDrawable(context, category.icon, category.color);
    }

    /**
     * Credit to Alexey and Hugo Gresse on Stack Overflow: https://stackoverflow.com/a/38244327/3380955
     * @param context the bitmap's context
     * @param drawableId the id of the vector resource
     * @return a bitmap image of the vector resource
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int color) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(context, color));
        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(drawable).getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
