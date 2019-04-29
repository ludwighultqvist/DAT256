package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.Toast;

import com.bulbasaur.dat256.R;
import com.bulbasaur.dat256.model.MapBounds;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.viewmodel.uielements.CustomInfoWindowAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.MarkerData;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

        fakeMeetUp = new MeetUp(0, "Fest hos Hassan", 57.714957, 11.909446, "Yippie!", MeetUp.Categories.PARTY, 0, null, null);
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
        this.map.moveCamera(CameraUpdateFactory.newLatLng(fakeMeetUpCoordinates));

        this.map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(m -> onMeetUpMarkerClick());
        this.map.setOnInfoWindowLongClickListener(m -> joinMarkedMeetUp());
        this.map.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                marker.hideInfoWindow();
            }
        });
        this.map.setOnCameraIdleListener(() -> {
            //get events and friends within the new map coordinates
            //unload events and friends outside the new map coordinates

            updateMapItems(requestNewMapItems(getCurrentMapBounds()));

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

    private List<? extends DBDocument> requestNewMapItems(MapBounds bounds) {
        //DBCollection newVisibleFriends = Database.getInstance().users(main.currentUser.id, "friends", new RequestListener(){onSuccuss()});

        Database.getInstance().user().get("friends")
                ref.query (, , //put request listener here);
                )
        newVisibleFriends
    }

    private void updateMapItems(List<? extends DBDocument> newMapItems) {
        DBCollection asdf = Database.getInstance().meetups();
        List<? extends DBDocument> asdd = asdf.all();
        for (DBDocument asdf :)
        for (DBDocument e : asdd) {
            double longit = (Double) e.get("longitude");
        }


    }
}
