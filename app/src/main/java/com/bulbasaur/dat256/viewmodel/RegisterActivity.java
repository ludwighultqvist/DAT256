package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner phoneNumberSpinner = findViewById(R.id.phoneNumberSpinner);
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(this);
        phoneNumberSpinner.setAdapter(countrySpinnerAdapter);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(v -> {

        });

        EditText firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Validator.detectSpecialChar(firstNameEditText.getText().toString())) {
                    firstNameEditText.//set other style
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
