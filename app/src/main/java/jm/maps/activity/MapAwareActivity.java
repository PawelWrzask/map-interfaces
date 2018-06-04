package jm.maps.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
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


import jm.maps.R;
import jm.maps.utils.PermissionManager;

/**
 * Created by Paweł Wrzask on 2018-05-28.
 */

public class MapAwareActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    int PROXIMITY_RADIUS=10000;
    double latitude;
    double longitude;
    double end_latitude, end_longitude;
    boolean isInitialized=false;

    private PermissionManager permissionManager;

    public MapAwareActivity(String displayNotification) {
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

            }
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @SuppressWarnings("all")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionManager.isLocationPermissionGranted(requestCode, this, grantResults)){
            if(client==null)
            {
                buildGoogleApiClient();
            }
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

    protected synchronized void buildGoogleApiClient()
    {
        client=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).addApi(LocationServices.API)
                .build();

        client.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if(currentLocationMarker !=null)
        {
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker =mMap.addMarker(markerOptions);

        if(!isInitialized)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            isInitialized=true;
        }

        if(client!=null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
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
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

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

}
