package com.bulbasaur.dat256.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

        final Calendar myCalendar = Calendar.getInstance();

        EditText startDate = (EditText) findViewById(R.id.startDateEditText);
        EditText startTime = (EditText)findViewById(R.id.startTimeEditText);

        //calendar
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
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                startDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateMeetUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //end calendar
        //time
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }

            private void updateLabel(){
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
                startTime.setText(sdf.format(myCalendar.getTime()));
            }
        };

        startTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                new TimePickerDialog(CreateMeetUpActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),
                        true ).show();
            }
        });

        //end time

    }

    protected void save(){
        Database db = Database.getInstance();
        DBDocument meetup = db.meetups().create();
        //gör så för alla attribut för en meetup
        db.meetups().get(meetup.id());
        meetup.set("name",null);
        meetup.set("description",null);
        meetup.set("name",null);
        meetup.set("name",null);
        meetup.set("name",null);

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
