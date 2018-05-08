package jm.maps;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Janek on 2018-01-24.
 */

public class GameEngine {

    private static double MAX_DISTANCE = 0.01f;
    private static int AMOUNT_OF_MARKERS = 10;
    private static double MINIMAL_DISTANCE_TO_SCORE = 50;
    private static int SCORED_POINTS = 10;
    private static int MINIMAL_DISTANCE_BETWEEN_MARKERS = 600;

    private int score;

    List<MarkerOptions> markers;

    boolean initialized = false;

    GameEngine(){
        score = 0;
    }

    private void initialize(Location location){
        if(initialized) return;
        initialized = true;
        markers = new ArrayList<MarkerOptions>();
        for(int i = 0 ; i < AMOUNT_OF_MARKERS ; i++){
            MarkerOptions options = new MarkerOptions();
            LatLng newPointerLocation = new LatLng(location.getLatitude(), location.getLongitude());

            boolean acceptableNewLocation = false;
            while(!acceptableNewLocation) { //kiedy nowa lokalizacja nie jest ok
                newPointerLocation = getRandomNearLocation(location, MAX_DISTANCE); // losowanie nowej pozycji
                acceptableNewLocation = true; // zakladamy ze jest ok
                for(MarkerOptions marker : markers) {
                    //sprawdzamy czy rzeczywiscie jest ok
                    if (SphericalUtil.computeDistanceBetween(marker.getPosition(), newPointerLocation) < MINIMAL_DISTANCE_BETWEEN_MARKERS) {
                        acceptableNewLocation = false;
                    }
                }

            }
            options.position(newPointerLocation);
            options.title("punkt "+String.valueOf(i));
            options.icon(BitmapDescriptorFactory.defaultMarker(getRandomColor()));
            markers.add(options);
        }
    }

    public void update(Location currentLocation){
        if(!initialized) initialize(currentLocation);
        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        List<MarkerOptions> toDelete = new ArrayList<>();
        for(MarkerOptions marker : markers){
            int distance = (int)SphericalUtil.computeDistanceBetween(current, marker.getPosition());
            marker.title(String.valueOf(distance+"m"));
            if(distance < MINIMAL_DISTANCE_TO_SCORE){
                toDelete.add(marker);
                score += SCORED_POINTS;
            }
        }
        markers.removeAll(toDelete);
    }

    private float getRandomColor(){
        return (float)Math.random()*330;
    }

    private LatLng getRandomNearLocation(Location location, double maxDistance){
        LatLng latLng = new LatLng(location.getLatitude() + Math.random()*maxDistance*2 - maxDistance,location.getLongitude() + Math.random()*maxDistance*2 - maxDistance);
        return latLng;
    }

    private LatLng getRandomOnCircleLocation(Location location, double maxDistance){
        double randomAngle = Math.random()*360;
        double changeLat = Math.cos(Math.toRadians(randomAngle))*maxDistance;
        double changeLng = Math.sin(Math.toRadians(randomAngle))*maxDistance*1.5;
        LatLng latLng = new LatLng(location.getLatitude() + changeLat,location.getLongitude() + changeLng);
        return latLng;
    }

    public List<MarkerOptions> getMarkers(){
        return markers;
    }

    public int getScore(){
        return score;
    }
}