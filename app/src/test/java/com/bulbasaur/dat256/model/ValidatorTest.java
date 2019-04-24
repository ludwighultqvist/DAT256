package com.bulbasaur.dat256.model;

import org.junit.Test;

import static com.bulbasaur.dat256.model.Country.SWEDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {

    @Test
    public void detectSpecialChar() {
        String badString = "ha!";
        String goodString = "Hassanö";
        assertFalse(Validator.detectSpecialChar(badString));
        assertTrue(Validator.detectSpecialChar(goodString));
    }

    @Test
    public void checkPhoneChar() {
        String goodPhone = "721743510";
        String badPhone = "01231";
        String badPhone2 = "bbbbbbbbb";
        String badPhoneNumber = "0284728174817";

        assertTrue(Validator.checkPhoneChar(goodPhone));
        assertFalse(Validator.checkPhoneChar(badPhone));
        assertFalse(Validator.checkPhoneChar(badPhone2));
        assertFalse(Validator.checkPhoneChar(badPhoneNumber));
    }

    @Test
    public void addCountryCode() {
        String phoneNumber = "721743510";
        phoneNumber = Validator.addCountryCode(SWEDEN, phoneNumber);
        assertEquals("0046721743510", phoneNumber);
    }

    @Test
    public void createUser() {
        String goodFirstName = "Hassan";
        String goodLastName = "Jaber";
        String goodPhoneNumber = "721743510";
        String goodPhoneNumber2 = "0721743510";
        String badFirstName = "Ha123!!";
        String badLastName = "@@";
        String badPhoneNumber = "0284728174817";
        User goodUser = Validator.createUser(goodFirstName,goodLastName,goodPhoneNumber,SWEDEN);
        User goodUser2 = Validator.createUser(goodFirstName,goodLastName,goodPhoneNumber2,SWEDEN);
        User nullUser1 = Validator.createUser(goodFirstName,goodLastName,badPhoneNumber,SWEDEN);
        User nullUser2 = Validator.createUser(goodFirstName,badLastName,goodPhoneNumber,SWEDEN);
        User nullUser3 = Validator.createUser(badFirstName,goodLastName,goodPhoneNumber,SWEDEN);

        assertNotNull(goodUser);
        assertNotNull(goodUser2);
        assertNull(nullUser1);
        assertNull(nullUser2);
        assertNull(nullUser3);

    }


    @Test
    public void removePhoneZero() {
        String phoneNumber = "0721743510";
        phoneNumber = Validator.removePhoneZero(phoneNumber);
        assertEquals("721743510",phoneNumber);
    }

    @Test
    public void checkPhoneLength(){
        String badPhone = "";
        String goodPhone = "123";
        assertTrue(Validator.checkPhoneLength(goodPhone));
        assertFalse(Validator.checkPhoneLength(badPhone));
    }

}