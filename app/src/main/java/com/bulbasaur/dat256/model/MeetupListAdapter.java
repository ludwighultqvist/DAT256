package com.bulbasaur.dat256.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bulbasaur.dat256.R;

import java.util.ArrayList;
import java.util.List;


public class MeetupListAdapter extends ArrayAdapter<MeetUp> {
    private Context mContext;
    int mResource;

    public MeetupListAdapter(Context context, int resource, List<MeetUp> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource= resource;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String name = getItem(position).getName();
        String desc = getItem(position).getDescription();

        MeetUp meetup = new MeetUp(name, desc);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName =(TextView) convertView.findViewById(R.id.meetupName);
        TextView tvDesc =(TextView) convertView.findViewById(R.id.meetupDesc);

        tvName.setText(name);
        tvDesc.setText(desc);

        return convertView;
    }
}

