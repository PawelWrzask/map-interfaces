package jm.maps.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jm.maps.GameEngine;
import jm.maps.R;
import jm.maps.connection.Client;


public class MapsActivity extends MapAwareActivity {

    public MapsActivity() {
        //super("DisplayNotification");

    }


    public void onLocationChanged(Location location) {
        //latitude = location.getLatitude();
        //longitude = location.getLongitude();

        //gameEngine.update(location);
        //for (MarkerOptions marker : gameEngine.getMarkers()) {
            //getMap().addMarker(marker);
        }

        //TextView etScore = (TextView) findViewById(R.id.score);
        //etScore.setText(String.valueOf(gameEngine.getScore()));
    }




