package com.bulbasaur.dat256.viewmodel;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.User;

import com.bulbasaur.dat256.viewmodel.profileTabs.ProfileTabFragment;

import java.util.ArrayList;
import java.util.List;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;


public class UserActivity extends AppCompatActivity {

    private User user;

    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getSerializableExtra("User");

        TextView nameView = findViewById(R.id.firstAndLastName);
        nameView.setText(user.getFirstName() + " " + user.getLastName());

        TextView phoneNumberView = findViewById(R.id.phoneNumberTextView);
        phoneNumberView.setText("" + user.getPhoneNumber());

        TextView meetAppScore = findViewById(R.id.meetAppScore);
        if (user.getScore() == null) {
            meetAppScore.setText("MeetAppScore: 0");
        } else {
            meetAppScore.setText("MeetAppScore: " + user.getScore());
        }

        TabLayout tabs = findViewById(R.id.profileTabs);
        tabs.bringToFront();
        ViewPager pager = findViewById(R.id.pager);
        List<ProfileTabFragment> fragments = new ArrayList<>(5);

        tabs.addTab(tabs.newTab().setText("MeetUps Joined"));
        ProfileTabFragment joined = new ProfileTabFragment();
        Bundle joinedBundle = new Bundle();
        joinedBundle.putStringArrayList("myList", user.getJoinedMeetUps());
        joined.setArguments(joinedBundle);
        fragments.add(joined);


        tabs.addTab(tabs.newTab().setText("Created MeetUps"));
        ProfileTabFragment created = new ProfileTabFragment();
        Bundle createdBundle = new Bundle();
        createdBundle.putStringArrayList("myList", user.getCreatedMeetUps());
        created.setArguments(createdBundle);

        fragments.add(created);

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        pager.setAdapter(new TabsAdapter(getSupportFragmentManager(), fragments));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ImageView profilePicture = findViewById(R.id.profilePicture);

        if (Helpers.isLoggedIn() && Main.getInstance().getCurrentUser().equals(user)) {
            qrCodeBitmap = Helpers.generateQRCode("USER" + user.getId(), 800);
            profilePicture.setImageBitmap(qrCodeBitmap);
            profilePicture.setVisibility(View.VISIBLE);
        } else {
            profilePicture.setVisibility(View.GONE);
        }
    }

    private class TabsAdapter extends FragmentPagerAdapter {
        private List<ProfileTabFragment> fragments;

        private TabsAdapter(FragmentManager fm, @NonNull List<ProfileTabFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return (0 <= i && i < fragments.size()) ? fragments.get(i) : null;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        /*Button showQRCodeButton = findViewById(R.id.showQRCodeButton);
        if (qrCodeBitmap == null) {
            showQRCodeButton.setVisibility(View.INVISIBLE);
        } else {
            showQRCodeButton.setOnClickListener(v -> {
                if (!showingQRCode) {
                    meetUpPicture.setImageBitmap(qrCodeBitmap);
                    showQRCodeButton.setText(R.string.showMeetUpPicture);
                    showingQRCode = true;
                } else {
                    meetUpPicture.setImageDrawable(getDrawable(meetUp.getCategory().pic));
                    showQRCodeButton.setText(R.string.showQRCode);
                    showingQRCode = false;
                }
            });
        }*/
    }
}
