package jm.maps.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import jm.maps.R;

/**
 * Created by Janek on 2018-05-25.
 */

public class PermissionManager {

    public static final int REQUEST_LOCATION_CODE=99;


    public void checkPermission(Activity activity){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            checkLocationPermission(activity);
        }

    }

    public boolean checkLocationPermission(Activity activity){
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;
        }
        else
            return true;
    }

    public boolean isLocationPermissionGranted(int requestCode, Activity activity, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    //permisson is granted!
                    if(ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED)
                    {
                        return true;

                    }
                }
                else //permisson is denied
                {
                    return false;
                }

        }
        return false;
    }
}
