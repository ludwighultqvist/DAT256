package com.bulbasaur.dat256.viewmodel;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bulbasaur.dat256.R;

public class Terms_And_Conditions_Activity extends AppCompatActivity {

    private TextView termsView;
    private Button goBack;
    static final int TERMS_VERIFIED_CODE = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions_view);
        termsView = (TextView)findViewById(R.id.termsView);
        goBack = (Button)findViewById(R.id.GoBack);
        init();

    }
    private void init() {
        goBack.setOnClickListener(v -> {
         finish();
        });

    }







}

