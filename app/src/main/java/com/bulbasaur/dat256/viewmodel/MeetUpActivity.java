package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MeetUpActivity extends AppCompatActivity {

    private MeetUp meetUp;

    private ImageView meetUpPicture;
    private Bitmap qrCodeBitmap;

    private boolean showingQRCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up);

        meetUp = Main.getInstance().getMeetUpsWithinMapView().get(getIntent().getIntExtra("MeetUpIndex", -1));//(MeetUp) getIntent().getSerializableExtra("MeetUp");

        TextView titleView = findViewById(R.id.titleView);
        titleView.setText(meetUp.getName());

        TextView descriptionView = findViewById(R.id.DescriptionView);
        descriptionView.setText(meetUp.getDescription());

        Button showOnMap = findViewById(R.id.show_in_map_button);
        showOnMap.setOnClickListener(v -> {
            Intent meetUpIntent = new Intent();
            meetUpIntent.putExtra("MeetUpReturn", meetUp);
            setResult(Activity.RESULT_OK, meetUpIntent);
            finish();
        });

        Button joinMeetupButton = findViewById(R.id.join_meetup_button);
        joinMeetupButton.setOnClickListener(v -> {
            if (Helpers.isLoggedIn()) {
                Helpers.joinMeetUp(this, meetUp, Main.getInstance().getCurrentUser().getId(), this::updateJoinedUsers);
            } else {
                Toast.makeText(this, "You must be logged in to do this", Toast.LENGTH_LONG).show();
            }
        });

        meetUpPicture = findViewById(R.id.meetUpPicture);
        meetUpPicture.setImageDrawable(getDrawable(meetUp.getCategory().pic));

        TextView startDateTextView = findViewById(R.id.startDateTextView);
        startDateTextView.setText(getString(R.string.starts, meetUp.getStart().getTime()));

        TextView endDateTextView = findViewById(R.id.endDateTextView);
        endDateTextView.setText(getString(R.string.ends, meetUp.getEnd().getTime()));

        TextView createdByTextView = findViewById(R.id.createdByTextView);

        if (!meetUp.getCreatorID().equals("null")) {
            createdByTextView.setText(getString(R.string.created_by, meetUp.getCreatorID()));

            //sets the text view to the name of the person who created the event
            Helpers.retrieveDocumentAndPerformAction(Database.getInstance().users(), meetUp.getCreatorID(), document -> {
                String firstName = (String) document.get("firstname");
                String lastName = (String) document.get("lastname");
                String name = firstName + " " + lastName;

                createdByTextView.setText(getString(R.string.created_by, name));
            });
        } else {
            createdByTextView.setText(getString(R.string.created_by, getString(R.string.user_not_found)));
        }

        updateJoinedUsers();

        TextView maxAttendees = findViewById(R.id.maxAttendeesMeetUpView);
        long maxSize = meetUp.getMaxAttendees();
        if (maxSize != 1) maxAttendees.setText(getString(R.string.maxMany, String.valueOf(maxSize)));
        else maxAttendees.setText(getString(R.string.maxOne, String.valueOf(maxSize)));

        Button showQRCodeButton = findViewById(R.id.showQRCodeButton);

        if (Helpers.isLoggedIn() && meetUp.alreadyAttendedBy(Main.getInstance().getCurrentUser().getId())) {
            qrCodeBitmap = Helpers.generateQRCode("MEETUP" + meetUp.getId(), 512);

            if (qrCodeBitmap == null) {
                showQRCodeButton.setVisibility(View.GONE);
            } else {
                showQRCodeButton.setVisibility(View.VISIBLE);
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
            }
        } else {
            showQRCodeButton.setVisibility(View.GONE);
        }
    }

    private void updateJoinedUsers() {
        TextView numJoinedUsers = findViewById(R.id.numJoinedUsersTextView);
        int joinedMeetUpSize = meetUp.getJoinedUsers().size();
        if (joinedMeetUpSize != 1) numJoinedUsers.setText(getString(R.string.joinedMany, String.valueOf(joinedMeetUpSize)));
        else numJoinedUsers.setText(getString(R.string.joinedOne, String.valueOf(joinedMeetUpSize)));
    }
}
