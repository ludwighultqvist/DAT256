package com.bulbasaur.dat256.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import com.bulbasaur.dat256.model.MeetUp.Visibility;
import com.bulbasaur.dat256.model.User;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DrawerLayout drawer;
    private SearchView searchView;

    private GoogleMap map;

    private User fakeFriend;

    private static final int CREATE_NEW_EVENT_CODE = 32;
    private static final int SHOW_EVENT_ON_MAP_CODE = 1;
    private static final int DEFAULT_MEET_UP_ZOOM_LEVEL = 15;

    private boolean markerInMiddle = false;

    private Main main;

    private HashMap<Marker, MeetUp> meetUpMarkerMap;

    private Marker meMarker;

    private User currentUser;

    private Marker currentlyOpenMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        main = new Main();

        meetUpMarkerMap = new HashMap<>();

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

        showUserLocationOnMapWithRegularMarkerAndMoveMapToIt();

        this.map.setOnMarkerClickListener(marker -> {
            if (!marker.equals(meMarker)) {
                marker.showInfoWindow();

                currentlyOpenMarker = marker;
            } else if (marker.equals(meMarker)) {
                Toast.makeText(this, "Your location", Toast.LENGTH_SHORT).show();
            }

            map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));//TODO ideally lerp to this location

            return true;
        });

        this.map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        this.map.setOnInfoWindowClickListener(m -> {
            onMeetUpMarkerClick(m);
        });
        this.map.setOnInfoWindowLongClickListener(m -> joinMarkedMeetUp(m));
        this.map.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                if (currentlyOpenMarker != null) {
                    currentlyOpenMarker.hideInfoWindow();//TODO hide the currently open marker, not just the fake one
                    currentlyOpenMarker = null;
                }
            }
        });
        this.map.setOnCameraIdleListener(() -> {
            System.out.println("camera idle - meetups update");

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
        System.out.println("removing meetups outside view...");

        main.removeOldMeetUps(bounds);
        removeOldMeetUpMarkers();

        System.out.println("removed meetups outside view");

        System.out.println("creating request...");

        DBCollection allMeetUpsCollection = Database.getInstance().meetups();

        if (allMeetUpsCollection == null) return;

        System.out.println("reference to all meetups worked");

        QueryFilter latitudeFilter = new QueryFilter("coord_lat");
        latitudeFilter.addFilter(">", bounds.getBottomLeft().lat);
        latitudeFilter.addFilter("<", bounds.getTopRight().lat);

        QueryFilter longitudeFilter = new QueryFilter("coord_lon");
        longitudeFilter.addFilter(">", bounds.getBottomLeft().lon);
        longitudeFilter.addFilter("<", bounds.getTopRight().lon);

        System.out.println("filters created");

        searchLatLon(allMeetUpsCollection, latitudeFilter, longitudeFilter);
    }

    private void searchLatLon(DBCollection allMeetUpsCollection, QueryFilter latFilter, QueryFilter lonFilter) {
        System.out.println("searching with query 1: latitude...");

        allMeetUpsCollection.search(latFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> latFilteredDocs) {
                super.onSuccess(latFilteredDocs);

                System.out.println("latitude query result: " + latFilteredDocs);

                System.out.println("success 1: searching with query 2...");

                allMeetUpsCollection.search(lonFilter, new RequestListener<List<? extends DBDocument>>() {
                    @Override
                    public void onSuccess(List<? extends DBDocument> lonFilteredDocs) {
                        super.onSuccess(lonFilteredDocs);

                        System.out.println("longitude query result: " + lonFilteredDocs);

                        System.out.println("successful search queries 1 & 2");

                        List<DBDocument> docsWithinView = intersection((List<DBDocument>) latFilteredDocs, (List<DBDocument>) lonFilteredDocs);

                        searchVisibilityPublic(allMeetUpsCollection, docsWithinView);
                        searchVisibilityFriends(allMeetUpsCollection, docsWithinView);
                    }
                });
            }
        });
    }

    private void searchVisibilityPublic(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView) {
        QueryFilter publicFilter = new QueryFilter("visibility");
        publicFilter.addFilter("=", "PUBLIC");

        allMeetUpsCollection.search(publicFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> publicDocs) {
                super.onSuccess(publicDocs);

                searchCurrentUserDocs(allMeetUpsCollection, docsWithinView, (List<DBDocument>) publicDocs);
            }
        });
    }

    private void searchCurrentUserDocs(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView, List<DBDocument> publicDocs) {
        QueryFilter currentUserFilter = new QueryFilter("creator");
        currentUserFilter.addFilter("=", Main.TEMP_CURRENT_USER_ID); //TODO replace this with current user ID

        allMeetUpsCollection.search(currentUserFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> currentUserDocs) {
                super.onSuccess(currentUserDocs);

                List<DBDocument> accessibleDocs = union(publicDocs, (List<DBDocument>) currentUserDocs);

                finishUpdatingMapMeetUps(intersection(accessibleDocs, docsWithinView));
            }
        });
    }

    public void finishUpdatingMapMeetUps(List<DBDocument> documents) {
        System.out.println("valid results (intersection): " + documents);

        System.out.println("requesting full individual valid results...");

        for (DBDocument doc : documents) {
            doc.init(new RequestListener<DBDocument>() {
                @Override
                public void onSuccess(DBDocument document) {
                    super.onSuccess(document);
                    System.out.println("document received with id " + document.id());

                    System.out.println("converting to meetup and updating in model...");

                    MeetUp newMeetUp = convertDocToMeetUp(document);

                    main.updateMapMeetUp(newMeetUp);

                    System.out.println("model updated. updating view...");

                    showUpdatedMeetUp(newMeetUp);

                    System.out.println("view updated");
                }
            });
        }
    }

    public void searchFriendsMeetUps(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView, List<DBDocument> friendsVisibleDocs) {
        DBCollection usersCollection = Database.getInstance().users();

        if (usersCollection == null) return;

        usersCollection.get(Main.TEMP_CURRENT_USER_ID, new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                emptyDoc.init(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        List<String> friendIDStrings = (List<String>) document.get("friends");

                        for (String s : friendIDStrings) {
                            QueryFilter friendFilter = new QueryFilter("creator");
                            friendFilter.addFilter("=", s);

                            allMeetUpsCollection.search(friendFilter, new RequestListener<List<? extends DBDocument>>() {
                                @Override
                                public void onSuccess(List<? extends DBDocument> docsOfFriends) {
                                    super.onSuccess(docsOfFriends);

                                    docsOfFriends = intersection((List<DBDocument>) docsOfFriends, docsWithinView);
                                    docsOfFriends = intersection((List<DBDocument>) docsOfFriends, friendsVisibleDocs);

                                    finishUpdatingMapMeetUps((List<DBDocument>) docsOfFriends);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void searchVisibilityFriends(DBCollection allMeetUpsCollection, List<DBDocument> docsWithinView) {
        QueryFilter friendsVisibilityFilter = new QueryFilter("visibility");
        friendsVisibilityFilter.addFilter("=", "FRIENDS");

        allMeetUpsCollection.search(friendsVisibilityFilter, new RequestListener<List<? extends DBDocument>>() {
            @Override
            public void onSuccess(List<? extends DBDocument> friendsVisibleDocs) {
                super.onSuccess(friendsVisibleDocs);

                searchFriendsMeetUps(allMeetUpsCollection, docsWithinView, (List<DBDocument>) friendsVisibleDocs);
            }
        });
    }

    private void showUpdatedMeetUp(MeetUp meetUp) {
        if (!meetUpMarkerMap.values().contains(meetUp)) {
            Marker marker = map.addMarker(createMarkerOptions(meetUp));

            meetUpMarkerMap.put(marker, meetUp);

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

    private List<DBDocument> intersection(List<DBDocument> first, List<DBDocument> second) {
        for (int i = first.size() - 1; i >= 0; i--) {
            if (!second.contains(first.get(i))) {
                first.remove(i);
            }
        }

        return first;
    }

    private List<DBDocument> union(List<DBDocument> first, List<DBDocument> second) {
        for (DBDocument s : second) {
            if (!first.contains(s)) {
                first.add(s);
            }
        }

        return first;
    }

    private MeetUp convertDocToMeetUp(DBDocument meetUpDoc) {
        String id = meetUpDoc.id();
        String creatorID = (String) meetUpDoc.get("creator");
        String name = (String) meetUpDoc.get("name");
        Double coord_lat = (Double) meetUpDoc.get("coord_lat");
        Double coord_lon = (Double) meetUpDoc.get("coord_lon");
        String description = (String) meetUpDoc.get("description");
        Categories category = MeetUp.getCategoryFromString((String) meetUpDoc.get("category"));
        Long maxAttendees = (Long) meetUpDoc.get("maxattendees");
        /*Calendar startDate = (Calendar) meetUpDoc.get("startdate");
        Calendar endDate = (Calendar) meetUpDoc.get("enddate");*/
        Visibility visibility = MeetUp.getVisibilityFromString((String) meetUpDoc.get("visibility"));

        if (id == null || name == null || coord_lat == null || coord_lon == null
                || description == null || category == null || maxAttendees == null) {//|| startDate == null || endDate == null) {//TODO put this back
            return null;
        }

        return new MeetUp(id, creatorID, name, new Coordinates(coord_lat, coord_lon), description, category,
                maxAttendees, null, null, visibility);
    }



    /**
     * Credit to Alexey and Hugo Gresse on Stack Overflow: https://stackoverflow.com/a/38244327/3380955
     * @param context the bitmap's context
     * @param drawableId the id of the vector resource
     * @return a bitmap image of the vector resource
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int color) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        DrawableCompat.setTint(Objects.requireNonNull(drawable), ContextCompat.getColor(context, color));
        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(drawable).getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /*

        DBCollection usersCollection = Database.getInstance().users();

        if (usersCollection == null) return null;

        DBDocument currentUserDoc = usersCollection.get("8snVW8GZQzV8QYibZzRW", new RequestListener<DBDocument>() {
            @Override
            public void onSuccess(DBDocument emptyDoc) {
                super.onSuccess(emptyDoc);

                emptyDoc.init(new RequestListener<DBDocument>() {
                    @Override
                    public void onSuccess(DBDocument document) {
                        super.onSuccess(document);

                        List<String> friendIDStrings = (List<String>) document.get("friends");


                        for (String s : friendIDStrings) {
                            meetUpCollection.get(s, new RequestListener<>())
                        }
                    }
                });
            }
        });


        return new ArrayList<>(); //TODO in this method, get the list of documents of all user-created meetups in user object
     */
}
