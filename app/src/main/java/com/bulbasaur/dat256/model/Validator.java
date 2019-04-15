package com.bulbasaur.dat256.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean detectSpecialChar(String string) {
        return string.matches("[a-zA-ZA-ZÅÄÖa-zåäö]+");
    }

    public static String removePhoneZero(String phoneNumber){
            if (phoneNumber.startsWith("0")) {
                phoneNumber = phoneNumber.substring(1);
            }

        return phoneNumber;
    }

    public static boolean checkPhoneLength(String phoneNumber){
        if(phoneNumber.length()>1){
            return true;
        }
        return false;
    }

    public static boolean checkPhoneChar(String phoneNumber) {
        if(phoneNumber.matches("[0-9]+") && phoneNumber.length() == 9){
            return true;
        }
        else{
            return false;
        }
    }

    public static String addCountryCode(Country country, String phoneNumber){
        return country.countryCode + phoneNumber;
    }

    public static User createUser(String firstName, String lastName, String phoneNumber, Country country){

        // ta bort nolla
        // kolla antal siffror
        // lägga till country code
        // kolla om namnet är rätt
        // kolla om efternamnet är rätt
        // isåffal, skapa instans

        if(checkPhoneLength(phoneNumber)) {
            phoneNumber = removePhoneZero(phoneNumber);
            if (checkPhoneChar(phoneNumber)) {
                phoneNumber = addCountryCode(country, phoneNumber);
                if (detectSpecialChar(firstName) && detectSpecialChar(lastName)) {
                    return new User(firstName, lastName, phoneNumber);
                }
            }
        }
            return null;
    }

}
