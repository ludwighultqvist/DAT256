package com.bulbasaur.dat256.viewmodel.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Toast;

import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
        List<String> joinedUsers = (List<String>) meetUpDoc.get("joinedusers");
        if (joinedUsers == null) joinedUsers = new ArrayList<>();
        MeetUp.Visibility visibility = MeetUp.getVisibilityFromString((String) meetUpDoc.get("visibility"));

        if (id == null || name == null || coord_lat == null || coord_lon == null
                || description == null || category == null || maxAttendees == null || startDate == null || endDate == null) {
            return null;
        }

        return new MeetUp(id, creatorID, name, new Coordinates(coord_lat, coord_lon), description, category,
                maxAttendees, startDate, endDate, visibility, joinedUsers);
    }


    private User convertDocToUser(DBDocument userDoc){
        String id = userDoc.id();
        String firstName = (String)userDoc.get("firstname");
        String lastName = (String)userDoc.get("lastname");
        String phoneNmbr = (String)userDoc.get("phone");
        int score = (int)userDoc.get("score");
        List<String> friends = Arrays.asList((String[])userDoc.get("friends"));
        List<String> createdMeetUps =  Arrays.asList((String[])userDoc.get("created meetups"));
        List<String> joinedMeetUps =  Arrays.asList((String[])userDoc.get("joined meetups"));
        LatLng coord = (LatLng)userDoc.get("coordinates");

        if(id == null || firstName == null || lastName == null || phoneNmbr == null || friends == null
                || createdMeetUps == null || joinedMeetUps == null || coord == null){
            return  null;
        }

        User friend = new User(firstName, lastName, phoneNmbr);
        friend.setCoordinates(coord);
        //TODO create an entire user

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


    public static void retrieveDocumentAndPerformAction(DBCollection collection, String id, DocumentAction action) {
        if (collection == null) return;

        collection.get(id, new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                emptyDoc.init(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        action.perform(document);
                    }
                });
            }
        });
    }

    public static void saveField(DBCollection collection, String id, String field, Object toSave, DocumentAction action) {
        if (collection == null) return;

        collection.get(id, new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                emptyDoc.set(field, toSave);

                emptyDoc.save(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        action.perform(document);
                    }
                });
            }
        });
    }

    public interface DocumentAction {
        void perform(DBDocument document);
    }

    public interface SimpleAction {
        void perform();
    }

    public static void emptyFunction() {}

    public static void joinMeetUp(Context context, MeetUp meetUp, String userID, SimpleAction successCallback) {
        if (!meetUp.alreadyJoined(userID)) {
            meetUp.joinMeetup(userID);
            Helpers.saveField(Database.getInstance().meetups(), meetUp.getId(), "joinedusers", meetUp.getJoinedUsers(), document -> {
                Toast.makeText(context, "Joined " + meetUp.getName(), Toast.LENGTH_LONG).show();
                successCallback.perform();
            });
        } else {
            Toast.makeText(context, "Already joined " + meetUp.getName(), Toast.LENGTH_LONG).show();
        }
    }
}
