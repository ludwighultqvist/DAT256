package com.bulbasaur.dat256.viewmodel.uielements;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.DatePickerDialog.OnDateSetListener;
import static android.app.TimePickerDialog.OnTimeSetListener;

public class CustomDateTimePickerHelper implements OnDateSetListener, OnTimeSetListener {

    private Context context;

    private Calendar calendar;

    private EditText dateText, timeText;

    private boolean dateSet = false, timeSet = false;

    public CustomDateTimePickerHelper(Context context, EditText dateText, EditText timeText) {
        this.context = context;
        this.dateText = dateText;
        this.timeText = timeText;

        calendar = Calendar.getInstance();
    }

    public CustomDateTimePickerHelper init() {
        dateText.setOnClickListener(v -> new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        timeText.setOnClickListener(v -> new TimePickerDialog(context, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());

        return this;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        updateDateLabel();
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        dateText.setText(sdf.format(calendar.getTime()));

        dateSet = true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        updateTimeLabel();
    }

    private void updateTimeLabel(){
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        timeText.setText(sdf.format(calendar.getTime()));

        timeSet = true;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isSet() {
        return dateSet && timeSet;
    }
}
