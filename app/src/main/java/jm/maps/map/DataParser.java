package jm.maps.map;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julia Matlak on 2018-01-12.
 */

public class DataParser {

    private  HashMap<String, String> getDuration(JSONArray googleDirecionJson){
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration="";
        String distance="";


        Log.d("json response", googleDirecionJson.toString());
        try {
            duration=googleDirecionJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance=googleDirecionJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration", duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleDirectionsMap;

    }
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA";
        String vicinity = "-NA";
        String latitude = "";
        String longtitude = "";
        String reference = "";


        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity"))
            {
                vicinity=googlePlaceJson.getString("vicinity");

            }
            latitude=googlePlaceJson.getJSONObject("grometry").getJSONObject("location").getString("lat");
            longtitude=googlePlaceJson.getJSONObject("grometry").getJSONObject("location").getString("lng");

            reference=googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat",latitude);
            googlePlacesMap.put("lng", longtitude);
            googlePlacesMap.put("reference",reference);




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlacesMap;
    }


    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count=jsonArray.length();
        List<HashMap<String,String>> placesList= new ArrayList<>();
        HashMap<String,String> placeMap=null;

        for(int i=0;i<count;i++)
        {
            try {
                placeMap=getPlace((JSONObject)jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);

    }

    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPaths(jsonArray);


    }

    public String[] getPaths(JSONArray googleStepJson)
    {
        if(googleStepJson == null) return new String[0];
        int count = googleStepJson.length();
        String[] polylines = new String[count];

        for(int i=0;i<count;i++)
        {
            try {
                polylines[i]=getPath(googleStepJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline="";
        try {
             polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return polyline;
    }

}
