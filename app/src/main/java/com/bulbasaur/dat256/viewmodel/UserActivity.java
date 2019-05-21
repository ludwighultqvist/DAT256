package com.bulbasaur.dat256.viewmodel;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;

public class UserActivity extends AppCompatActivity {

    private User user;

    private Bitmap qrCodeBitmap;

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

        ImageView profilePicture = findViewById(R.id.profilePicture);

        if (Main.getInstance().getCurrentUser().equals(user)) {
            qrCodeBitmap = Helpers.generateQRCode("USER" + user.getId(), 800);

            profilePicture.setImageBitmap(qrCodeBitmap);
            profilePicture.setVisibility(View.VISIBLE);
        } else {
            profilePicture.setVisibility(View.GONE);
        }

        /*Button showQRCodeButton = findViewById(R.id.showQRCodeButton);
        if (qrCodeBitmap == null) {
            showQRCodeButton.setVisibility(View.INVISIBLE);
        } else {
            showQRCodeButton.setOnClickListener(v -> {
                if (!showingQRCode) {
                    meetUpPicture.setImageBitmap(qrCodeBitmap);
                    showQRCodeButton.setText(R.string.showMeetUpPicture);
                    showingQRCode = true;
                } else {
                    meetUpPicture.setImageDrawable(getDrawable(meetUp.getCategory().pic));
                    showQRCodeButton.setText(R.string.showQRCode);
                    showingQRCode = false;
                }
            });
        }*/
    }
}
