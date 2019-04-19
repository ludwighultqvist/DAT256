package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;

public class CreateMeetUpActivity extends AppCompatActivity {

    private MeetUp meetUp;

    private TextView chosenLocationTextView;

    private static final int PICKED_COORDINATES_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet_up);

        meetUp = new MeetUp();

        Button chooseLocationButton = findViewById(R.id.chooseLocationButton);
        chooseLocationButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, LocationPickerActivity.class), PICKED_COORDINATES_CODE);
        });

        chosenLocationTextView = findViewById(R.id.chosenLocationTextView);
        chosenLocationTextView.setText(getString(R.string.coordinates, "", ""));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKED_COORDINATES_CODE) {
            if (resultCode == RESULT_OK) {
                Coordinates pickedLocationCoordinates = (Coordinates) data.getSerializableExtra("Coordinates");

                meetUp.setCoordinates(pickedLocationCoordinates);

                chosenLocationTextView.setText(
                        getString(
                                R.string.coordinates,
                                Coordinates.convertToNSEW(true, meetUp.getCoordinates().lat, (char) 2),
                                Coordinates.convertToNSEW(false, meetUp.getCoordinates().lon, (char) 2)));
            }
        }
    }
}
