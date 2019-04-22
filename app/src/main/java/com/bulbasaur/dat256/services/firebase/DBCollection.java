package com.bulbasaur.dat256.services.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DBCollection implements DBCollectable {

    private CollectionReference collection;

    DBCollection(String root) {
        this(null, null, root);
    }

    private DBCollection(CollectionReference collection, DBObject object, String root) {
        if (collection == null) {
            this.collection = FirebaseFirestore.getInstance().collection(root);
        } else {
            this.collection = collection.document(object.id()).collection(root);
        }
    }

    @Override
    public DBObject get(String id) {
        final DBObject result = new DBObject(id);

        collection.document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                result.set(document.getData());
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {

                });

        return result;
    }

    @Override
    public void set(DBObject object) {
        collection.document(object.id()).set(object.get(), SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    @Override
    public List<DBObject> all() {
        List<DBObject> result = new ArrayList<>();

        collection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DBObject object = new DBObject(document.getId(), document.getData());
                        }
                    }
                })
                .addOnFailureListener(e -> {

                });

        return result;
    }

    @Override
    public List<DBObject> search(String text) {
        return null;
    }

    @Override
    public DBCollectable subCollection(DBObject object, String root) {
        return new DBCollection(collection, object, root);
    }
}
