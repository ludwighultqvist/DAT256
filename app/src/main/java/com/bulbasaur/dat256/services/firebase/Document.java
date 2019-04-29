package com.bulbasaur.dat256.services.firebase;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * @author ludwighultqvist
 * class that implements the DBDocument interface
 */
class Document implements DBDocument {

    private DocumentReference document;
    private Map<String, Object> data = new HashMap<>();

    Document(DocumentReference document, RequestListener<DBDocument> listener) {
        if (document != null) {
            init(document, listener);
        }
    }

    /**
     * package private constructor which creates a new empty document with no reference to a
     * document in the Firestore database
     */
    Document() {
        this(null, null);
    }

    void init(DocumentReference document, RequestListener<DBDocument> listener) {
        this.document = document;

        this.document.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();
                            if (data != null) {
                                data.putAll(snapshot.getData());
                            }
                        }

                        if (listener != null) {
                            listener.onSuccess(this);
                        }
                    }
                    else if (listener != null) {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(this);
                    }
                });
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

    @Override
    public void save(RequestListener<DBDocument> listener) {
        set("last-save", DateFormat.getDateInstance().format(new Date()));

        document.set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onSuccess(this);
                        }
                    }
                    else if (listener != null) {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(this);
                    }
                });
    }

    /**
     * saves the stored objects of the fields in the Firestore database
     */
    @Override
    public void save() {
        save(null);
    }

    /**
     * deletes the document from the Firestore database
     */
    @Override
    public void delete() {
        delete(null);
    }

    @Override
    public void delete(RequestListener<DBDocument> listener) {
        document.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onSuccess(this);
                        }
                    }
                    else if (listener != null) {
                        listener.onComplete(this);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onComplete(this);
                    }
                });
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

    /*
    public void addListener(DBListener listener) {
        document.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                listener.update();
            }
        });
    }
    */
}
