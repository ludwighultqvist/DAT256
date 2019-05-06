package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Country;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.model.Validator;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.uielements.CountrySpinnerAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.EditTextWithError;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton, readTerms;
    private CheckBox checkBox, ageCheck;


    private boolean firstNameValid = false, lastNameValid = false, phoneNumberValid = false;

    private Country country;

    static final int REGISTER_VERIFIED_CODE = 10;

    EditTextWithError firstNameEditText;
    EditTextWithError lastNameEditText;
    EditTextWithError phoneNumberEditText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Create the country spinner
        Spinner phoneNumberSpinner = findViewById(R.id.phoneNumberSpinner);
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(this);
        phoneNumberSpinner.setAdapter(countrySpinnerAdapter);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        Button goToLoginViewButton = findViewById(R.id.goToLoginViewButton);
        readTerms = findViewById(R.id.readTermsButton);
        checkBox = findViewById(R.id.termsCheckbox);
        ageCheck = findViewById(R.id.ageCheck);

        //Updates the selected country code to the correct value
        phoneNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                country = Country.values()[pos];
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        readTerms.setOnClickListener(v -> {
                    startActivity(new Intent(RegisterActivity.this, Terms_And_Conditions_Activity.class));
                });
        checkBox.setOnClickListener(v -> {
            checkBox.isChecked();
            updateCreateAccountButton();
        });
        ageCheck.setOnClickListener(v -> {
            ageCheck.isChecked();
            updateCreateAccountButton();
        });


          //Sets up the validation code for the first name text field
        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameValid = Validator.detectSpecialChar(Objects.requireNonNull(firstNameEditText.getText()).toString());
                firstNameEditText.setHasError(!firstNameValid);
                if(!firstNameValid){
                    firstNameEditText.setError(getApplicationContext().getString(R.string.first_name_error));
                }
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
                if(!lastNameValid){
                    lastNameEditText.setError(getApplicationContext().getString(R.string.last_name_error));
                }

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
                if(!phoneNumberValid){
                    phoneNumberEditText.setError(getApplicationContext().getString(R.string.phone_number_error));
                }

                updateCreateAccountButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Set what happens when "Create Account" is pressed: send verification code & go to verification view
        createAccountButton.setOnClickListener(v -> {
            user = Validator.createUser(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(), phoneNumberEditText.getText().toString(), country);

            Database.getInstance().phoneAuthenticator().sendVerificationCode(user.getPhoneNumber(), this, new RequestListener() {
                @Override
                public void onSuccess(Object object) {
                    super.onSuccess(object);
                    startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                    Log.d("VER", "success");
                }

                @Override
                public void onComplete(Object object) {
                    super.onComplete(object);
                    startActivityForResult(new Intent(RegisterActivity.this, VerificationView.class), REGISTER_VERIFIED_CODE);
                    Log.d("VER", "complete");
                }

                @Override
                public void onFailure(Object object) {
                    super.onFailure(object);
                    Log.d("VER", "fail: " + user.getPhoneNumber());
                }
            });


        });

        //If the user already has an account, they can go to the log-in view
        goToLoginViewButton.setOnClickListener(v -> {
            finish();

            startActivity(new Intent(this, LoginActivity.class));
        });
    }



    private void updateCreateAccountButton() {
        if (firstNameValid && lastNameValid && phoneNumberValid && checkBox.isChecked() && ageCheck.isChecked()) {
            createAccountButton.setEnabled(true);
        } else {
            createAccountButton.setEnabled(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_VERIFIED_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
                DBDocument document = Database.getInstance().user(new RequestListener<>());
                if (document != null) {
                    document.set("firstname", user.getFirstName());
                    document.set("lastname", user.getLastName());
                    document.set("phone", user.getPhoneNumber());
                    document.save(new RequestListener<>());
                }
            }
        }
    }
}
