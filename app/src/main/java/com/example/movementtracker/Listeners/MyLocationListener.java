package com.example.movementtracker.Listeners;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.example.movementtracker.Activities.WorkingActivity;
import com.example.movementtracker.Utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationListener implements LocationListener {

    private Context context;
    private TextView locationText;
    private boolean hasText;

    public MyLocationListener(Context context, TextView locationText) {
        this.context = context;
        this.locationText = locationText;
        hasText = true;
    }

    public MyLocationListener(Context context) {
        this.context = context;
        hasText = false;
    }

    @Override
    public void onLocationChanged(Location loc) {
        String longitude = "Longitud: " + loc.getLongitude();
        String latitude = "Latitud: " + loc.getLatitude();
        WorkingActivity.currentLongitude = Math.floor(loc.getLongitude() * Constants.LOCATION_ROUNDING) / Constants.LOCATION_ROUNDING;
        WorkingActivity.currentLatitude = Math.floor(loc.getLatitude() * Constants.LOCATION_ROUNDING) / Constants.LOCATION_ROUNDING;

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nTu Ciudad actual es: " + cityName;
        if(hasText){
            locationText.setText(s);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
