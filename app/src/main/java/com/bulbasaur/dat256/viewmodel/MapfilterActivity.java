package com.bulbasaur.dat256.viewmodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.bulbasaur.dat256.model.MeetUp.Categories.EDUCATION;
import static com.bulbasaur.dat256.model.MeetUp.Categories.FOOD;
import static com.bulbasaur.dat256.model.MeetUp.Categories.GAMES;
import static com.bulbasaur.dat256.model.MeetUp.Categories.PARTY;
import static com.bulbasaur.dat256.model.MeetUp.Categories.SPORTS;

public class MapfilterActivity extends AppCompatActivity {
    protected Map<MeetUp.Categories, Boolean> boolsOfMeetUp;
   private Switch sport, food, education, games, party;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfilter);
        boolsOfMeetUp = new TreeMap<>();
        sport = findViewById(R.id.sports);
        education = findViewById(R.id.education);
        food = findViewById(R.id.food);
        games = findViewById(R.id.game);
        party = findViewById(R.id.party);

        boolsOfMeetUp.put(SPORTS, true);
        boolsOfMeetUp.put(EDUCATION, true);
        boolsOfMeetUp.put(FOOD, true);
        boolsOfMeetUp.put(GAMES, true);
        boolsOfMeetUp.put(PARTY, true);

        sport.setOnClickListener( v -> {
            if(sport.isChecked()){
                boolsOfMeetUp.replace(SPORTS, false, true);
            }
            else{
                boolsOfMeetUp.replace(SPORTS, true, false);
            }
        });

        education.setOnClickListener( v -> {
            if(education.isChecked()){
                boolsOfMeetUp.replace(EDUCATION, false, true);
            }
            else{
                boolsOfMeetUp.replace(EDUCATION, true, false);
            }
        });

        food.setOnClickListener( v -> {
            if(food.isChecked()){
                boolsOfMeetUp.replace(FOOD, false, true);
            }
            else{
                boolsOfMeetUp.replace(FOOD, true, false);
            }
        });

        games.setOnClickListener( v -> {
            if(games.isChecked()){
                boolsOfMeetUp.replace(GAMES, false, true);
            }
            else{
                boolsOfMeetUp.replace(GAMES, true, false);
            }
        });

        party.setOnClickListener( v -> {
            if(party.isChecked()){
                boolsOfMeetUp.replace(PARTY, false, true);
            }
            else{
                boolsOfMeetUp.replace(PARTY, true, false);
            }
        });
    }

    public Map<MeetUp.Categories, Boolean> getBoolsOfMeetUp(){
        return boolsOfMeetUp;
    }
}
