package com.bulbasaur.dat256.services.firebase;

public class QueryFilter {

    private String field;
    private String comparison;
    private Object value;

    public QueryFilter(String field, String comparison, Object value) {
        this.field = field.toLowerCase();
        this.comparison = comparison;
        this.value = value;
    }

    String getField() {
        return field;
    }

    String getComparison() {
        return comparison;
    }

    Object getValue() {
        return value;
    }
}
