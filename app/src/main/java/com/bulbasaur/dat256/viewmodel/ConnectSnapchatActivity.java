package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.bulbasaur.dat256.R;
import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.core.controller.LoginStateController;


public class ConnectSnapchatActivity extends AppCompatActivity {
    Button testButton;
   // private ViewGroup mViewRoot;
    //View mLoginButton = SnapLogin.getButton(getApplicationContext(), (ViewGroup)mViewRoot);




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_snapchat);

        final LoginStateController.OnLoginStateChangedListener mLoginStateChangedListener =
                new LoginStateController.OnLoginStateChangedListener() {
                    @Override
                    public void onLoginSucceeded() {
                        System.out.println("Login successfull");
                        //TODO Give User Access to bitmoji, message that login successful and change view to map.
                    }

                    @Override
                    public void onLoginFailed() {
                        System.out.println("Login failed");
                        //TODO Print error message on screen
                    }

                    @Override
                    public void onLogout() {
                        System.out.println("Logout successfull");
                        //TODO Remove User Acess to bitmoji
                    }
                };

      SnapLogin.getLoginStateController(getApplicationContext()).addOnLoginStateChangedListener(mLoginStateChangedListener);





        testButton = findViewById(R.id.connectButton);;

        testButton.setOnClickListener(v -> {
            //TODO Om användaren ej är inloggad, tillåt ej knapptryck
            SnapLogin.getAuthTokenManager(this).startTokenGrant(); //Bör öppna Snapchat/Hemsidan för att logga in
        });
        /*
        View mLoginButton = findViewById(R.id.connectButton);
        mLoginButton.setOnClickListener(v -> {
            SnapLogin.getAuthTokenManager(this).startTokenGrant();
        });*/
    }
}
