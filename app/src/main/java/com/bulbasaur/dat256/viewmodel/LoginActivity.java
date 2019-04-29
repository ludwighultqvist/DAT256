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
import android.widget.Spinner;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Country;
import com.bulbasaur.dat256.model.Validator;
import com.bulbasaur.dat256.services.firebase.Authenticator;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.QueryFilter;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.uielements.CountrySpinnerAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.EditTextWithError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class  LoginActivity extends AppCompatActivity {

    private boolean phoneNumberValid = false;
    private Button verify, registerButton;
    private String selectedCountryCode;
    private String phoneNumber;
    private Authenticator authenticator;
    private TextView wrongNumber;

    static final int LOGIN_VERIFIED_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        wrongNumber = (TextView)findViewById(R.id.wrongNumber);
        registerButton = findViewById(R.id.registerButton);
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

            this.phoneNumber = selectedCountryCode + Objects.requireNonNull(
                    Validator.removePhoneZero(phoneNumberEditText.getText().toString()));
            System.out.println(numberInDatabase());
            System.out.println(phoneNumber);

            if(numberInDatabase()) {
                Database.getInstance().phoneAuthenticator(LoginActivity.this).sendVerificationCode(phoneNumber);

                startActivityForResult(new Intent(LoginActivity.this, VerificationView.class), LOGIN_VERIFIED_CODE);
            }
            else {
                wrongNumber.setVisibility(View.VISIBLE);
            }
        });

        registerButton.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void updateVerifyButton() {
        if (phoneNumberValid) {
            verify.setEnabled(true);
        } else {
            verify.setEnabled(false);
        }
    }

    private boolean numberInDatabase() {
        List<QueryFilter> usernameList = new ArrayList<QueryFilter>();
        usernameList.add(new QueryFilter("phonenumber", "=", phoneNumber));
        return Database.getInstance().users().search(usernameList) != null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == LOGIN_VERIFIED_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
