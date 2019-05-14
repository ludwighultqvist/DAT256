package com.bulbasaur.dat256.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ListActivity extends AppCompatActivity {
    private ListView meetupList;
    private List<MeetUp> meetUps = new ArrayList<MeetUp>();
    private List<? extends DBDocument> tempDocument;
    EditText searchBar;


    MeetupListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);
        meetupList = (ListView) findViewById(R.id.listView);
        searchBar = (EditText) findViewById(R.id.Search);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ListActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        nameSorter();
        dateSorter();
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
    protected void nameSorter() {
        Comparator<MeetUp> c = new Comparator<MeetUp>() {
            @Override
            public int compare(MeetUp o1, MeetUp o2) {

                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        ArrayList<MeetUp> a = new ArrayList<MeetUp>();
        for(MeetUp i: meetUps)
        System.out.println("original list " + i.getName());
        Collections.sort(meetUps,c);
        for(MeetUp k: meetUps)
        System.out.println("sorted list " + k.getName());
    }
    protected void dateSorter() {
        Comparator<MeetUp> c = new Comparator<MeetUp>() {
            @Override
            public int compare(MeetUp o1, MeetUp o2) {
                Date date1 = o1.getStart().getTime();
                DateFormat format1 = new SimpleDateFormat("MM-dd HH:mm");
                String strDate1 = format1.format(date1);
                Date date2 = o2.getStart().getTime();
                DateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");
                String strDate2 = format2.format(date2);
                return strDate1.compareToIgnoreCase(strDate2);
            }
        };
        ArrayList<MeetUp> a = new ArrayList<MeetUp>();
        for(MeetUp i: meetUps)
            System.out.println("dateUnsorted list " + i.getName());
        Collections.sort(meetUps,c);
        for(MeetUp k: meetUps)
            System.out.println("dateSorted " + k.getName());
    }
}






