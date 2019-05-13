package com.bulbasaur.dat256.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bulbasaur.dat256.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MeetupListAdapter extends ArrayAdapter<MeetUp> {
    private Context mContext;
    int mResource;
    Date date;


    public MeetupListAdapter(Context context, int resource, List<MeetUp> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource= resource;

    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String name = getItem(position).getName();
        date = getItem(position).getStart().getTime();
        String desc = getItem(position).getDescription();
        DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String strDate = format.format(date);

        MeetUp meetup = new MeetUp(name,strDate,desc);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName =(TextView) convertView.findViewById(R.id.meetupName);
        TextView tvDate =(TextView) convertView.findViewById(R.id.meetupDate);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.meetupDesc);

        tvName.setText(name);
        tvDate.setText(strDate);
        tvDesc.setText(desc);

        return convertView;
    }
}

