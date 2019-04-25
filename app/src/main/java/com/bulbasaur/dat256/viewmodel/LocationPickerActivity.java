package com.bulbasaur.dat256.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPickerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private Coordinates coordinatesToBePicked = new Coordinates();

    private LocationManager locationManager;

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

        map.setOnMarkerClickListener(marker -> true);

        //set map position to lat 0 lon 0 just in case we can't get the user location
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //set map to user position if possible
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng lastLocationCoords = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println(lastLocationCoords.latitude + " " + lastLocationCoords.longitude);
                Marker marker = map.addMarker(new MarkerOptions().position(lastLocationCoords).title("Your location"));
                marker.showInfoWindow();
                map.moveCamera(CameraUpdateFactory.newLatLng(lastLocationCoords));
            }
        });
    }
}
