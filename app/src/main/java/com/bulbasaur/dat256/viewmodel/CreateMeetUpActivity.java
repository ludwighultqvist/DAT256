package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;

public class CreateMeetUpActivity extends AppCompatActivity {

    private MeetUp meetUp;

    private static final int PICKED_COORDINATES_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet_up);

        meetUp = new MeetUp();

        Button chooseLocationButton = findViewById(R.id.chooseLocationButton);
        chooseLocationButton.setOnClickListener(v -> {
            Intent locationPickerIntent = new Intent(this, LocationPickerActivity.class);
            locationPickerIntent.putExtra("Coordinates", meetUp.getCoordinates());
            startActivityForResult(locationPickerIntent, PICKED_COORDINATES_CODE);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKED_COORDINATES_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}
