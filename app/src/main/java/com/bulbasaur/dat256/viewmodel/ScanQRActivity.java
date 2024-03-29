package com.bulbasaur.dat256.viewmodel;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class ScanQRActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (Helpers.isLoggedIn()) {
            User user = Main.getInstance().getCurrentUser();

            if (text.startsWith("MEETUP")) {
                String meetUpID = text.substring("MEETUP".length());
                Helpers.tryAttendMeetUp(this, user, meetUpID);
                finish();
            } else if (text.startsWith("USER")) {
                String friend = text.substring("USER".length());
                if (!user.hasFriend(friend)) {
                    Helpers.addFriend(this, user, friend, Helpers::emptyFunction);
                } else {
                    Toast.makeText(this, "This person is already your friend!", Toast.LENGTH_LONG).show();
                }
                finish();
            } else {
                Toast.makeText(this, "Couldn't read QR code...", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You need to be logged in to scan QR codes!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}