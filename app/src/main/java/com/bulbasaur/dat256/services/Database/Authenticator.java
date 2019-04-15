package com.bulbasaur.dat256.services.Database;

public interface Authenticator {

    enum VerificationStatus {
        WAITING, SENT, COMPLETED, FAILED,
    }

    void sendVerificationCode(String recipient);

    void verify(String verificationCode);

    VerificationStatus status();

}
