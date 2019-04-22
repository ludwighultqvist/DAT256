package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;

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

    public DBDocument currentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user == null ? null : users().get(user.getUid());
    }
}
