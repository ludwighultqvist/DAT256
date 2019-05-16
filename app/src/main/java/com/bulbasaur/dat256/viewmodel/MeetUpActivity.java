package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;

public class MeetUpActivity extends AppCompatActivity {

    private MeetUp meetUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up);

        meetUp = (MeetUp) getIntent().getSerializableExtra("MeetUp");

        TextView titleView = findViewById(R.id.titleView);
        titleView.setText(meetUp.getName());

        TextView descriptionView = findViewById(R.id.DescriptionView);
        descriptionView.setText(meetUp.getDescription());

        Button showOnMap = findViewById(R.id.show_in_map_button);
        showOnMap.setOnClickListener(v -> {
            Intent meetUpIntent = new Intent();
            meetUpIntent.putExtra("MeetUpReturn", meetUp);
            setResult(Activity.RESULT_OK, meetUpIntent);
            finish();
        });

        ImageView meetUpPicture = findViewById(R.id.meetUpPicture);
        meetUpPicture.setImageDrawable(getDrawable(meetUp.getCategory().pic));

        TextView startDateTextView = findViewById(R.id.startDateTextView);
        startDateTextView.setText(getString(R.string.starts, meetUp.getStart().getTime()));

        TextView endDateTextView = findViewById(R.id.endDateTextView);
        endDateTextView.setText(getString(R.string.ends, meetUp.getEnd().getTime()));

        TextView createdByTextView = findViewById(R.id.createdByTextView);

        if (!meetUp.getCreatorID().equals("null")) {
            createdByTextView.setText(getString(R.string.created_by, meetUp.getCreatorID()));

            //sets the text view to the name of the person who created the event
            retrieveDocumentAndPerformAction(Database.getInstance().users(), meetUp.getCreatorID(), document -> {
                String firstName = (String) document.get("firstname");
                String lastName = (String) document.get("lastname");
                String name = firstName + " " + lastName;

                createdByTextView.setText(getString(R.string.created_by, name));
            });
        } else {
            createdByTextView.setText(getString(R.string.created_by, getString(R.string.user_not_found)));
        }
    }

    private void retrieveDocumentAndPerformAction(DBCollection collection, String id, DocumentAction action) {
        if (collection == null) return;

        collection.get(id, new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                emptyDoc.init(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        action.perform(document);
                    }
                });
            }
        });
    }

    private interface DocumentAction {
        void perform(DBDocument document);
    }
}
