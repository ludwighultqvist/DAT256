package com.bulbasaur.dat256.services.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

class Collection implements DBCollection {

    private CollectionReference collection;

    Collection(String root) {
        collection = FirebaseFirestore.getInstance().collection(root);
    }

    Collection(CollectionReference collection) {
        this.collection = collection;
    }

    @Override
    public DBDocument create() {
        return new Document(collection.document());
    }

    @Override
    public DBDocument get(String id) {
        Document result = null;

        collection.document(id).get()
                .addOnCompleteListener(task -> {})
                .addOnFailureListener(e -> {});
        return result;
    }

    @Override
    public List<? extends DBDocument> all() {
        List<Document> result = new ArrayList<>();

        collection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();

                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            result.add(new Document(document.getReference()));
                        }
                    }
                })
                .addOnFailureListener(e -> {});

        return result;
    }
}
