package com.bulbasaur.dat256.viewmodel.profileTabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.MeetUpActivity;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class ProfileTabFragment extends Fragment {

    ProfileListAdapter adapter;
    List<String> meetups;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        ListView list = view.findViewById(R.id.profileList);
        List<MeetUp> muList = new ArrayList<>();

        meetups = getArguments().getStringArrayList("myList");

        if (meetups.isEmpty()) {
            System.out.println("Profile fragment - list is empty");
        }

        adapter = new ProfileListAdapter(getContext(), muList);
        list.setAdapter(adapter);

        for (String id : meetups) {
            Database.getInstance().meetups().get(id, new RequestListener<DBDocument>(true) {
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);
                    MeetUp mu = Helpers.convertDocToMeetUp(object);
                    muList.add(mu);
                    System.out.println("" + muList.size());
                    System.out.println("" + meetups.size());
                    if(muList.size() == meetups.size()){
                        adapter.notifyDataSetInvalidated();
                    }
                }
            });
        }

        return view;
    }


    private class ProfileListAdapter extends ArrayAdapter<MeetUp>{
        private ProfileListAdapter(Context context, List<MeetUp> list){
            super(context,R.layout.activity_meetuplistobject, R.id.meetupName, list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            MeetUp item = getItem(position);
            if (item != null){
                TextView name = view.findViewById(R.id.meetupName);
                TextView date = view.findViewById(R.id.meetupDate);
                TextView desc = view.findViewById(R.id.meetupDesc);

                name.setText(item.getName());
                date.setText(DateFormat.getDateTimeInstance().format(item.getStart().getTime()));
                desc.setText(item.getDescription());

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), MeetUpActivity.class);
                    intent.putExtra("MeetUp", item);
                    startActivity(intent);
                });
            }
            return view;
        }
    }
}
