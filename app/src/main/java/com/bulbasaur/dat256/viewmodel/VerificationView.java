package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Validator;
import com.bulbasaur.dat256.services.firebase.Authenticator;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.uielements.EditTextWithError;

import java.util.Objects;

public class VerificationView extends AppCompatActivity {
    private EditText first, second, third, fourth, fifth, sixth;
    private TextView wrongCode;
    private Authenticator authenticator;
    private String code;
    private Button newCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_view);
        first =  (EditText)findViewById(R.id.FirstNumber);
        second = (EditText)findViewById(R.id.SecondNumber);
        third = (EditText)findViewById(R.id.ThirdNumber);
        fourth = (EditText)findViewById(R.id.FourthNumber);
        fifth = (EditText)findViewById(R.id.FifthNumber);
        sixth = (EditText)findViewById(R.id.SixthNumber);
        wrongCode = (TextView)findViewById(R.id.wrongCode);
        newCode = (Button)findViewById(R.id.newCode);

        authenticator = Database.getInstance().activeAuthenticator();
        init();
        }


    private void checkCode() {
        code = first.getText().toString() + second.getText().toString() + third.getText().toString() + fourth.getText().toString() + fifth.getText().toString() + sixth.getText().toString();
        /*if (authenticator.status() == Authenticator.VerificationStatus.COMPLETED) {
            Database.testIt();

        }
        else{
         */

        authenticator.verify(code, this, new RequestListener() {
            @Override
            public void onComplete() {
                displayErrorMessage();
            }

            @Override
            public void onSuccess() {
                finish();
                startActivity(new Intent(VerificationView.this, MenuActivity.class));

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void displayErrorMessage(){
        wrongCode.setVisibility(View.VISIBLE);
        finish();
        startActivity(new Intent(this, VerificationView.class));
    }


    private void init(){

        first.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() ==1 ) {
                    if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                        checkCode();
                    }
                    second.requestFocus();
                }
            }
        });


        second.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.length() == 1) {
                    if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                        checkCode();
                    }
                    third.requestFocus();
                }

            }
        });
        third.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.length() == 1) {
                    if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                        checkCode();
                    }
                    fourth.requestFocus();
                }
            }

        });
         fourth.addTextChangedListener(new TextWatcher() {
             public void afterTextChanged(Editable s) {

             }

             public void beforeTextChanged(CharSequence s, int start, int count,
                                           int after) {

             }

             public void onTextChanged(CharSequence s, int start, int before,
                                       int count) {
                 if (s.length() == 1) {
                     if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                         checkCode();
                     }
                     fifth.requestFocus();
                 }

             }


         });
        fifth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.length() == 1) {
                    if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                        checkCode();
                    }
                    sixth.requestFocus();
                }
            }


        });

        sixth.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if(first.length()==1 && second.length()==1 && third.length()==1 && fourth.length()==1 && fifth.length()==1 && sixth.length()==1){
                    checkCode();
                }
            }


        });

    }

}
