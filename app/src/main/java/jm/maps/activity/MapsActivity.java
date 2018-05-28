package jm.maps.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

    static GameEngine gameEngine;

    MapsActivity(){
        gameEngine = new GameEngine();
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();


        gameEngine.update(location);
        for (MarkerOptions marker : gameEngine.getMarkers()) {
            getMap().addMarker(marker);
        }

        TextView etScore = (TextView) findViewById(R.id.score);
        etScore.setText(String.valueOf(gameEngine.getScore()));
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
}
