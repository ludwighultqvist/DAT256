package com.bulbasaur.dat256.viewmodel.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.MeetUpActivity;
import com.bulbasaur.dat256.viewmodel.MenuActivity;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;

import java.text.DateFormat;
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

        /*
        List<MeetUp> meetUps = new ArrayList<>();
        meetUps.add(new MeetUp("name", "date", "desc"));
        meetUps.add(new MeetUp("name", "date", "desc"));
        meetUps.add(new MeetUp("name", "date", "desc"));
        meetUps.add(new MeetUp("name", "date", "desc"));

        meetUpSection("Test meetups", meetUps);
        adapter.notifyDataSetChanged();
        */
        test();

        return view;
    }

    private void test() {
        List<MeetUp> meetUps = new ArrayList<>();
        Database.getInstance().meetups().all(new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> documents) {
                super.onSuccess(documents);
                for (DBDocument document : documents) {
                    document.init(new RequestListener<DBDocument>() {
                        @Override
                        public void onSuccess(DBDocument doc) {
                            super.onSuccess(doc);
                            MeetUp meetUp = Helpers.convertDocToMeetUp(doc);

                            if (meetUp != null) {
                                meetUps.add(meetUp);
                            }

                            documents.remove(doc);

                            if (5 <= meetUps.size() || documents.isEmpty()) {
                                meetUpSection("Test meetups", new ArrayList<>(meetUps));
                                meetUps.clear();
                            }

                            if (documents.isEmpty()) {
                                refresh();
                            }
                        }
                    });
                }
            }
        });
    }

    void meetUpSection(@NonNull String title, @NonNull List<MeetUp> meetUps) {
        adapter.add(new Section<MeetUp>(title, meetUps) {
            @Override
            View inflate(MeetUp item) {
                View view = getLayoutInflater().inflate(R.layout.activity_meetuplistobject, null);
                TextView name = view.findViewById(R.id.meetupName);
                TextView date = view.findViewById(R.id.meetupDate);
                TextView desc = view.findViewById(R.id.meetupDesc);

                name.setText(item.getName());
                date.setText(DateFormat.getDateTimeInstance().format(item.getStart().getTime()));
                desc.setText(item.getDescription());

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), MeetUpActivity.class);
                    intent.putExtra("MeetUp", item);
                    startActivityForResult(intent, MenuActivity.SHOW_EVENT_ON_MAP_CODE);
                });

                return view;
            }
        });
    }

    void refresh() {
        adapter.notifyDataSetChanged();
    }

    private class SectionsAdapter extends ArrayAdapter<Section> {

        private SectionsAdapter(Context context) {
            super(context, R.layout.fragment_section, R.id.title, new ArrayList<>());
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView title = view.findViewById(R.id.title);
            //ListView list = view.findViewById(R.id.list);
            LinearLayout linear = view.findViewById(R.id.linear);

            Section<?> section = getItem(position);

            if (section != null) {
                title.setText(section.getTitle());
                //list.setAdapter(section.getAdapter());

                linear.removeAllViews();
                for (View v : section.getViews()) {
                    linear.addView(v);
                }
                linear.invalidate();
            }

            view.setClickable(false);

            return view;
        }
    }

    private abstract class Section<E> {
        private String title;
        private List<E> items;

        private Section(String title, List<E> items) {
            this.title = title;
            this.items = items;
        }

        public String getTitle() {
            return title;
        }

        private List<View> getViews() {
            List<View> views = new ArrayList<>();
            for (E item : items) {
                views.add(inflate(item));
            }
            return views;
        }

        abstract View inflate(E item);
    }



    /*
    void addUserSection(@NonNull String title, @NonNull List<User> users) {
        System.out.println(users.toString());
        if (getContext() != null) {
            adapter.add(new Section(title, new UserAdapter(getContext(), users)));
        }
    }

    void addMeetUpSection(@NonNull String title, @NonNull List<MeetUp> meetUps) {
        if (getContext() != null) {
            adapter.add(new Section(title, new MeetupAdapter(getContext(), meetUps)));
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

    private class MeetupAdapter extends ArrayAdapter<MeetUp> {

        private MeetupAdapter(@NonNull Context context, @NonNull List<MeetUp> meetUps) {
            super(context, R.layout.activity_meetuplistobject, R.id.meetupName, meetUps);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            System.out.println("YAYAYAYAYYAYAYAYAYAYA numero " + position);

            TextView name = view.findViewById(R.id.meetupName);
            TextView date = view.findViewById(R.id.meetupDate);
            TextView desc = view.findViewById(R.id.meetupDesc);

            MeetUp meetUp = getItem(position);

            if (meetUp != null) {
                name.setText(meetUp.getName());
                //date.setText(DateFormat.getDateTimeInstance().format(meetUp.getStart().getTime()));
                date.setText("date");
                desc.setText(meetUp.getDescription());
            }

            return view;
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
    */
}
