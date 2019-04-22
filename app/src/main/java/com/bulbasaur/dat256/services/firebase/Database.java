package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;
import android.util.Log;

import com.bulbasaur.dat256.services.Database.Authenticator;
import com.bulbasaur.dat256.services.Database.PhoneAuthenticator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Database {
    private static final String USERS = "users";
    private static final String MEETUPS = "meetups";
    private static final String GROUPS = "groups";

    private static Database instance;
    private Authenticator authenticator;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    private Database() {}

    public Authenticator phoneAuthenticator(Activity activity) {
        authenticator = new PhoneAuthenticator(activity);
        return authenticator;
    }

    public Authenticator activeAuthenticator() {
        return authenticator;
    }

    public DBCollection users() {
        return new Collection(USERS);
    }

    public DBCollection meetups() {
        return new Collection(MEETUPS);
    }

    public DBCollection groups() {
        return new Collection(GROUPS);
    }

    public DBDocument user() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;

        DBDocument document = users().get(user.getUid());
        return document == null ? users().create(user.getUid()) : document;
    }

    public static void testIt() {
        Database database = Database.getInstance();

        // create a user with given ID
        DBDocument user = database.users().create("uid");
        user.set("name", "Test-User");
        user.save();

        // create a group with random ID
        DBDocument group = database.groups().create();
        group.set("name", "Test-Group");
        group.save();

        // create a meetup with random ID
        DBDocument meetup = database.meetups().create();
        meetup.set("name", "Test-Meetup");
        meetup.save();


    }
}
