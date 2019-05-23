package com.bulbasaur.dat256.viewmodel.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
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
        if (creatorID == null) creatorID = "null";
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

        List<String> attendingUsers = (List<String>) meetUpDoc.get("attendingusers");
        if (attendingUsers == null) attendingUsers = new ArrayList<>();

        MeetUp.Visibility visibility = MeetUp.getVisibilityFromString((String) meetUpDoc.get("visibility"));

        if (id == null || name == null || coord_lat == null || coord_lon == null
                || description == null || category == null || maxAttendees == null || startDate == null || endDate == null) {
            return null;
        }

        return new MeetUp(id, creatorID, name, new Coordinates(coord_lat, coord_lon), description, category,
                maxAttendees, startDate, endDate, visibility, joinedUsers, attendingUsers);
    }


    public static User convertDocToUser(DBDocument userDoc){
        String id = userDoc.id();
        String firstName = (String)userDoc.get("firstname");
        String lastName = (String)userDoc.get("lastname");
        String phoneNmbr = (String)userDoc.get("phone");
        Number score = (Number) userDoc.get("score");
        List<String> friends = (List<String>) userDoc.get("friends");
        List<String> createdMeetUps =  (List<String>)userDoc.get("created meetups");
        List<String> joinedMeetUps =  (List<String>)userDoc.get("joined meetups");

        Number coord_lat = (Number) userDoc.get("coord_lat");
        Number coord_lon = (Number) userDoc.get("coord_lon");

        Coordinates coord = (coord_lat == null || coord_lon == null) ? null : new Coordinates(coord_lat.doubleValue(), coord_lon.doubleValue());

        //Coordinates coord = new Coordinates(coord_lat.doubleValue(), coord_lon.doubleValue());
        if(id == null || firstName == null || lastName == null || phoneNmbr == null || coord == null){
            return  null;
        }

        User friend = new User(id);
        friend.setFirstName(firstName);
        friend.setLastName(lastName);
        friend.setPhoneNumber(phoneNmbr);
        friend.setCoordinates(coord);
        for (String f : friends) {
            friend.addFriend(f);
        }
        if(score == null) {
            friend.setScore(0);
        } else {
            friend.setScore(score.intValue());
        }

        if(joinedMeetUps != null) {
            if (!joinedMeetUps.isEmpty()) {
                for (String joinedMUID : joinedMeetUps) {
                    if (!joinedMUID.equals(null)) {
                        friend.addJoinedMeetUp(joinedMUID);
                    }
                }
            }
        }

        if(createdMeetUps != null){
            if(!createdMeetUps.isEmpty()) {
                for (String createdMUID : createdMeetUps) {
                    if (!createdMUID.equals(null)) {
                        friend.addCreatedMeetUp(createdMUID);
                    }
                }
            }
        }
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

    public static void logIn(Activity activity, DBDocument document) {
        Main.getInstance().logIn(convertDocToUser(document));//todo set other user attributes from database here

        if (activity != null) {//TODO change this so that the navigation menu items are always set correctly
            MenuItem loginLogout = ((NavigationView) activity.findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_login_logout);
            loginLogout.setTitle(R.string.log_out);
        }
    }

    public static void logOut(Activity activity) {
        Database.getInstance().logout();
        Main.getInstance().logOutCurrentUser();

        MenuItem loginLogout = ((NavigationView) activity.findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_login_logout);
        loginLogout.setTitle(R.string.create_account_log_in);
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

    public static void tryAttendMeetUp(Context context, User user, String meetUpID) {
        retrieveDocumentAndPerformAction(Database.getInstance().meetups(), meetUpID, document -> {
            MeetUp meetUp = convertDocToMeetUp(document);
            if (meetUp != null) {
                if (meetUp.alreadyAttendedBy(user.getId())) {
                    Toast.makeText(context, "You have already registered attendance at this MeetUp!", Toast.LENGTH_LONG).show();
                } else {
                    attendMeetUp(context, meetUp, user);
                }
            } else {
                Toast.makeText(context, "MeetUp is corrupt or can't be found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static void attendMeetUp(Context context, MeetUp meetUp, User user) {
        if (!meetUp.alreadyAttendedBy(user.getId())) {
            List<String> tempAttending = new ArrayList<>(meetUp.getAttendingUsers());
            tempAttending.add(user.getId());
            Helpers.saveField(Database.getInstance().meetups(), meetUp.getId(), "attendingusers", tempAttending, document -> {
                Toast.makeText(context, "You are now on the attendance list for " + meetUp.getName(), Toast.LENGTH_LONG).show();
                int meetUpIndexInMain = Main.getInstance().getMeetUpsWithinMapView().indexOf(meetUp);
                if (meetUpIndexInMain != -1) {
                    Main.getInstance().getMeetUpsWithinMapView().get(meetUpIndexInMain).attendMeetUp(user.getId());
                }
            });
        }

    }

    public static void addFriend(Context context, User currentUser, String friendToAddID, SimpleAction callback) {
        if (!currentUser.hasFriend(friendToAddID)) {
            List<String> tempFriends = new ArrayList<>(currentUser.getFriends());
            tempFriends.add(friendToAddID);
            Helpers.saveField(Database.getInstance().users(), currentUser.getId(), "friends", tempFriends, document -> {
                addBack(context, currentUser, friendToAddID, callback);
            });
        }
    }

    private static void addBack(Context context, User currentUser, String friendToAddID, SimpleAction callback) {
        retrieveDocumentAndPerformAction(Database.getInstance().users(), friendToAddID, document -> {
            List<String> newFriendFriends = (List<String>) document.get("friends");

            if (newFriendFriends != null) {
                newFriendFriends.add(currentUser.getId());
                Helpers.saveField(Database.getInstance().users(), friendToAddID, "friends", newFriendFriends, document1 -> {
                    Toast.makeText(context, "Added friend with ID " + friendToAddID, Toast.LENGTH_LONG).show();
                    currentUser.addFriend(friendToAddID);
                    callback.perform();
                });
            }
        });
    }

    public static boolean isLoggedIn() {
        return Database.getInstance().hasUser() && Main.getInstance().getCurrentUser() != null;
    }

    public static Bitmap generateQRCode(String content, int size) {
        QRCodeWriter writer = new QRCodeWriter();
        Bitmap qrCodeBitmap = null;
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, (size / 3) * 2);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return qrCodeBitmap;
    }
}
