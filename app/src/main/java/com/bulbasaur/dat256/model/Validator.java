package com.bulbasaur.dat256.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private boolean detectSpecialChar(String string) {
        Pattern p = Pattern.compile("[A-Za-zªµºÀ-ÖØ-öø-ƺƼ-ƿǄǆ-Ǉǉ-Ǌǌ-Ǳǳ-ʓʕ-ʯͰ-ͳͶ-ͷͻ-ͽΆΈ-ΊΌΎ-ΡΣ-ϵϷ-ҁҊ-ԣԱ-Ֆա-ևႠ-Ⴥᴀ-ᴫᵢ-ᵷᵹ-ᶚḀ-ἕἘ-Ἕἠ-ὅὈ-Ὅὐ-ὗὙὛὝὟ-ώᾀ-ᾇᾐ-ᾗᾠ-ᾧᾰ-ᾴᾶ-Άιῂ-ῄῆ-Ήῐ-ΐῖ-Ίῠ-Ῥῲ-ῴῶ-Ώⁱⁿℂℇℊ-ℓℕℙ-ℝℤΩℨK-ℭℯ-ℴℹℼ-ℿⅅ-ⅉⅎↃ-ↄⰀ-Ⱞⰰ-ⱞⱠ-Ɐⱱ-ⱼⲀ-ⳤⴀ-ⴥꙀ-ꙟꙢ-ꙭꚀ-ꚗꜢ-ꝯꝱ-ꞇꞋ-ꞌﬀ-ﬆﬓ-ﬗＡ-Ｚａ-ｚ]|\\ud801[\\udc00-\\udc4f]|\\ud835[\\udc00-\\udc54\\udc56-\\udc9c\\udc9e-\\udc9f\\udca2\\udca5-\\udca6\\udca9-\\udcac\\udcae-\\udcb9\\udcbb\\udcbd-\\udcc3\\udcc5-\\udd05\\udd07-\\udd0a\\udd0d-\\udd14\\udd16-\\udd1c\\udd1e-\\udd39\\udd3b-\\udd3e\\udd40-\\udd44\\udd46\\udd4a-\\udd50\\udd52-\\udea5\\udea8-\\udec0\\udec2-\\udeda\\udedc-\\udefa\\udefc-\\udf14\\udf16-\\udf34\\udf36-\\udf4e\\udf50-\\udf6e\\udf70-\\udf88\\udf8a-\\udfa8\\udfaa-\\udfc2\\udfc4-\\udfcb]");
        Matcher m = p.matcher(string);
        boolean b = m.find();
        return b;
    }

    boolean isNameValid(String name) {
        if (!name.isEmpty()) {
            if (!detectSpecialChar(name)) { //TODO add detect if in database already
                return true;
            }
            return false;
        }
        return false;
    }

    boolean checkPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("[0-9]{9}");
        Matcher m = p.matcher(phoneNumber);
        boolean b = m.find();
        return b;
    }
}
