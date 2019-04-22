package com.bulbasaur.dat256.services.firebase;

import java.util.List;

public interface DBCollection {

    DBDocument create();

    DBDocument get(String id);

    List<? extends DBDocument> all();
}
