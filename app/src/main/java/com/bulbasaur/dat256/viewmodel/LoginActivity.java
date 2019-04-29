package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
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
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.viewmodel.uielements.CountrySpinnerAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.EditTextWithError;

import java.util.Objects;

public class  LoginActivity extends AppCompatActivity {

    private boolean phoneNumberValid = false;
    private Button verify;
    private String selectedCountryCode;

    static final int LOGIN_VERIFIED_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        //Create the country spinner
        EditTextWithError phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        verify = findViewById(R.id.Verify);
        Spinner phoneNumberSpinner = findViewById(R.id.phoneNumberSpinner);
        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(this);
        phoneNumberSpinner.setAdapter(countrySpinnerAdapter);
        phoneNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCountryCode = Country.values()[pos].countryCodeVisual;
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Sets up the validation code for the phone number text field
        verify.setOnClickListener(v -> {
            String phoneNumber = selectedCountryCode + Objects.requireNonNull(phoneNumberEditText.getText()).toString();

            Database.getInstance().phoneAuthenticator(this).sendVerificationCode(phoneNumber);

            startActivityForResult(new Intent(this, VerificationView.class), LOGIN_VERIFIED_CODE);
        });

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
                updateVerifyButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void updateVerifyButton() {
        if (phoneNumberValid) {
            verify.setEnabled(true);
        } else {
            verify.setEnabled(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_VERIFIED_CODE) {
            if (resultCode == RESULT_OK) {
                finish();

                //TODO verification was successful - log the user in
            }
        }
    }
}
