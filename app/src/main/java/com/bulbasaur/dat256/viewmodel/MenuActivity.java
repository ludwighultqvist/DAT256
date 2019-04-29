package com.bulbasaur.dat256.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.Coordinates;
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MapBounds;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.MeetUp.Categories;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.QueryFilter;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.uielements.CustomInfoWindowAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.MarkerData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout drawer;
    private SearchView searchView;

    private GoogleMap map;

    private MeetUp fakeMeetUp;
    private LatLng fakeMeetUpCoordinates;

    private static final int SHOW_EVENT_ON_MAP_CODE = 1;
    private static final int DEFAULT_MEET_UP_ZOOM_LEVEL = 15;

    private boolean markerInMiddle = false;

    private Main main;

    private List<DBDocument> myMeetUpsDocs;

    private List<? extends DBDocument> myMeetUpsDocsLat, myMeetUpsDocsLon;
  
    private Coordinates currentCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        main = new Main();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        searchView = findViewById(R.id.search_bar);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search for users or meet ups");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_profile:
                    break;
                case R.id.nav_qr:
                    startActivity(new Intent(this, ScanQRActivity.class));
                    break;
                case R.id.nav_settings:
                    break;
                case R.id.nav_connect_bitmoji:
                    startActivity(new Intent(this, ConnectSnapchatActivity.class));
                    break;
                case R.id.nav_login_logout:
                    startActivity(new Intent(this, RegisterActivity.class));
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> startActivity(new Intent(this, CreateMeetUpActivity.class)));

        fakeMeetUp = new MeetUp(0, "Fest hos Hassan", 57.714957, 11.909446, "Yippie!", Categories.PARTY, 0, null, null);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        fakeMeetUpCoordinates = new LatLng(fakeMeetUp.getLatitude(), fakeMeetUp.getLongitude());

        MarkerData markerData = new MarkerData(fakeMeetUp.getName(), fakeMeetUp.getCategory().primaryColor, fakeMeetUp.getDescription(), fakeMeetUp.getCategory().primaryColor);
        Gson markerDataGson = new Gson();
        String markerDataString = markerDataGson.toJson(markerData);
        MarkerOptions markerOptions = new MarkerOptions().position(fakeMeetUpCoordinates).snippet(markerDataString).icon(BitmapDescriptorFactory.fromBitmap(fakeMeetUp.getIconBitmap(this))).anchor(0.5f, 0.5f).alpha(0.6f);
        Marker marker = this.map.addMarker(markerOptions);


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
                this.map.moveCamera(CameraUpdateFactory.newLatLng(lastLocationCoords));
                this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationCoords,DEFAULT_MEET_UP_ZOOM_LEVEL));
                Marker me = map.addMarker(new MarkerOptions().position(lastLocationCoords).title("Your location"));
            }
        });


        this.map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(m -> onMeetUpMarkerClick());
        this.map.setOnInfoWindowLongClickListener(m -> joinMarkedMeetUp());
        this.map.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                marker.hideInfoWindow();
            }
        });
        this.map.setOnCameraIdleListener(() -> {
            System.out.println("camera idle - meetups update");

            requestNewMapItemsAndUpdate(getCurrentMapBounds());
        });
    }

    private void joinMarkedMeetUp() {
        //TODO make the user join the marked event here
        Toast.makeText(this, "Joined " + fakeMeetUp.getName(), Toast.LENGTH_SHORT).show();
    }

    private void onMeetUpMarkerClick() {
        Intent meetUpIntent = new Intent(this, MeetUpActivity.class);
        meetUpIntent.putExtra("MeetUp", fakeMeetUp);
        startActivityForResult(meetUpIntent, SHOW_EVENT_ON_MAP_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_EVENT_ON_MAP_CODE) {
            if (resultCode == RESULT_OK) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(fakeMeetUpCoordinates, DEFAULT_MEET_UP_ZOOM_LEVEL));
            }
        }
    }

    private MapBounds getCurrentMapBounds() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        return new MapBounds(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude);
    }

    private void requestNewMapItemsAndUpdate(MapBounds bounds) {
        System.out.println("creating request");

        DBDocument user = Database.getInstance().user();

        if (user == null) return;

        System.out.println("my database exists");

        DBCollection myMeetupsDatabase = user.subCollection("meetups");

        if (myMeetupsDatabase == null) return;

        System.out.println("my meetups database exists");

        List<QueryFilter> latitudeFilter = new LinkedList<>();
        latitudeFilter.add(new QueryFilter("latitude", ">", bounds.getBottomLeft().lat));
        latitudeFilter.add(new QueryFilter("latitude", "<", bounds.getTopRight().lat));

        List<QueryFilter> longitudeFilter = new LinkedList<>();
        longitudeFilter.add(new QueryFilter("longitude", ">", bounds.getBottomLeft().lon));
        longitudeFilter.add(new QueryFilter("longitude", "<", bounds.getTopRight().lon));

        System.out.println("query created");

        search(myMeetupsDatabase, latitudeFilter, longitudeFilter);
    }

    private void search(DBCollection collection, List<QueryFilter> latFilter, List<QueryFilter> lonFilter) {
        System.out.println("searching with query 1");

        collection.search(latFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> latFilteredDocs) {
                myMeetUpsDocsLat = latFilteredDocs;

                System.out.println("success 1: searching with query 2");

                collection.search(lonFilter, new RequestListener<List<? extends DBDocument>>() {
                    @Override
                    public void onSuccess(List<? extends DBDocument> lonFilteredDocs) {
                        myMeetUpsDocsLon = lonFilteredDocs;

                        System.out.println("successful search queries 1 & 2");

                        myMeetUpsDocs = intersection((List<DBDocument>) myMeetUpsDocsLat, (List<DBDocument>) myMeetUpsDocsLon);

                        main.updateMapMeetUps(convertDocsToMeetUps(myMeetUpsDocs));
                    }
                });
            }
        });
    }

    private List<DBDocument> intersection(List<DBDocument> first, List<DBDocument> second) {
        for (DBDocument s : second) {
            if (!first.contains(s)) {
                first.add(s);
            }
        }

        return first;
    }

    private List<MeetUp> convertDocsToMeetUps(List<? extends DBDocument> meetUpDocs) {
        List<MeetUp> meetUps = new ArrayList<>();

        for (DBDocument doc : meetUpDocs) {
            meetUps.add(convertDocToMeetUp(doc));
        }

        return meetUps;
    }

    private MeetUp convertDocToMeetUp(DBDocument meetUpDoc) {
        return new MeetUp(
                Long.parseLong(meetUpDoc.id()),
                (String) meetUpDoc.get("name"),
                (Coordinates) meetUpDoc.get("coordinates"),
                (String) meetUpDoc.get("description"),
                (Categories) meetUpDoc.get("category"),
                (Integer) meetUpDoc.get("maxattendees"),
                (Calendar) meetUpDoc.get("startdate"),
                (Calendar) meetUpDoc.get("enddate"));
    }
}
