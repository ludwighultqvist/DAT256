package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Country;
import com.bulbasaur.dat256.model.Validator;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;

    private boolean firstNameValid = false, lastNameValid = false, phoneNumberValid = false;

    private String selectedCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Create the country spinner
        Spinner phoneNumberSpinner = findViewById(R.id.phoneNumberSpinner);
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(this);
        phoneNumberSpinner.setAdapter(countrySpinnerAdapter);

        EditTextWithError firstNameEditText = findViewById(R.id.firstNameEditText);
        EditTextWithError lastNameEditText = findViewById(R.id.lastNameEditText);
        EditTextWithError phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        //Updates the selected country code to the correct value
        phoneNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCountryCode = Country.values()[pos].countryCode;
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Sets up the validation code for the first name text field
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameValid = Validator.detectSpecialChar(Objects.requireNonNull(firstNameEditText.getText()).toString());
                firstNameEditText.setHasError(!firstNameValid);

                //TODO show error message here

                updateCreateAccountButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Sets up the validation code for the last name text field
        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastNameValid = Validator.detectSpecialChar(Objects.requireNonNull(lastNameEditText.getText()).toString());
                lastNameEditText.setHasError(!lastNameValid);

                //TODO show error message here

                updateCreateAccountButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Sets up the validation code for the phone number text field
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumberValid = Validator.checkPhoneChar(Objects.requireNonNull(phoneNumberEditText.getText()).toString());
                phoneNumberEditText.setHasError(!phoneNumberValid);

                //TODO show error message here

                updateCreateAccountButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        createAccountButton.setOnClickListener(v -> {
            String phoneNumber = selectedCountryCode + Objects.requireNonNull(phoneNumberEditText.getText()).toString();

            //TODO add code for sending a verification code here
        });
    }

    private void updateCreateAccountButton() {
        if (firstNameValid && lastNameValid && phoneNumberValid) {
            createAccountButton.setEnabled(true);
        } else {
            createAccountButton.setEnabled(false);
        }
    }
}
