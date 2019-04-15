package com.bulbasaur.dat256.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Country;

public class CountrySpinnerAdapter extends ArrayAdapter<String> {

    private Context context;

    CountrySpinnerAdapter(@NonNull Context context) {
        super(context, R.layout.phone_number_spinner_item);
        this.context = context;
    }

    @Override
    public int getCount() {
        return Country.values().length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.phone_number_spinner_item, parent, false);
            viewHolder.countryIcon = convertView.findViewById(R.id.country_icon);
            viewHolder.countryCode = convertView.findViewById(R.id.country_code);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.countryIcon.setImageResource(Country.values()[position].iconResource);
        viewHolder.countryCode.setText(Country.values()[position].countryCodeVisual);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView countryIcon;
        TextView countryCode;
    }
}
