package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snapchat.kit.sdk.SnapLogin;
import com.bulbasaur.dat256.R;


public class ConnectSnapchatActivity extends AppCompatActivity {
    Button testButton;
   // private ViewGroup mViewRoot;
    //View mLoginButton = SnapLogin.getButton(getApplicationContext(), (ViewGroup)mViewRoot);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_snapchat);
        testButton = findViewById(R.id.connectButton);

        testButton.setOnClickListener(v -> {
            SnapLogin.getAuthTokenManager(this).startTokenGrant();
        });
        /*
        View mLoginButton = findViewById(R.id.connectButton);
        mLoginButton.setOnClickListener(v -> {
            SnapLogin.getAuthTokenManager(this).startTokenGrant();
        });*/
    }
}
