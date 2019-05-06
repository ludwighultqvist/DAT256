package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.services.firebase.Database;

public class ListActivity extends AppCompatActivity {
    private ListView meetupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);
        meetupList = (ListView) findViewById(R.id.listView);

    }

/* Todo: Create two dummy meetup objects and fill with the information we want. (Name and Date/time)
    Create ArrayList with Meetup-objects
    Create class ArrayListAdapter
   */




    //To-do: connect meetup-list to meetup-objects.
}
