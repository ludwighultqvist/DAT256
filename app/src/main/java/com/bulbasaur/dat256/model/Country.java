package com.bulbasaur.dat256.model;

public enum Country {

    SWEDEN("Sweden", "SE", "+46");

    public String name;
    public String shortName;
    public String countryCode;

    Country(String name, String shortName, String countryCode) {
        this.name = name;
        this.shortName = shortName;
        this.countryCode = countryCode;
    }
}
