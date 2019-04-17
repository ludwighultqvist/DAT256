package com.bulbasaur.dat256.services.Database;

import android.app.Activity;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * @author ludwighultqvist
 * a class which implements the Authenticator interfaces methods where the validation code is
 * sent by sms to a phone number and verified as such
 */
public class PhoneAuthenticator implements Authenticator {

    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;

    private Activity activity;
    private VerificationStatus status;

    /**
     * creates a new PhoneAuthenticator object, which sets up the phone authentication to the
     * Firebase database.
     * @param activity an activity required by the Firebase phone authentication, cannot be null
     */
    public PhoneAuthenticator(Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // automatically log in
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                status = VerificationStatus.FAILED;
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                // manually log in
                verificationId = s;
                token = forceResendingToken;
                status = VerificationStatus.SENT;
            }
        };
    }

    /**
     * sends a verification code to the given phone number
     * @param recipient the string which is where to code is sent, e.g. email, phone number etc.
     */
    @Override
    public void sendVerificationCode(String recipient) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                recipient,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks
        );
        status = VerificationStatus.WAITING;
    }

    /**
     * verified the given code
     * @param verificationCode the string containing the code to be validated
     */
    @Override
    public void verify(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    /**
     * returns the current status of the validation
     * @return the enum object of the validation
     */
    @Override
    public VerificationStatus status() {
        return status;
    }

    /**
     * tries a sign in / sign up in the Firebase date with the given credential object containing
     * the code to be validated. If the task is successful a user is fetched.
     * @param credential the object containing the data of the validation
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                status = VerificationStatus.COMPLETED;
            }
            else {
                status = VerificationStatus.FAILED;
            }
        });
    }


}
