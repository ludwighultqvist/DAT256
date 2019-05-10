package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.MeetupListAdapter;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {
    private ListView meetupList;
    private List<MeetUp> meetUps = new ArrayList<MeetUp>();
    private List<? extends DBDocument> tempDocument;

    MeetupListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);
        meetupList = (ListView) findViewById(R.id.listView);
        meetupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  Intent meetUpIntent = new Intent(ListActivity.this, MeetUpActivity.class);
                                                  meetUpIntent.putExtra("MeetUp", getMeetUp(position));
                                                  startActivityForResult(meetUpIntent, MenuActivity.SHOW_EVENT_ON_MAP_CODE);
                                              }
                                          }
        );
        init();

    }



    private MeetUp getMeetUp(int pos){
        return meetUps.get(pos);
    }

    private void init() {
        updateList();
        adapter = new MeetupListAdapter(this, R.layout.activity_meetuplistobject, meetUps);
        meetupList.setAdapter(adapter);
    }



    private void updateList() {
        //tempDocument = new ArrayList<DBDocument>();
        meetUps = Main.getInstance().getMeetUpsWithinMapView();
        /*
        allMeetUps.all(new RequestListener<List<? extends DBDocument>>(true) {

            @Override
            public void onSuccess(List<? extends DBDocument> object) {
                super.onComplete(object);
                tempDocument = object;
                init2(object);
            }
        });
    }
    private void init2(List<? extends DBDocument> documents) {
        for (DBDocument doc : documents) {
            doc.init(new RequestListener<DBDocument>(true) {
                @Override
                public void onSuccess(DBDocument document) {
                    super.onSuccess(document);

                    MeetUp newMeetUp = convertDocToMeetUp(document);

                    meetUps.add(newMeetUp);

                    if (meetUps.size() == documents.size()) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
    */
}
}

/* Todo: Create two dummy meetup objects and fill with the information we want. (Name and Date/time)
    Create ArrayList with Meetup-objects
    Create class ArrayListAdapter
   */




    //To-do: connect meetup-list to meetup-objects.

