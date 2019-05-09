package com.bulbasaur.dat256.viewmodel.uielements;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bulbasaur.dat256.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;

    private final View mContents;

    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        mWindow = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_info_window, null);
        mContents = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        Gson gson = new Gson();
        MarkerData markerData = gson.fromJson(marker.getSnippet(), MarkerData.class);

        /*int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.
        if (marker.equals(mBrisbane)) {
            badge = R.drawable.badge_qld;
        } else if (marker.equals(mAdelaide)) {
            badge = R.drawable.badge_sa;
        } else if (marker.equals(mSydney)) {
            badge = R.drawable.badge_nsw;
        } else if (marker.equals(mMelbourne)) {
            badge = R.drawable.badge_victoria;
        } else if (marker.equals(mPerth)) {
            badge = R.drawable.badge_wa;
        } else if (marker.equals(mDarwin1)) {
            badge = R.drawable.badge_nt;
        } else if (marker.equals(mDarwin2)) {
            badge = R.drawable.badge_nt;
        } else if (marker.equals(mDarwin3)) {
            badge = R.drawable.badge_nt;
        } else if (marker.equals(mDarwin4)) {
            badge = R.drawable.badge_nt;
        } else {
            // Passing 0 to setImageResource will clear the image view.
            badge = 0;
        }
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);*/
        if(markerData.isMeetUpOrNah()) {
            String title = markerData.getTitle();
            TextView titleUi = view.findViewById(R.id.title);
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                System.out.println(markerData.getTitleColor());
                titleText.setSpan(new ForegroundColorSpan(context.getColor(markerData.getTitleColor())), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = markerData.getDescription();
            String joinText = context.getString(R.string.join_by_long_press);
            TextView snippetUi = view.findViewById(R.id.snippet);
            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet + "\n\n" + joinText);

                snippetText.setSpan(new ForegroundColorSpan(context.getColor(markerData.getDescriptionColor())), 0, markerData.getDescription().length(), 0);

                //snippetText.setSpan(new ForegroundColorSpan(context.getColor(R.color.mainColor)), markerData.getDescription().length(), snippet.length(), 0);
                //snippetText.setSpan(new StyleSpan(Typeface.BOLD), markerData.getDescription().length(), snippet.length(), 0);
                //snippetText.setSpan(new RelativeSizeSpan(2f), markerData.getDescription().length(), snippet.length(), 0);

                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }else{
            String title = markerData.getTitle();
            TextView titleUi = view.findViewById(R.id.title);
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                System.out.println(markerData.getTitleColor());
                titleText.setSpan(new ForegroundColorSpan(context.getColor(markerData.getTitleColor())), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = markerData.getDescription();
            String goToFriendPage = context.getString(R.string.go_to_friend_page);
            TextView snippetUi = view.findViewById(R.id.snippet);
            if (snippet != null) {
                SpannableString snippetText = new SpannableString(snippet + "\n\n" + goToFriendPage);

                snippetText.setSpan(new ForegroundColorSpan(context.getColor(markerData.getDescriptionColor())), 0, markerData.getDescription().length(), 0);

                snippetUi.setText(snippetText);
            }
        }
    }
}