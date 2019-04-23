package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;
import com.bulbasaur.dat256.services.Database.Authenticator;
import com.bulbasaur.dat256.services.Database.PhoneAuthenticator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author ludwighultqvist
 * class that acts as as the main entry point for fetching and updating the Firestore database.
 * the class is a singleton so that it is synchroniced when multiple objects needs access
 */
public class Database {

    private static final String USERS = "users";
    private static final String MEETUPS = "meetups";
    private static final String GROUPS = "groups";
    private static Database instance;

    private Authenticator authenticator;

    /**
     * initializes, if necessary, the singleton Database object and returns it
     * @return the singleton object
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    /**
     * private constructor, needed for the class to be a singleton
     */
    private Database() {}

    /**
     * creates a new PhoneAuthenticator object, stores in the Database object and returns it
     * @param activity the activity required by the authenticator
     * @return the PhoneAuthenticator object
     */
    public Authenticator phoneAuthenticator(Activity activity) {
        authenticator = new PhoneAuthenticator(activity);
        return authenticator;
    }

    /**
     * returns the active authenticator, it can only be one active
     * @return the Authenticator object
     */
    public Authenticator activeAuthenticator() {
        return authenticator;
    }

    /**
     * creates and returns a reference to the databases collection of users
     * @return the DBCollection object
     */
    public DBCollection users() {
        return new Collection(USERS);
    }

    /**
     * creates and returns a reference to the databases collection of meetups
     * @return the DBCollection object
     */
    public DBCollection meetups() {
        return new Collection(MEETUPS);
    }


    /**
     * creates and returns a reference to the databases collection of groups
     * @return the DBCollection object
     */
    public DBCollection groups() {
        return new Collection(GROUPS);
    }

    /**
     * returns the currently logged in user.
     * if no user is logged in null will be returned.
     * if a user is logged in and it is already in the database, the reference to the users document
     * is returned.
     * if a user is logged in but not saved in the database, a new user document will be created
     * with the logged in users uid
     * @return the DBDocument or null
     */
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
