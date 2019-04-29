package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * @author ludwighultqvist
 * a class which implements the Authenticator interfaces methods where the validation code is
 * sent by sms to a phone number and verified as such
 */
class PhoneAuthenticator implements Authenticator {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private Activity activity;
    private VerificationStatus status;

    PhoneAuthenticator(Activity activity, RequestListener listener) {
        this.activity = activity;
    }

    /**
     * creates a new PhoneAuthenticator object, which sets up the phone authentication to the
     * Firebase database.
     * @param activity an activity required by the Firebase phone authentication, cannot be null
     */
    PhoneAuthenticator(Activity activity) {
        this(activity, null);
    }

    PhoneAuthenticator() {
        this(null, null);
    }

    @Override
    public void sendVerificationCode(String recipient, Activity activity, RequestListener listener) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                recipient,
                60,
                TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();

                        if (code != null) {
                            verify(code);
                        }

                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        status = VerificationStatus.FAILED;

                        if (listener != null) {
                            listener.onFailure();
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        verificationId = s;
                        token = forceResendingToken;
                        status = VerificationStatus.SENT;

                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                }
        );
        status = VerificationStatus.WAITING;
    }

    /**
     * sends a verification code to the given phone number
     * @param recipient the string which is where to code is sent, e.g. email, phone number etc.
     */
    @Override
    public void sendVerificationCode(String recipient) {
        sendVerificationCode(recipient, activity, null);
    }

    @Override
    public void verify(String verificationCode, Activity activity, RequestListener listener) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        status = VerificationStatus.COMPLETED;

                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }
                    else {
                        status = VerificationStatus.FAILED;

                        if (listener != null) {
                            listener.onComplete();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure();
                    }
                });
    }

    /**
     * verifies the given code
     * @param verificationCode the string containing the code to be validated
     */
    @Override
    public void verify(String verificationCode) {
        verify(verificationCode, activity, null);
    }

    /**
     * returns the current status of the validation
     * @return the enum object of the validation
     */
    @Override
    public VerificationStatus status() {
        return status;
    }

}
