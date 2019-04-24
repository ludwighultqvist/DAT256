package com.bulbasaur.dat256.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class CreateMeetUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MeetUp meetUp;
    private TextView chosenLocationTextView;
    private EditText startDate;
    private EditText startTime;
    private String meetUpCategory;

    private static final int PICKED_COORDINATES_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meet_up);

        EditText meetUpTitle = findViewById(R.id.nameEditText);

        EditText meetUpDescription = findViewById(R.id.descriptionEditText);

        meetUp = new MeetUp();

        /**
         * location
         */

        Button chooseLocationButton = findViewById(R.id.chooseLocationButton);
        chooseLocationButton.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, LocationPickerActivity.class), PICKED_COORDINATES_CODE);
        });

        chosenLocationTextView = findViewById(R.id.chosenLocationTextView);
        chosenLocationTextView.setText(getString(R.string.coordinates, "", ""));

        /**
         * max attendees
         */

        EditText maxAttendees = findViewById(R.id.maxAttendeesEditText);



        final Calendar myCalendar = Calendar.getInstance();

        startDate = findViewById(R.id.startDateEditText);
        startTime = findViewById(R.id.startTimeEditText);

        /**
         * DatePickerDialog
         */
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }


            private void updateLabel() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                startDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        startDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            DatePickerDialog dpd = new DatePickerDialog(CreateMeetUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        /**
         * TimePickerDialog
         */
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }

            private void updateLabel(){
                String myFormat = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
                startTime.setText(sdf.format(myCalendar.getTime()));
            }
        };

        startTime.setOnClickListener(v -> new TimePickerDialog(CreateMeetUpActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),
                true ).show());


        /**
         * Spinner
         */
        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        /**
         * button
         */

        final Button button = findViewById(R.id.createMeetUpButton);
        button.setOnClickListener(v -> {
            String title = meetUpTitle.getText().toString();
            String description = meetUpDescription.getText().toString();
            String attendees = maxAttendees.getText().toString();

            meetUp.setName(title);
            meetUp.setDescription(description);
            meetUp.setMaxAttendees(Integer.parseInt(attendees));
            meetUp.setStart(myCalendar);
            meetUp.setEnd(myCalendar);
            meetUp.setCategory(meetUp.getCategoryFromString(meetUpCategory));

            save(meetUp);
        });

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
        meetup.set("coordinates", meetUp.getCoordinates());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        meetUpCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
