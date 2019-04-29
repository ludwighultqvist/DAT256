package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;

/**
 * @author ludwighultqvist
 * interface with in general required code to send verification codes to recipients
 * and verify them.
 */
public interface Authenticator {

    /**
     * enum which acts as the current state of a validation in progress
     * WAITING - a validation is started, but waiting to be sent
     * SENT - the validation has been sent, but not yet validated
     * COMPLETED - the validation is done
     * FAILED - the validation has failed
     * null - no validation is has started
     */
    enum VerificationStatus {
        WAITING, SENT, COMPLETED, FAILED,
    }

    /**
     * sends a verification-code to the given recipient
     * @param recipient the string which is where to code is sent, e.g. email, phone number etc.
     */
    void sendVerificationCode(String recipient);

    /**
     * verifies if the given code is the same as the one sent to recipient.
     * must be preceded by a call to 'sendVerificationCode'
     * @param verificationCode the string containing the code to be validated
     */
    void verify(String verificationCode);

    /**
     * returns the current status of the validation
     * @return the enum object of the validation
     */
    VerificationStatus status();

    void sendVerificationCode(String recipient, Activity activity, RequestListener listener);

    void verify(String verificationCode, Activity activity, RequestListener listener);
}
