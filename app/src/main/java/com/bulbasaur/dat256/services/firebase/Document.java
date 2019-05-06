package com.bulbasaur.dat256.services.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ludwighultqvist
 * class that implements the DBDocument interface
 */
class Document implements DBDocument {

    private DocumentReference document;
    private Map<String, Object> data = new HashMap<>();

    Document(@NonNull DocumentReference document, @NonNull RequestListener<DBDocument> listener) {
        init(document, listener);
    }

    /**
     * package private constructor which creates a new empty document with no reference to a
     * document in the Firestore database
     */
    Document(@NonNull DocumentReference document) {
        this.document = document;
    }

    Document() {}

    /**
     * initializes the document by fetching its content from the database using the given reference
     * @param document the given reference
     * @param listener the listener of the request
     */
    void init(DocumentReference document, @NonNull RequestListener<DBDocument> listener) {
        this.document = document;
        init(listener);
    }

    /**
     * initializes the document by fetching its content from the database using the saved reference
     * @param listener the listener of the request
     */
    @Override
    public void init(@NonNull RequestListener<DBDocument> listener) {
        this.document.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();
                            if (data != null) {
                                this.data.putAll(data);
                            }
                            listener.onSuccess(this);
                        }
                        else {
                            listener.onComplete(this);
                        }
                    }
                    else {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(this));
    }

    /**
     * returns the id of the document
     * @return the id string
     */
    @Override
    public String id() {
        return document.getId();
    }

    /**
     * gets the object of the given field. if that field or object does not exist, null
     * is returned
     * @param field the field string
     * @return the object of the field
     */
    @Override
    public Object get(String field) {
        return data.get(field.toLowerCase());
    }

    /**
     * gets the object of the given field.
     * @param field the field string
     * @param object the object to be saved on the field
     */
    @Override
    public void set(String field, Object object) {
        data.put(field.toLowerCase(), object);
    }

    /**
     * removes the local object of the given field
     * @param field the field string
     */
    @Override
    public void remove(String field) {
        data.remove(field);
    }

    /**
     * saves the stored objects of the fields in the Firestore database
     * @param listener the listener of the request
     */
    @Override
    public void save(@NonNull RequestListener<DBDocument> listener) {
        set("last-save", DateFormat.getDateTimeInstance().format(new Date()));

        document.set(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(this);
                    }
                    else {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(this));
    }

    /**
     * deletes the document from the database
     * @param listener the listener of the request
     */
    @Override
    public void delete(@NonNull RequestListener<DBDocument> listener) {
        document.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(this);
                    } else {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(this));
    }

    /**
     * fetches a subCollection of the document with the name, e.g. the groups of a user etc.
     * @param name the name string
     * @return the DBCollection object
     */
    @Override
    public DBCollection subCollection(String name) {
        return new Collection(document.collection(name));
    }

    /**
     * creates and returns a runnable tester of the document.
     * @return the runnable object
     */
    @Override
    public Runnable tester() {
        return new Tester();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Document)) {
            return false;
        }

        return this.id().equals(((Document) obj).id());
    }

    @NonNull
    @Override
    public String toString() {
        if (document == null) return  "Document: [null]";

        return "Document: [" +
                "id: " + document.getId() + ", " +
                "path: " + document.getPath() +  ", " +
                "data: " + data.toString() +
                "]";
    }

    /**
     * private class used for running tests
     */
    private class Tester implements Runnable {
        private static final String FIELD = "test-field";
        private static final String VALUE = "test-value";
        private static final boolean DEBUG = false;

        private Document document = Document.this;;
        private Map<String, Object> data;

        /*
        reset the document after the tests
         */
        private void reset() {
            System.out.println("resetting...");
            if (data == null) return;
            document.data = data;

            document.save(new RequestListener<DBDocument>(DEBUG) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);
                    System.out.println("final state: " + document.toString());
                    System.out.println("\n---------- DOCUMENT TEST FINISHED ----------\n");
                }
            });
        }

        /*
        test of the delete method
         */
        private void delete() {
            System.out.println("testing delete...");
            data = document.data;
            document.delete(new RequestListener<DBDocument>(DEBUG) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);
                    document.init(new RequestListener<DBDocument>(true) {
                        @Override
                        public void onComplete(DBDocument object) {
                            super.onSuccess(object);
                            System.out.println("delete() -> document = " + document.toString());
                            reset();
                        }
                    });
                }
            });
        }

        /*
        test of the remove method
         */
        private void remove() {
            document.remove(FIELD);
            System.out.println("testing remove...\n" + "remove(" + FIELD + ") -> " + FIELD + " = " + document.get(FIELD) + "\n");

            System.out.println("testing save after remove...");
            document.save(new RequestListener<DBDocument>(DEBUG) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);

                    document.init(new RequestListener<DBDocument>(DEBUG) {
                        @Override
                        public void onSuccess(DBDocument object) {
                            super.onSuccess(object);

                            System.out.println("save() after remove -> document = " + document.toString());
                            delete();
                        }
                    });
                }
            });
        }

        /*
        test of the get and set methods
         */
        private void getAndSet() {
            System.out.println("testing get...\n" + "get(" + FIELD + ") -> " + FIELD + " = " + document.get(FIELD) + "\n");

            document.set(FIELD, VALUE);
            System.out.println("testing set...\n" + "set(" + FIELD + ", " + VALUE + ") -> " + FIELD + " = " + document.get(FIELD) + "\n");

            System.out.println("testing save after set...");
            document.save(new RequestListener<DBDocument>(DEBUG) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);

                    document.init(new RequestListener<DBDocument>(DEBUG) {
                        @Override
                        public void onSuccess(DBDocument object) {
                            super.onSuccess(object);

                            System.out.println("save() after set() -> document = " + document.toString());
                            remove();
                        }
                    });
                }
            });
        }

        /*
        test of the init method
         */
        private void init() {
            System.out.println("testing init...");
            document.init(new RequestListener<DBDocument>(DEBUG) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);

                    System.out.println("init() -> document = " + document.toString());
                    getAndSet();
                }
            });
        }

        /**
         * method used to run all tests
         */
        @Override
        public void run() {
            System.out.println("\n---------- DOCUMENT TEST STARTED ----------\n");
            System.out.println("initial state: " + this.toString());
            init();
        }
    }
}
