package com.bulbasaur.dat256.services.firebase;

import java.util.HashMap;
import java.util.Map;

public class DBObject {

    private String id;
    private Map<String, Object> data = new HashMap<>();

    DBObject(String id, Map<String, Object> data) {
        this.id = id;
        set(data);
    }

    DBObject(String id) {
        this(id, null);
    }

    public String id() {
        return id;
    }

    public Object get(String name) {
        return data.get(name);
    }

    public void set(String name, Object object) {
        data.put(name, object);
    }

    void set(Map<String, Object> data) {
        if (data != null) {
            this.data.putAll(data);
        }
    }

    Map<String, Object> get() {
        return data;
    }
}
