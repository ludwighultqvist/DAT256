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

    /**
     * creates a new PhoneAuthenticator object, which sets up the phone authentication to the database.
     */
    PhoneAuthenticator() {}

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
                            verify(code, activity, listener);
                        }
                        else {
                            listener.onComplete(null);
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        listener.onComplete(null);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        listener.onFailure(null);
                    }
                });
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
    }
}
