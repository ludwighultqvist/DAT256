package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MeetUp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MeetUp fakeMeetUp;

    private LatLng hassansFest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fakeMeetUp = new MeetUp();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        hassansFest = new LatLng(fakeMeetUp.getLatitude(), fakeMeetUp.getLongitude());
        mMap.addMarker(new MarkerOptions().position(hassansFest).title(fakeMeetUp.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hassansFest));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onMeetUpMarkerClick();

                return true;
            }
        });
    }

    private void onMeetUpMarkerClick() {
        Intent meetUpIntent = new Intent(this, MeetUpActivity.class);
        meetUpIntent.putExtra("MeetUp", fakeMeetUp);
        startActivityForResult(meetUpIntent, SHOW_EVENT_ON_MAP);
    }

    static final int SHOW_EVENT_ON_MAP = 0;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_EVENT_ON_MAP) {
            if (resultCode == RESULT_OK) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hassansFest, 10));
            }
        }
    }
}
