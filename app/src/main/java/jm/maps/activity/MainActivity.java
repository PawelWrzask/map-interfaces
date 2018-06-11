package jm.maps.activity;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jm.maps.GameEngine;
import jm.maps.R;
import jm.maps.connection.Client;
import jm.maps.utils.PermissionManager;
import jm.maps.view.FragmentDiscover;
import jm.maps.view.FragmentHistory;
import jm.maps.view.FragmentMapsActivity;
import jm.maps.view.FragmentNear;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener  {
    private DrawerLayout drawer;

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private int PROXIMITY_RADIUS=10000;
    private double latitude;
    private double longitude;
    private double end_latitude, end_longitude;
    private boolean isInitialized=false;

    private static final GameEngine gameEngine = new GameEngine();


    public MainActivity(){

    }




    @SuppressWarnings("all")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager permissionManager = new PermissionManager();
        if(permissionManager.isLocationPermissionGranted(requestCode, this, grantResults)){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }else{
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();
        }
    }


    public void changeType(View view)
    {
        if(mMap.getMapType()== GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        else if(mMap.getMapType()==GoogleMap.MAP_TYPE_TERRAIN){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if (mMap.getMapType()==GoogleMap.MAP_TYPE_HYBRID){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    public synchronized void buildGoogleApiClient()
    {
        if(client==null) {
            client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).addApi(LocationServices.API)
                    .build();

            client.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(getMap() == null) return;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        gameEngine.update(location);
        for (MarkerOptions marker : gameEngine.getMarkers()) {
            getMap().addMarker(marker);
        }

        TextView etScore = (TextView) findViewById(R.id.score);
        etScore.setText(String.valueOf(gameEngine.getScore()));

        if(currentLocationMarker !=null)
        {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);

        if(!isInitialized)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            isInitialized=true;
        }



        locationChangedAction(location);
    }

    public void locationChangedAction(Location location){

    }



    private String getDirectionUrl()
    {

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);

        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key=AIzaSyDxdzWca6yNgPRRks8xaet4CLpirt4Rxzk");

        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longtitude, String nearbyPlace)
    {
        StringBuilder googlePlaceUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location"+latitude+","+longtitude);

        googlePlaceUrl.append("&radius"+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=AIzaSyDxdzWca6yNgPRRks8xaet4CLpirt4Rxzk");

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle){

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
    }

    public GoogleMap getMap(){
        return mMap;
    }

    @SuppressWarnings("all")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentMapsActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_my_location);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentMapsActivity()).commit();
                break;
            case R.id.nav_near_me:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentNear()).commit();
                break;
            case R.id.discover:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDiscover()).commit();
                break;
            case R.id.beenhere:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHistory()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View v) throws IOException {
        switch (v.getId()) {
            case R.id.B_search: {
                EditText tf_location = (EditText) findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();

                List<Address> addressList = new ArrayList<>();
                MarkerOptions mo = new MarkerOptions();

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    for (int i = 0; i < addressList.size(); i++) {
                        Address myAddress = addressList.get(i);
                        LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                        mo.position(latLng);
                        mo.title("Your search result");
                        getMap().addMarker(mo);
                        getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    }

                }
            }
            break;


            case R.id.B_to:

                TextView etScore = (TextView) findViewById(R.id.score);
                Log.i("socket: ", "before sending out");
                Client myClient = new Client("31.186.82.95", 51069, etScore);
                myClient.execute();
                Log.i("socket: ", "after sending out");


                break;


        }
    }

    public void setMap(GoogleMap map){
        this.mMap = map;
    }

}