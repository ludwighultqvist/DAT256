package com.bulbasaur.dat256.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //TODO make this button go back to the map (zoomed in on the event)
            }
        });
    }
}
