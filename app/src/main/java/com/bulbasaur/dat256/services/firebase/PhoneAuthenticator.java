package com.bulbasaur.dat256.services.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;

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

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private Activity activity;

    /**
     * creates a new PhoneAuthenticator object, which sets up the phone authentication to the
     * Firebase database.
     * @param activity an activity required by the Firebase phone authentication, cannot be null
     */
    PhoneAuthenticator(Activity activity) {
        this.activity = activity;
    }

    PhoneAuthenticator() {
        this(null);
    }

    @Override
    public void sendVerificationCode(String recipient, Activity activity, @NonNull RequestListener listener) {
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
                        else {
                            listener.onComplete(null);
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        listener.onComplete(null);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        listener.onFailure(null);
                    }
                });

        listener.finish();

        /*
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
                            listener.onSuccess(null);
                        }
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        status = VerificationStatus.FAILED;

                        if (listener != null) {
                            listener.onFailure(null);
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        verificationId = s;
                        token = forceResendingToken;
                        status = VerificationStatus.SENT;

                        if (listener != null) {
                            listener.onComplete(null);
                        }
                    }
                }
        );
        status = VerificationStatus.WAITING;
        */
    }

    /**
     * sends a verification code to the given phone number
     * @param recipient the string which is where to code is sent, e.g. email, phone number etc.
     */
    @Override
    public void sendVerificationCode(String recipient) {
        sendVerificationCode(recipient, activity, new RequestListener(true));
    }


    @Override
    public void verify(String verificationCode, Activity activity, @NonNull RequestListener listener) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(null);
                    }
                    else {
                        listener.onFailure(null);
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(null);
                });

        listener.finish();

        /*
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {

                        if (listener != null) {
                            listener.onSuccess(null);
                        }
                    }
                    else {

                        if (listener != null) {
                            listener.onComplete(null);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(null);
                    }
                });
        */
    }

    /**
     * verifies the given code
     * @param verificationCode the string containing the code to be validated
     */
    @Override
    public void verify(String verificationCode) {
        verify(verificationCode, activity, new RequestListener(true));
    }
}
