package com.bulbasaur.dat256.services.Database;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.*;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.*;
import java.util.concurrent.TimeUnit;

public class PhoneAuthenticator implements Authenticator {
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;

    private Activity activity;
    private VerificationStatus status;

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

    @Override
    public void verify(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    status = VerificationStatus.COMPLETED;
                }
                else {
                    status = VerificationStatus.FAILED;
                }
            }
        });
    }


    @Override
    public VerificationStatus status() {
        return status;
    }
}
