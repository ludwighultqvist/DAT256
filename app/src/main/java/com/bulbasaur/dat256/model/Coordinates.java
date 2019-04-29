package com.bulbasaur.dat256.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Coordinates implements Serializable {

    public double lat = Double.NEGATIVE_INFINITY, lon = Double.NEGATIVE_INFINITY;   //default to a number that cannot be shown on the map and is not a valid coordinate

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static String convertToNSEW(boolean latitude, double coordinate, char numDecimalPlaces) {
        if (latitude) {
            if (coordinate < 0) {
                return roundToDecimalPlace(-coordinate, numDecimalPlaces) + "S";
            } else {
                return roundToDecimalPlace(coordinate, numDecimalPlaces) + "N";
            }
        } else {    //longitude
            if (coordinate < 0) {
                return roundToDecimalPlace(-coordinate, numDecimalPlaces) + "W";
            } else {
                return roundToDecimalPlace(coordinate, numDecimalPlaces) + "E";
            }
        }
    }

    private static String roundToDecimalPlace(double number, char numDecimalPlaces) {
        StringBuilder decimalPlaces = new StringBuilder();

        for (char i = 0; i < numDecimalPlaces; i++) {
            decimalPlaces.append("#");
        }

        DecimalFormat df = new DecimalFormat("#." + decimalPlaces.toString());

        return df.format(number);
    }
}
