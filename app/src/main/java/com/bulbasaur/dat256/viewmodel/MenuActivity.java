package com.bulbasaur.dat256.viewmodel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.viewmodel.uielements.CustomInfoWindowAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.MarkerData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.login.models.MeData;
import com.snapchat.kit.sdk.login.models.UserDataResponse;
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout drawer;
    private SearchView searchView;

    private GoogleMap map;

    private MeetUp fakeMeetUp;
    private LatLng fakeMeetUpCoordinates;
    private User fakeUser;
    private LatLng fakeUserCoordinates;

    private static final int SHOW_EVENT_ON_MAP_CODE = 1;
    private static final int DEFAULT_MEET_UP_ZOOM_LEVEL = 15;

    private boolean markerInMiddle = false;

    MarkerOptions markerOptionsUser = null;

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
        fakeUser = new User("Hassan","Jaber","0721743510", 57.681790, 11.984620);

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

        MarkerData markerData = new MarkerData(fakeMeetUp.getName(), fakeMeetUp.getCategory().color, fakeMeetUp.getDescription(), fakeMeetUp.getCategory().color);
        Gson markerDataGson = new Gson();
        String markerDataString = markerDataGson.toJson(markerData);
        MarkerOptions markerOptions = new MarkerOptions().position(fakeMeetUpCoordinates).snippet(markerDataString).icon(BitmapDescriptorFactory.fromBitmap(fakeMeetUp.getIconBitmap(this))).anchor(0.5f, 0.5f).alpha(0.6f);
        Marker marker = this.map.addMarker(markerOptions);

        fakeUserCoordinates = new LatLng(fakeUser.getLatitude(),fakeUser.getLongitude());
        //Getbitmap from marker drawable
        String query = "{me{bitmoji{avatar}, displayName}}";
        SnapLogin.fetchUserData(this, query, null, new FetchUserDataCallback() {
            @Override
            public void onSuccess(@Nullable UserDataResponse userDataResponse) {
                if(userDataResponse == null || userDataResponse.getData() == null){
                    return;
                }

                MeData meData = userDataResponse.getData().getMe();
                if(meData == null){
                    return;
                }

                if(meData.getBitmojiData() != null) {
                    Glide.with(getApplicationContext()).asBitmap().load(meData.getBitmojiData().getAvatar()).into(new Target<Bitmap>() {
                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            markerOptionsUser = new MarkerOptions().position(fakeUserCoordinates).title(userDataResponse.getData().getMe().getDisplayName()).icon(BitmapDescriptorFactory.fromBitmap(resource)).anchor(0.5f, 0.5f);
                            map.addMarker(markerOptionsUser);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void getSize(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(@Nullable Request request) {

                        }

                        @Nullable
                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(boolean b, int i) {

            }
        });

        this.map.moveCamera(CameraUpdateFactory.newLatLng(fakeMeetUpCoordinates));

        this.map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(m -> onMeetUpMarkerClick());
        this.map.setOnInfoWindowLongClickListener(m -> joinMarkedMeetUp());
        this.map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                if (reason == REASON_GESTURE) {
                    marker.hideInfoWindow();
                }
            }
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

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
