package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Country;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner phoneNumberSpinner = findViewById(R.id.phoneNumberSpinner);
        ArrayList countryList = new ArrayList<>();
        Arrays.asList(Country.values()).forEach(x -> countryList.add(x.countryCode));
        ArrayAdapter<String> phoneNumberSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryList);

        phoneNumberSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneNumberSpinner.setAdapter(phoneNumberSpinnerAdapter);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(v -> {

        });
    }
}
