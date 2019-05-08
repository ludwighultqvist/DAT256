package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.MeetupListAdapter;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.QueryFilter;
import com.bulbasaur.dat256.services.firebase.RequestListener;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {
    private ListView meetupList;
    private ArrayList<MeetUp> meetUps;
    private List<DBDocument> tempDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);
        meetupList = (ListView) findViewById(R.id.listView);
        meetUps = new ArrayList<>();
        init();

    }

    private void init() {
/*
        meetUps.add(new MeetUp("100","Baum", "Fest Hos Baum", new Coordinates(57.704330, 11.963180), "fet fest BYOB"));
        meetUps.add(new MeetUp("101","Nils", "Fest Hos Nils", new Coordinates(57.704330, 11.973180), "fet fest BYOB"));
        meetUps.add(new MeetUp("102","Mejborn", "Fest Hos Mejborn", new Coordinates(57.704330, 11.983180), "fet fest BYOB"));
        meetUps.add(new MeetUp("103","Baum", "Fest Hos Baum", new Coordinates(57.704330, 11.963180), "fet fest BYOB"));
        meetUps.add(new MeetUp("104","Nils", "Fest Hos Nils", new Coordinates(57.704330, 11.973180), "fet fest BYOB"));
        meetUps.add(new MeetUp("105","Mejborn", "Fest Hos Mejborn", new Coordinates(57.704330, 11.983180), "fet fest BYOB"));
        meetUps.add(new MeetUp("106","Baum", "Fest Hos Baum", new Coordinates(57.704330, 11.963180), "fet fest BYOB"));
        meetUps.add(new MeetUp("107","Nils", "Fest Hos Nils", new Coordinates(57.704330, 11.973180), "fet fest BYOB"));
        meetUps.add(new MeetUp("108","Mejborn", "Fest Hos Mejborn", new Coordinates(57.704330, 11.983180), "fet fest BYOB"));
        meetUps.add(new MeetUp("109","Baum", "Fest Hos Baum", new Coordinates(57.704330, 11.963180), "fet fest BYOB"));
        meetUps.add(new MeetUp("110","Nils", "Fest Hos Nils", new Coordinates(57.704330, 11.973180), "fet fest BYOB"));
        meetUps.add(new MeetUp("111","Mejborn", "Fest Hos Mejborn", new Coordinates(57.704330, 11.983180), "fet fest BYOB"));
*/
        updateList();
        MeetupListAdapter adapter = new MeetupListAdapter(this, R.layout.activity_meetuplistobject, meetUps);
        meetupList.setAdapter(adapter);
    }



    private void updateList(){
        tempDocument = new ArrayList<DBDocument>();
        DBCollection allMeetUps = Database.getInstance().meetups();
        allMeetUps.all(new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> tempDocument) {
                super.onSuccess(tempDocument);
            }
        });

        for (DBDocument doc : tempDocument) {
            doc.init(new RequestListener<DBDocument>() {
                @Override
                public void onSuccess(DBDocument document) {
                    super.onSuccess(document);
                    System.out.println("FUNKA DÅÅÅÅ" + document.id());

                    MeetUp newMeetUp = convertDocToMeetUp(document);
                    meetUps.add(newMeetUp);

                    System.out.println("ADDED MEETUP: " + newMeetUp);
                }
            });
        }
    }

    private MeetUp convertDocToMeetUp(DBDocument meetUpDoc) {
        String id = meetUpDoc.id();
        String creatorID = (String) meetUpDoc.get("creator");
        String name = (String) meetUpDoc.get("name");
        Double coord_lat = (Double) meetUpDoc.get("coord_lat");
        Double coord_lon = (Double) meetUpDoc.get("coord_lon");
        String description = (String) meetUpDoc.get("description");
        MeetUp.Categories category = MeetUp.getCategoryFromString((String) meetUpDoc.get("category"));
        Long maxAttendees = (Long) meetUpDoc.get("maxattendees");
        /*Calendar startDate = (Calendar) meetUpDoc.get("startdate");
        Calendar endDate = (Calendar) meetUpDoc.get("enddate");*/
        MeetUp.Visibility visibility = MeetUp.getVisibilityFromString((String) meetUpDoc.get("visibility"));

        if (id == null || name == null || coord_lat == null || coord_lon == null
                || description == null || category == null || maxAttendees == null) {//|| startDate == null || endDate == null) {//TODO put this back
            return null;
        }

        return new MeetUp(id, creatorID, name, new Coordinates(coord_lat, coord_lon), description, category,
                maxAttendees, null, null, visibility);
    }
}

/* Todo: Create two dummy meetup objects and fill with the information we want. (Name and Date/time)
    Create ArrayList with Meetup-objects
    Create class ArrayListAdapter
   */




    //To-do: connect meetup-list to meetup-objects.

