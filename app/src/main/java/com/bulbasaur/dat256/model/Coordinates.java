package com.bulbasaur.dat256.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Coordinates implements Serializable {

    public double lat, lon;

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
