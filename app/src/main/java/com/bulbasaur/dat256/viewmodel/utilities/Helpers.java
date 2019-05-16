package com.bulbasaur.dat256.viewmodel.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Helpers {

    public static List<DBDocument> intersection(List<DBDocument> first, List<DBDocument> second) {
        for (int i = first.size() - 1; i >= 0; i--) {
            if (!second.contains(first.get(i))) {
                first.remove(i);
            }
        }

        return first;
    }

    public static List<DBDocument> union(List<DBDocument> first, List<DBDocument> second) {
        for (DBDocument s : second) {
            if (!first.contains(s)) {
                first.add(s);
            }
        }

        return first;
    }

    public static MeetUp convertDocToMeetUp(DBDocument meetUpDoc) {
        String id = meetUpDoc.id();
        String creatorID = (String) meetUpDoc.get("creator");
        String name = (String) meetUpDoc.get("name");
        Double coord_lat = (Double) meetUpDoc.get("coord_lat");
        Double coord_lon = (Double) meetUpDoc.get("coord_lon");
        String description = (String) meetUpDoc.get("description");
        MeetUp.Categories category = MeetUp.getCategoryFromString((String) meetUpDoc.get("category"));
        Long maxAttendees = (Long) meetUpDoc.get("maxattendees");
        Calendar startDate = MeetUp.getDateFromHashMap((HashMap<String, Object>) meetUpDoc.get("startdate"));
        Calendar endDate = MeetUp.getDateFromHashMap((HashMap<String, Object>) meetUpDoc.get("enddate"));
        MeetUp.Visibility visibility = MeetUp.getVisibilityFromString((String) meetUpDoc.get("visibility"));

        if (id == null || name == null || coord_lat == null || coord_lon == null
                || description == null || category == null || maxAttendees == null || startDate == null || endDate == null) {
            return null;
        }

        return new MeetUp(id, creatorID, name, new Coordinates(coord_lat, coord_lon), description, category,
                maxAttendees, startDate, endDate, visibility);
    }


    public static User convertDocToUser(DBDocument userDoc){
        String id = userDoc.id();
        String firstName = (String)userDoc.get("firstname");
        String lastName = (String)userDoc.get("lastname");
        String phoneNmbr = (String)userDoc.get("phone");
        Integer score = (Integer) userDoc.get("score");
        List<String> friends = (List<String>) userDoc.get("friends");
        List<String> createdMeetUps =  (List<String>)userDoc.get("created meetups");
        List<String> joinedMeetUps =  (List<String>)userDoc.get("joined meetups");
        Double coord_lat = (Double) userDoc.get("coord_lat");
        Double coord_lon = (Double) userDoc.get("coord_lon");
        Coordinates coord = new Coordinates(coord_lon, coord_lat);
        if(id == null || firstName == null || lastName == null || phoneNmbr == null || coord == null){
            return  null;
        }

        User friend = new User(id);
        friend.setFirstName(firstName);
        friend.setLastName(lastName);
        friend.setPhoneNumber(phoneNmbr);
        friend.setCoordinates(coord);
        if(score == null) {
            friend.setScore(0);
        } else {
            friend.setScore(score);
        }
/*
        for(String joinedMUID : joinedMeetUps){
            if(joinedMUID != null){
                friend.addJoinedMeetUp(joinedMUID);
            }
        }

        for(String createdMUID : createdMeetUps){
            if(createdMUID != null){
                friend.addCreatedMeetUp(createdMUID);
            }
        }
*/
        return friend;
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
