package com.bulbasaur.dat256.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPickerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private Coordinates coordinatesToBePicked = new Coordinates();

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_picker_map);
        mapFragment.getMapAsync(this);

        Toolbar locationPickerToolbar = findViewById(R.id.locationPickerToolbar);
        locationPickerToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        locationPickerToolbar.setNavigationOnClickListener(v -> finish());

        ImageView pickThisLocationImageView = findViewById(R.id.pickThisLocationIcon);
        pickThisLocationImageView.setOnClickListener(v -> {
            coordinatesToBePicked.lat = map.getCameraPosition().target.latitude;
            coordinatesToBePicked.lon = map.getCameraPosition().target.longitude;

            Intent locationPickedIntent = new Intent();
            locationPickedIntent.putExtra("Coordinates", coordinatesToBePicked);
            setResult(Activity.RESULT_OK, locationPickedIntent);
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //set map position to lat 0 lon 0 just in case we can't get the user location
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //set map position to user position
        if (lastKnownLocation != null) {
            LatLng lastLocationCoords = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(lastLocationCoords).title("Your location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(lastLocationCoords));
        }
    }
}
