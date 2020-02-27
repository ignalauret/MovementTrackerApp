package com.example.movementtracker.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.core.app.ActivityCompat;

import static com.example.movementtracker.Utils.Constants.REQUEST_LOCATION_CODE;

public class Util {

    public static AlertDialog createLocationRequestAlert(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle("Titulo")
                .setMessage("Mensaje")
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION_CODE);
                            }
                        }).create();
    }
}
