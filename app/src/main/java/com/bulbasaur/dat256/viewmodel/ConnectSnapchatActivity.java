package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
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

        testButton = findViewById(R.id.connectButton);;

        testButton.setOnClickListener(v -> {
            //TODO Om användaren ej är inloggad, tillåt ej knapptryck
            SnapLogin.getAuthTokenManager(this).startTokenGrant(); //Bör öppna Snapchat/Hemsidan för att logga in
        });

        final LoginStateController.OnLoginStateChangedListener mLoginStateChangedListener = new LoginStateController.OnLoginStateChangedListener() {
            @Override
            public void onLoginSucceeded() {
                System.out.println("Login successfull");
                Log.d("BITMOJI", "Login successfull");
                //TODO Give User Access to bitmoji, message that login successful and change view to map.
                System.out.println("BITMOJI LOGIN");
                DBDocument user = Database.getInstance().users().create("bitmoji-test");
                user.set("name", "Bitmoji test user");
                user.save();

            }

            @Override
            public void onLoginFailed() {
                System.out.println("Login failed");
                System.out.println("BITMOJI LOGIN failed");

                Log.d("BITMOJI", "Login failed");
                //TODO Print error message on screen
            }

            @Override
            public void onLogout() {
                System.out.println("Logout successfull");
                System.out.println("BITMOJI Logout");

                Log.d("BITMOJI", "Logout successfull");
                //TODO Remove User Acess to bitmoji
            }
        };

        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(mLoginStateChangedListener);

        boolean isUserLoggedIn = SnapLogin.isUserLoggedIn(this);


        if(isUserLoggedIn){
            Log.d("Plz", "Yeees");
        }


        /*
        View mLoginButton = findViewById(R.id.connectButton);
        mLoginButton.setOnClickListener(v -> {
            SnapLogin.getAuthTokenManager(this).startTokenGrant();
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("resuming");
        if(SnapLogin.isUserLoggedIn(this)){
            System.out.println("iojfoaijsdfoi");
            Log.d("Plz", "Yeees");
        }
    }
}
