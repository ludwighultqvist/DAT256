package com.bulbasaur.dat256.viewmodel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MeetUp;

public class MapFilterActivityV2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filter_v2);

        LinearLayout categoryFilterLayout = findViewById(R.id.map_filter_v2_linear_layout);
        for (MeetUp.Categories c : MeetUp.Categories.values()) {
            View categorySwitchView = getLayoutInflater().inflate(R.layout.category_filter_switch, null);
            ((Switch) categorySwitchView).setText(c.categoryName);
            ((Switch) categorySwitchView).setChecked(Main.getInstance().getCategoryFilters().get(c));
            ((Switch) categorySwitchView).setOnCheckedChangeListener((buttonView, isChecked) -> Main.getInstance().getCategoryFilters().put(c, isChecked));
            categoryFilterLayout.addView(categorySwitchView);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);

        super.onBackPressed();
    }
}