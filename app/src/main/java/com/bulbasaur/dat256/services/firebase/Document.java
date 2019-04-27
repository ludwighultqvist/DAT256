package com.bulbasaur.dat256.services.firebase;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
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

    /**
     * package private constructor which creates a new document with the given reference to a
     * document in the Firestore database
     * @param document the DocumentReference object
     */
    Document(DocumentReference document) {
        if (document != null) {
            init(document);
        }
    }

    /**
     * package private constructor which creates a new empty document with no reference to a
     * document in the Firestore database
     */
    Document() {
        this(null);
    }

    /**
     * initializes the document object with the given reference to a document in the Firestore
     * database and fetches the data from it and stores it in the Map object
     * @param document the given DocumentReference object
     */
    void init(DocumentReference document) {
        Log.d("DOCUMENT", "Initializing document: " + document.getPath());
        this.document = document;
        document.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DOCUMENT", "Initializing document succeeded");
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();
                            if (data != null) {
                                data.putAll(snapshot.getData());
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {});
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
     * saves the stored objects of the fields in the Firestore database
     */
    @Override
    public void save() {
        Log.d("DOCUMENT", "Saving document: " + document.getPath());
        set("last-save", DateFormat.getDateInstance().format(new Date()));
        document.set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DOCUMENT", "Saving document succeeded");
                    }
                })
                .addOnFailureListener(e -> {});
    }

    /**
     * deletes the document from the Firestore database
     */
    @Override
    public void delete() {
        document.delete()
                .addOnCompleteListener(task -> {})
                .addOnFailureListener(e -> {});
    }

    /**
     * returns a boolean if the document does not exist in the database
     * @return the boolean value
     */
    @Override
    public boolean isEmpty() {
        return document == null;
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
}