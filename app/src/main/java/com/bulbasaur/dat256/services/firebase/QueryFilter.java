package com.bulbasaur.dat256.services.firebase;

import java.util.HashMap;
import java.util.Map;

public class QueryFilter {

    private String field;
    private Map<String, Object> filters = new HashMap<>();

    public QueryFilter(String field) {
        this.field = field;
    }

    public QueryFilter(String field, String comparison, Object value) {
        this(field);
        filters.put(comparison, value);
    }

    public void addFilter(String comparison, Object value) {
        filters.put(comparison, value);
    }

    String getField() {
        return field;
    }

    Map<String, Object> getFilters() {
        return filters;
    }

}
