package com.bulbasaur.dat256.viewmodel.discover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {
    SectionsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        ListView list = view.findViewById(R.id.list);
        adapter = new SectionsAdapter(getContext());
        list.setAdapter(adapter);

        return view;
    }

    void addUserSection(@NonNull String title, @NonNull List<User> users) {
        if (getContext() != null) {
            adapter.add(new Section(title, new UserAdapter(getContext(), users)));
        }
    }

    void addMeetUpSection(@NonNull String title, @NonNull List<MeetUp> meetUps) {
        if (getContext() != null) {
            adapter.add(new Section(title, new MeetupAdapter(getContext(), meetUps)));
        }
    }

    void refresh() {
        adapter.notifyDataSetChanged();
    }

    private class SectionsAdapter extends ArrayAdapter<Section> {

        private SectionsAdapter(Context context) {
            super(context, R.layout.fragment_section, new ArrayList<>());
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView title = view.findViewById(0);
            ListView list = view.findViewById(0);

            Section section = getItem(position);

            if (section != null) {
                title.setText(section.getTitle());
                list.setAdapter(section.getAdapter());
            }

            return view;
        }
    }

    private class Section {
        String title;
        ArrayAdapter adapter;

        private Section(String title, ArrayAdapter adapter) {
            this.title = title;
            this.adapter = adapter;
        }

        private String getTitle() {
            return title;
        }

        private ArrayAdapter getAdapter() {
            return adapter;
        }
    }

    private class UserAdapter extends ArrayAdapter<User> {

        private UserAdapter(@NonNull Context context, @NonNull List<User> users) {
            super(context, 0, users);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            User user = getItem(position);

            if (user != null) {
                // set values
            }

            return view;
        }
    }

    private class MeetupAdapter extends ArrayAdapter<MeetUp> {

        private MeetupAdapter(@NonNull Context context, @NonNull List<MeetUp> meetUps) {
            super(context, 0, meetUps);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            MeetUp meetUp = getItem(position);

            if (meetUp != null) {
                // set values
            }

            return view;
        }
    }
}
