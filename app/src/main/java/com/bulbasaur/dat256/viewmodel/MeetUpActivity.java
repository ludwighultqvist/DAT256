package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;

public class MeetUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up);

        MeetUp meetUp = (MeetUp) getIntent().getSerializableExtra("MeetUp");

        TextView titleView = findViewById(R.id.titleView);
        titleView.setText(meetUp.getName());

        TextView descriptionView = findViewById(R.id.DescriptionView);
        descriptionView.setText(meetUp.getDescription());

        Button showOnMap = findViewById(R.id.show_in_map_button);
        showOnMap.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
    }
}
