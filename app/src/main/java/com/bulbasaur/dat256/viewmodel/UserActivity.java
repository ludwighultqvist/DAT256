package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.User;

public class UserActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getSerializableExtra("User");

        TextView nameView = findViewById(R.id.firstAndLastName);
        nameView.setText(user.getFirstName() + " "+ user.getLastName());

        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);
        phoneNumberView.setText("" + user.getPhoneNumber());

        TextView meetAppScore = findViewById(R.id.meetAppScore);
        if(user.getScore() == null){
            meetAppScore.setText("0");
        }else{
            meetAppScore.setText(""+user.getScore());
        }
    }
}
