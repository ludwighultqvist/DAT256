package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
     * @return the PhoneAuthenticator object
     */
    public Authenticator phoneAuthenticator() {
        authenticator = new PhoneAuthenticator();
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

    public boolean hasUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
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
    public DBDocument user(@NonNull RequestListener<DBDocument> listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return null;
        }

        users().get(user.getUid(), new RequestListener<DBDocument>() {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);
                    object.init(listener);
                    //listener.onSuccess(object);
                }

                @Override
                public void onComplete(DBDocument object) {
                    super.onComplete(object);
                    listener.onSuccess(users().create(user.getUid(), new RequestListener<>()));
                    /*
                    users().create(user.getUid(), new RequestListener<DBDocument>() {
                        @Override
                        public void onSuccess(DBDocument object) {
                            super.onSuccess(object);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onComplete(DBDocument object) {
                            super.onComplete(object);
                            listener.onComplete(object);
                        }

                        @Override
                        public void onFailure(DBDocument object) {
                            super.onFailure(object);
                            listener.onFailure(object);
                        }
                    });*/
                }

                @Override
                public void onFailure(DBDocument object) {
                    super.onFailure(object);
                    listener.onFailure(object);
                }

            });
        /*
        users().get(user.getUid(), new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument object) {
                super.onSuccess(object);
                if (!object.isEmpty()) {
                    listener.onSuccess(object);
                }
                else {
                    users().create(user.getUid(), new RequestListener<DBDocument>() {
                        @Override
                        public void onSuccess(DBDocument object) {
                            super.onSuccess(object);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onComplete(DBDocument object) {
                            super.onComplete(object);
                            listener.onComplete(object);
                        }

                        @Override
                        public void onFailure(DBDocument object) {
                            super.onFailure(object);
                            listener.onFailure(object);
                        }
                    });
                }
            }

            @Override
            public void onComplete(DBDocument object) {
                super.onComplete(object);
                listener.onComplete(object);
            }

            @Override
            public void onFailure(DBDocument object) {
                super.onFailure(object);
                listener.onFailure(object);
            }
        });
        */

        return listener.getObject();
    }

    public void testIt() {
        System.out.println("\n---------- DATABASE TEST STARTED ----------\n");

        DBCollection test = new Collection("test");
        test.get("test-document", new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument object) {
                super.onSuccess(object);
                object.tester().run();
            }
        });
    }

}
