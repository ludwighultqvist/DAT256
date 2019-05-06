package com.bulbasaur.dat256.services.firebase;

import android.support.annotation.NonNull;

/**
 * @author ludwighultqvist
 * interface that acts as a document in the Firestore database
 */
public interface DBDocument {

    /**
     * returns the id of the document
     * @return the id string
     */
    String id();

    /**
     * initializes the document by fetching its content from the database using the saved reference
     * @param listener the listener of the request
     */
    void init(@NonNull RequestListener<DBDocument> listener);

    /**
     * gets the local object of the given field. if that field or object does not exist, null
     * is returned
     * @param field the field string
     * @return the object of the field
     */
    Object get(String field);

    /**
     * gets the local object of the given field.
     * @param field the field string
     * @param object the object to be saved on the field
     */
    void set(String field, Object object);

    /**
     * removes the local object of the given field
     * @param field the field string
     */
    void remove(String field);

    /**
     * saves the stored objects of the fields in the Firestore database
     * @param listener the listener of the request
     */
    void save(@NonNull RequestListener<DBDocument> listener);

    /**
     * deletes the document from the database
     * @param listener the listener of the request
     */
    void delete(@NonNull RequestListener<DBDocument> listener);

    /**
     * fetches a subCollection of the document with the name, e.g. the groups of a user etc.
     * @param name the name string
     * @return the DBCollection object
     */
    DBCollection subCollection(String name);

    /**
     * creates and returns a runnable tester of the document.
     * @return the runnable object
     */
    Runnable tester();
}
