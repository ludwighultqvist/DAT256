package com.bulbasaur.dat256.viewmodel.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.MeetUpActivity;
import com.bulbasaur.dat256.viewmodel.MenuActivity;
import com.bulbasaur.dat256.viewmodel.UserActivity;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;
import com.google.rpc.Help;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {
    private SectionsAdapter adapter;
    private OnUpdateListener onUpdateListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        ListView list = view.findViewById(R.id.list);
        adapter = new SectionsAdapter(getContext());
        list.setAdapter(adapter);

        if (onUpdateListener != null) {
            onUpdateListener.onUpdate();
        }

        return view;
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
                    startActivity(intent);
                });

                return view;
            }

        });
    }

    void userSection(@NonNull String title, @NonNull List<User> users) {
        adapter.add(new Section<User>(title, users) {
            @Override
            View inflate(User item) {
                View view = getLayoutInflater().inflate(R.layout.activity_meetuplistobject, null);
                TextView name = view.findViewById(R.id.meetupName);
                TextView date = view.findViewById(R.id.meetupDate);
                TextView desc = view.findViewById(R.id.meetupDesc);

                name.setText(item.getFirstName() + " " + item.getLastName());
                date.setText(item.getId());
                desc.setText(item.getCoordinates().toString());

                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), UserActivity.class);
                    intent.putExtra("User", item);
                    startActivity(intent);
                });

                return view;
            }

        });
    }

    interface OnUpdateListener {
        void onUpdate();
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
