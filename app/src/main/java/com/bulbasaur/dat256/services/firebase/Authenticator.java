package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author ludwighultqvist
 * interface with in general required code to send verification codes to recipients
 * and verify them.
 */
public interface Authenticator {

    void sendVerificationCode(String recipient, Activity activity,@NonNull RequestListener listener);

    /**
     * sends a verification-code to the given recipient
     * @param recipient the string which is where to code is sent, e.g. email, phone number etc.
     */
    void sendVerificationCode(String recipient);

    void verify(String verificationCode, Activity activity, @NonNull RequestListener listener);

    /**
     * verifies if the given code is the same as the one sent to recipient.
     * must be preceded by a call to 'sendVerificationCode'
     * @param verificationCode the string containing the code to be validated
     */
    void verify(String verificationCode);

}
