package com.bulbasaur.dat256.services.firebase;

public interface DBDocument {

    String id();

    Object get(String field);

    void set(String field, Object object);

    void save();

    DBCollection subCollection(String name);

    void delete();
}
