package com.bulbasaur.dat256.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.bulbasaur.dat256.model.Main;
import com.bulbasaur.dat256.model.MapBounds;
import com.bulbasaur.dat256.model.MeetUp;
import com.bulbasaur.dat256.model.User;
import com.bulbasaur.dat256.services.firebase.DBCollection;
import com.bulbasaur.dat256.services.firebase.DBDocument;
import com.bulbasaur.dat256.services.firebase.Database;
import com.bulbasaur.dat256.services.firebase.QueryFilter;
import com.bulbasaur.dat256.services.firebase.RequestListener;
import com.bulbasaur.dat256.viewmodel.uielements.CustomInfoWindowAdapter;
import com.bulbasaur.dat256.viewmodel.uielements.MarkerData;
import com.bulbasaur.dat256.viewmodel.utilities.Helpers;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.bulbasaur.dat256.viewmodel.utilities.Helpers.getBitmapFromVectorDrawable;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout drawer;
    private SearchView searchView;

    private GoogleMap map;

    private User fakeFriend;

    private static final int CREATE_NEW_EVENT_CODE = 32;

    public static final int SHOW_EVENT_ON_MAP_CODE = 1;
    private static final int SHOW_FRIEND_ON_MAP_CODE = 45;
    private static final int DEFAULT_MEET_UP_ZOOM_LEVEL = 15;

    private boolean markerInMiddle = false;

    private Main main;

    private HashMap<Marker, MeetUp> meetUpMarkerMap;
    private HashMap<Marker, User> friendMarkerMap;

    private Marker meMarker;

    private User currentUser;

    private Marker currentlyOpenMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        main = Main.getInstance();

        meetUpMarkerMap = new HashMap<>();
        friendMarkerMap = new HashMap<>();

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
                case R.id.nav_MeetUpList:
                    startActivity(new Intent(this, ListActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> startActivityForResult(new Intent(this, CreateMeetUpActivity.class), CREATE_NEW_EVENT_CODE));

        if(Database.getInstance().hasUser()){
            Database.getInstance().user(new RequestListener<DBDocument>(){
                @Override
                public void onSuccess(DBDocument object) {
                    super.onSuccess(object);
                    currentUser = new User(object.id());
                    currentUser.setFirstName((String)object.get("firstname"));
                    currentUser.setLastName((String)object.get("lastname"));
                    currentUser.setPhoneNumber((String)object.get("phonenumber"));

                    //TODO get friends from DB and add them to the currentUser.friends list
                }
            });
        }
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
        refreshMapItems(getCurrentMapBounds());
        showUserLocationOnMapWithRegularMarkerAndMoveMapToIt();

        this.map.setOnMarkerClickListener(marker -> {
            if (!marker.equals(meMarker)) {
                currentlyOpenMarker = marker;
                marker.showInfoWindow();
            } else if (marker.equals(meMarker)) {
                Toast.makeText(this, "Your location", Toast.LENGTH_SHORT).show();
            }

            refreshMapItems(getCurrentMapBounds());

            map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));//TODO ideally lerp to this location

            if(meetUpMarkerMap.containsKey(currentlyOpenMarker)){
                this.map.setOnInfoWindowClickListener(m -> { onMeetUpMarkerClick(m);});
                this.map.setOnInfoWindowLongClickListener(m -> joinMarkedMeetUp(m));
            } else if(friendMarkerMap.containsKey(currentlyOpenMarker)){
                this.map.setOnInfoWindowClickListener(m -> {onFriendMarkerCLick(m);});
            }

            return true;
        });

        this.map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        this.map.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                if (currentlyOpenMarker != null) {
                    currentlyOpenMarker.hideInfoWindow();
                    currentlyOpenMarker = null;
                }
            }
        });
        this.map.setOnCameraIdleListener(() -> {
            System.out.println("camera idle - markers update");

            if (currentlyOpenMarker == null) {
                refreshMapItems(getCurrentMapBounds());
            }
        });
    }


    private void showUserLocationOnMapWithRegularMarkerAndMoveMapToIt() {
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
                meMarker = map.addMarker(new MarkerOptions().position(lastLocationCoords).title("Your location"));

                if(currentUser != null) {
                    currentUser.setCoordinates(lastLocationCoords);
                }

                //fake friend :/
                fakeFriend = new User("Dein", "Freund", "phone");
                MarkerData markerData = new MarkerData(false, ""+fakeFriend.getFirstName()
                        +" "+ fakeFriend.getLastName(),R.color.mainColor,fakeFriend.getScore(),R.color.mainColor);
                Gson markerDataGson = new Gson();
                String markerDataString = markerDataGson.toJson(markerData);
                Bitmap icon = getBitmapFromVectorDrawable(this, R.drawable.ic_friend_icon_24dp, R.color.mainColor);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude()+1, location.getLongitude()+2)).snippet(markerDataString).
                        icon(BitmapDescriptorFactory.fromBitmap(icon));
                Marker fakeFriendMarker = this.map.addMarker(markerOptions);
                fakeFriend.setCoordinates(new LatLng(location.getLatitude()+1, location.getLongitude()+1));
                friendMarkerMap.put(fakeFriendMarker, fakeFriend);
                //end

            } else {
                Toast.makeText(this, "Your location is not found", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void joinMarkedMeetUp(Marker m) {
        //TODO make the user join the marked event here
        Toast.makeText(this, "Joined " + meetUpMarkerMap.get(m).getName(), Toast.LENGTH_LONG).show();
    }

    private void onMeetUpMarkerClick(Marker m) {
        Intent meetUpIntent = new Intent(this, MeetUpActivity.class);
        meetUpIntent.putExtra("MeetUp", meetUpMarkerMap.get(m));
        startActivityForResult(meetUpIntent, SHOW_EVENT_ON_MAP_CODE);
    }

    private void onFriendMarkerCLick(Marker m){
        Intent friendIntent = new Intent(this, UserActivity.class);
        friendIntent.putExtra("User", friendMarkerMap.get(m));
        startActivityForResult(friendIntent, SHOW_FRIEND_ON_MAP_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_EVENT_ON_MAP_CODE) {
            if (resultCode == RESULT_OK) {
                MeetUp resultMeetup = (MeetUp) data.getSerializableExtra("MeetUpReturn");
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(resultMeetup.getCoordinates().lat, resultMeetup.getCoordinates().lon), DEFAULT_MEET_UP_ZOOM_LEVEL));
            }
        } else if (requestCode == CREATE_NEW_EVENT_CODE) {
            if (resultCode == RESULT_OK) {
                refreshMapItems(getCurrentMapBounds());
            }
        }
    }

    private MapBounds getCurrentMapBounds() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        return new MapBounds(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude);
    }

    private void refreshMapItems(MapBounds bounds) {
        // remove markers and meetups that are out of bounds of the map view
        main.removeOldMeetUps(bounds);
        removeOldMeetUpMarkers();

        //get a reference to the meetup list in the database
        DBCollection allMeetUpsCollection = Database.getInstance().meetups();

        if (allMeetUpsCollection == null) return;

        //create query filters for the coordinates of the boundaries of the view
        QueryFilter latitudeFilter = new QueryFilter("coord_lat");
        latitudeFilter.addFilter(">", bounds.getBottomLeft().lat);
        latitudeFilter.addFilter("<", bounds.getTopRight().lat);

        QueryFilter longitudeFilter = new QueryFilter("coord_lon");
        longitudeFilter.addFilter(">", bounds.getBottomLeft().lon);
        longitudeFilter.addFilter("<", bounds.getTopRight().lon);

        //search the database for meetups that lie within the view boundaries
        searchLatLon(allMeetUpsCollection, latitudeFilter, longitudeFilter);
    }

    private void searchLatLon(DBCollection allMeetUpsCollection, QueryFilter latFilter, QueryFilter lonFilter) {
        //search by latitude
        allMeetUpsCollection.search(latFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> latFilteredDocs) {
                super.onSuccess(latFilteredDocs);

                //search by longitude
                allMeetUpsCollection.search(lonFilter, new RequestListener<List<? extends DBDocument>>() {
                    @Override
                    public void onSuccess(List<? extends DBDocument> lonFilteredDocs) {
                        super.onSuccess(lonFilteredDocs);

                        //get the intersection of the set of latitude- and set of longitude-filtered events, since
                        // the map boundaries are of course within a rectangle
                        List<DBDocument> docsWithinView = Helpers.intersection((List<DBDocument>) latFilteredDocs, (List<DBDocument>) lonFilteredDocs);

                        //search for public events and the current user's events
                        searchVisibilityPublic(allMeetUpsCollection, docsWithinView);

                        //search for events that are available for friends
                        searchVisibilityFriends(allMeetUpsCollection, docsWithinView);
                    }
                });
            }
        });
    }

    private void searchVisibilityPublic(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView) {
        //create a filter for public events
        QueryFilter publicFilter = new QueryFilter("visibility");
        publicFilter.addFilter("=", "PUBLIC");

        //search for public events
        allMeetUpsCollection.search(publicFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> publicDocs) {
                super.onSuccess(publicDocs);

                //search for the current user's events
                searchCurrentUserDocs(allMeetUpsCollection, docsWithinView, (List<DBDocument>) publicDocs);
            }
        });
    }

    private void searchCurrentUserDocs(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView, List<DBDocument> publicDocs) {
        //create a filter for the current user's events
        QueryFilter currentUserFilter = new QueryFilter("creator");
        currentUserFilter.addFilter("=", Main.TEMP_CURRENT_USER_ID); //TODO replace this with current user ID

        //search for the current user's events
        allMeetUpsCollection.search(currentUserFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> currentUserDocs) {
                super.onSuccess(currentUserDocs);

                //get the union between the set of public events and current user events as both should be visible to the current user
                List<DBDocument> accessibleDocs = Helpers.union(publicDocs, (List<DBDocument>) currentUserDocs);

                //place the new public and private events on the map
                finishUpdatingMapMeetUps(Helpers.intersection(accessibleDocs, docsWithinView));
            }
        });
    }

    private void searchVisibilityFriends(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView) {
        //create a filter for events that are visible to friends (not necessarily the current user's friends)
        QueryFilter friendsVisibilityFilter = new QueryFilter("visibility");
        friendsVisibilityFilter.addFilter("=", "FRIENDS");

        //search for the events that are visible to friends (not necessarily the current user's friends)
        allMeetUpsCollection.search(friendsVisibilityFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> friendsVisibleDocs) {
                super.onSuccess(friendsVisibleDocs);

                //search for events that the current user's friends own
                searchFriendsMeetUps(allMeetUpsCollection, docsWithinView, (List<DBDocument>) friendsVisibleDocs);
            }
        });
    }

    public void searchFriendsMeetUps(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView, List<DBDocument> friendsVisibleDocs) {
        //get a reference to the user list in the database
        DBCollection usersCollection = Database.getInstance().users();

        if (usersCollection == null) return;

        //get the current user's document
        usersCollection.get(Main.TEMP_CURRENT_USER_ID, new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                //initialize the current user's document to read its values
                emptyDoc.init(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        //get the list of friends of the current user
                        List<String> friendIDStrings = (List<String>) document.get("friends");

                        //for every friend of the current user...
                        for (String s : friendIDStrings) {
                            //create a filter for all events whose creator is the current friend
                            QueryFilter friendFilter = new QueryFilter("creator");
                            friendFilter.addFilter("=", s);

                            //search for these events
                            allMeetUpsCollection.search(friendFilter, new RequestListener<List<? extends DBDocument>>() {
                                @Override
                                public void onSuccess(List<? extends DBDocument> docsOfFriends) {
                                    super.onSuccess(docsOfFriends);

                                    //get the intersection of the events that are physically within
                                    // view, the events that this current friend owns, and the events
                                    // marked "for friends", as these are the ones that the current
                                    // user should be able to see
                                    docsOfFriends = Helpers.intersection((List<DBDocument>) docsOfFriends, docsWithinView);
                                    docsOfFriends = Helpers.intersection((List<DBDocument>) docsOfFriends, friendsVisibleDocs);

                                    //place the new friend events on the map
                                    finishUpdatingMapMeetUps((List<DBDocument>) docsOfFriends);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void finishUpdatingMapMeetUps(List<DBDocument> documents) {
        for (DBDocument doc : documents) {
            doc.init(new RequestListener<DBDocument>() {
                @Override
                public void onSuccess(DBDocument document) {
                    super.onSuccess(document);

                    MeetUp newMeetUp = Helpers.convertDocToMeetUp(document);

                    main.updateMapMeetUp(newMeetUp);

                    showUpdatedMeetUp(newMeetUp);
                }
            });
        }
    }

    private void showUpdatedMeetUp(MeetUp meetUp) {
        if (!meetUpMarkerMap.values().contains(meetUp)) {
            Marker marker = map.addMarker(createMarkerOptions(meetUp));

            meetUpMarkerMap.put(marker, meetUp);
        }
    }

    private void showUpdatedFriend(User friend) {
        if (!friendMarkerMap.values().contains(friend)) {
            Marker marker = map.addMarker(createMarkerOptions(friend));

            friendMarkerMap.put(marker, friend);

            System.out.println("one marker added");
        }
    }

    private void removeOldMeetUpMarkers() {
        Iterator<Marker> markerIterator = meetUpMarkerMap.keySet().iterator();
        while (markerIterator.hasNext()) {
            Marker m = markerIterator.next();

            if (!main.getMeetUpsWithinMapView().contains(meetUpMarkerMap.get(m))) {
                m.remove();
                markerIterator.remove();
            }
        }
    }

    private void removeOldFriendMarkers(){
        Iterator<Marker> markerIterator = friendMarkerMap.keySet().iterator();
        while (markerIterator.hasNext()) {
            Marker m = markerIterator.next();

            if (!main.getFriendsWithinMapView().contains(friendMarkerMap.get(m))) {
                m.remove();
                markerIterator.remove();

                System.out.println("one marker removed");
            }
        }
    }

    private MarkerOptions createMarkerOptions(MeetUp m) {
        return new MarkerOptions()
                .position(new LatLng(m.getCoordinates().lat, m.getCoordinates().lon))
                .snippet(new Gson().toJson(new MarkerData(true, m.getName(), m.getCategory().primaryColor, m.getDescription(), m.getCategory().secondaryColor)))
                .icon(BitmapDescriptorFactory.fromBitmap(m.getIconBitmap(this)))
                .anchor(0.5f, 0.5f)
                .alpha(0.6f);
    }

    private MarkerOptions createMarkerOptions(User u) {
        Bitmap icon = getBitmapFromVectorDrawable(this, R.drawable.ic_friend_icon_24dp, R.color.mainColor);
        return new MarkerOptions()
                .position(u.getCoordinates())
                .snippet(new Gson().toJson(new MarkerData(false, "" + u.getFirstName() + u.getLastName(), R.color.mainColor, "", R.color.mainColor)))
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 0.5f)
                .alpha(0.6f);
    }

}
