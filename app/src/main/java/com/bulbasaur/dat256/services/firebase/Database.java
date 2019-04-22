package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;

import com.bulbasaur.dat256.services.Database.Authenticator;
import com.bulbasaur.dat256.services.Database.PhoneAuthenticator;

public class Database {
    private static final String USERS = "users";
    private static final String MEETUPS = "meetups";
    private static final String GROUPS = "groups";

    private static Database instance;

    private Authenticator authenticator;

    private static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    private Database() {

    }

    public Authenticator phoneAuthenticator(Activity activity) {
        authenticator = new PhoneAuthenticator(activity);
        return authenticator;
    }

    public Authenticator activeAuthenticator() {
        return authenticator;
    }

    public DBCollectable users() {
        return new DBCollection(USERS);
    }

    public DBCollectable meetups() {
        return new DBCollection(MEETUPS);
    }

    public DBCollectable groups() {
        return new DBCollection(GROUPS);
    }
}
