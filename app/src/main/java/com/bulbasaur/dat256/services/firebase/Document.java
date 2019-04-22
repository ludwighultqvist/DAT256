package com.bulbasaur.dat256.services.firebase;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

class Document implements DBDocument {

    private DocumentReference document;
    private Map<String, Object> data = new HashMap<>();

    Document(DocumentReference document) {
        this.document = document;

        document.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
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

    @Override
    public String id() {
        return document.getId();
    }

    @Override
    public Object get(String field) {
        return data.get(field);
    }

    @Override
    public void set(String field, Object object) {
        data.put(field, object);
    }

    @Override
    public void save() {
        document.set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {})
                .addOnFailureListener(e -> {});
    }

    @Override
    public DBCollection subCollection(String name) {
        return new Collection(document.collection(name));
    }

    @Override
    public void delete() {
        document.delete()
                .addOnCompleteListener(task -> {})
                .addOnFailureListener(e -> {});
    }
}
