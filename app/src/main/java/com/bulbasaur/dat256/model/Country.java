package com.bulbasaur.dat256.model;

import com.bulbasaur.dat256.R;

public enum Country {

    SWEDEN("Sweden", "SE", "0046", "+46", R.drawable.se),
    USA("USA", "US", "001", "+1", R.drawable.us);

    public String name, shortName, countryCode, countryCodeVisual;
    public int iconResource;

    Country(String name, String shortName, String countryCode, String countryCodeVisual, int iconResource) {
        this.name = name;
        this.shortName = shortName;
        this.countryCode = countryCode;
        this.countryCodeVisual = countryCodeVisual;
        this.iconResource = iconResource;
    }
}
