package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.viewmodel.uielements.CustomDateTimePickerHelper;

public class CreateMeetUpActivity extends AppCompatActivity {

    private static final int PICKED_COORDINATES_CODE = 2;

    private MeetUp meetUp;
    private TextView chosenLocationTextView;
    private String meetUpCategory;

    private CustomDateTimePickerHelper startDateTime, endDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet_up);

        EditText meetUpTitle = findViewById(R.id.nameEditText);

        EditText meetUpDescription = findViewById(R.id.descriptionEditText);

        meetUp = new MeetUp();

        // location
        Button chooseLocationButton = findViewById(R.id.chooseLocationButton);
        chooseLocationButton.setOnClickListener(v -> startActivityForResult(new Intent(this, LocationPickerActivity.class), PICKED_COORDINATES_CODE));

        chosenLocationTextView = findViewById(R.id.chosenLocationTextView);
        chosenLocationTextView.setText(getString(R.string.coordinates, "", ""));

        // max attendees
        EditText maxAttendees = findViewById(R.id.maxAttendeesEditText);

        final EditText startDate = findViewById(R.id.startDateEditText);
        final EditText startTime = findViewById(R.id.startTimeEditText);
        startDateTime = new CustomDateTimePickerHelper(this, startDate, startTime).init();

        final EditText endDate = findViewById(R.id.endDateEditText);
        final EditText endTime = findViewById(R.id.endTimeEditText);
        endDateTime = new CustomDateTimePickerHelper(this, endDate, endTime).init();

        // Spinner
        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meetUpCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // button
        final Button button = findViewById(R.id.createMeetUpButton);
        button.setOnClickListener(v -> {
            String title = meetUpTitle.getText().toString();
            String description = meetUpDescription.getText().toString();
            String attendees = maxAttendees.getText().toString();

            meetUp.setName(title);
            meetUp.setDescription(description);

            int maxAttendeesNumber = -1;
            try {
                maxAttendeesNumber = Integer.parseInt(attendees);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            meetUp.setMaxAttendees(maxAttendeesNumber);
            meetUp.setStart(startDateTime.getCalendar());
            meetUp.setEnd(endDateTime.getCalendar());
            meetUp.setCategory(meetUp.getCategoryFromString(meetUpCategory));

            if (isMeetUpValid(meetUp)) {
                save(meetUp);

                finish();
            } else {
                Snackbar.make(findViewById(R.id.createMeetUpLinearLayout), "You must specify a name, location, start & end times, and a category", Snackbar.LENGTH_LONG).show();
            }

            /*
             currentUser.createdMeetUps.add(meetUp);
             */

        });
    }

    private boolean isMeetUpValid(MeetUp meetUp) {
        return !meetUp.getName().isEmpty()
                && meetUp.getCoordinates().lat != Double.NEGATIVE_INFINITY && meetUp.getCoordinates().lon != Double.NEGATIVE_INFINITY
                && meetUp.getStart() != null && startDateTime.isSet() && meetUp.getEnd() != null && endDateTime.isSet()
                && meetUp.getCategory() != null;
    }

    protected void save(MeetUp meetUp){

        //koden nedan är till för att spara själva meetupen på databasen
        Database db = Database.getInstance();
        DBDocument meetup = db.meetups().create();

        //hur man får tag i ett id.
        db.meetups().get(meetup.id());

        //gör så för alla attribut för en meetup
        meetup.set("name", meetUp.getName());
        meetup.set("description", meetUp.getDescription());
        meetup.set("coord_lat", meetUp.getCoordinates().lat);
        meetup.set("coord_lon", meetUp.getCoordinates().lon);
        meetup.set("maxAttendees", meetUp.getMaxAttendees());
        meetup.set("startDate", meetUp.getStart());
        meetup.set("endDate", meetUp.getEnd());
        meetup.set("category", meetUp.getCategory());

        meetup.save();

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
