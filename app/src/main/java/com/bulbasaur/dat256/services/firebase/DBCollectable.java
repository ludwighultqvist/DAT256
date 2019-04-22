package com.bulbasaur.dat256.services.firebase;

import java.util.List;

public interface DBCollectable {

    DBObject get(String id);

    void set(DBObject object);List<DBObject> all();

    List<DBObject> search(String text);

    DBCollectable subCollection(DBObject object, String root);
}
