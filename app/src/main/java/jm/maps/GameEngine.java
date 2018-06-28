package jm.maps;

import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel Wrzask on 2018-01-24.
 */

public class GameEngine {

    private static double MAX_DISTANCE = 0.01f;
    private static int AMOUNT_OF_MARKERS = 3;
    private static double MINIMAL_DISTANCE_TO_SCORE = 50;
    private static int SCORED_POINTS = 10;
    private static int MINIMAL_DISTANCE_BETWEEN_MARKERS = 600;
    private static int ACCEPTABLE_RETRIES = 100;

    private int score;

    List<MarkerOptions> markerOptions;

    List<Marker> markers;

    boolean initialized = false;

    public GameEngine(){
        score = 0;
    }

    private void initialize(Location location){
        if(initialized) return;
        initialized = true;
        markerOptions = new ArrayList<MarkerOptions>();
        markers = new ArrayList<>();
        for(int i = 0 ; i < AMOUNT_OF_MARKERS ; i++){
            MarkerOptions options = new MarkerOptions();
            LatLng newPointerLocation = new LatLng(location.getLatitude(), location.getLongitude());

            int retries = 0;

            boolean acceptableNewLocation = false;
            while(!acceptableNewLocation) { //kiedy nowa lokalizacja nie jest ok
                newPointerLocation = getRandomNearLocation(location, MAX_DISTANCE); // losowanie nowej pozycji
                acceptableNewLocation = true; // zakladamy ze jest ok
                for(MarkerOptions marker : markerOptions) {
                    //sprawdzamy czy rzeczywiscie jest ok
                    if (SphericalUtil.computeDistanceBetween(marker.getPosition(), newPointerLocation) < MINIMAL_DISTANCE_BETWEEN_MARKERS) {
                        acceptableNewLocation = false;
                    }
                }

                retries++;
                if(retries > ACCEPTABLE_RETRIES) return;
            }
            options.position(newPointerLocation);
            options.title("punkt "+String.valueOf(i));
            options.icon(BitmapDescriptorFactory.defaultMarker(getRandomColor()));
            markerOptions.add(options);
        }
    }

    public void restart(Location location){
        for(Marker marker : markers){
            marker.remove();
        }
        markers.clear();
        markerOptions.clear();
        initialized = false;
        initialize(location);
    }

    public void update(Location currentLocation){
        if(!initialized) initialize(currentLocation);
        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        List<MarkerOptions> toDelete = new ArrayList<>();
        for(MarkerOptions marker : markerOptions){
            int distance = (int)SphericalUtil.computeDistanceBetween(current, marker.getPosition());
            marker.title(String.valueOf(distance+"m"));
            if(distance < MINIMAL_DISTANCE_TO_SCORE){
                toDelete.add(marker);
                score += SCORED_POINTS;
            }
        }
        markerOptions.removeAll(toDelete);

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

    public List<MarkerOptions> getMarkerOptions(){
        return markerOptions;
    }

    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }

    public void addMarker(Marker m) {
        markers.add(m);
    }
}