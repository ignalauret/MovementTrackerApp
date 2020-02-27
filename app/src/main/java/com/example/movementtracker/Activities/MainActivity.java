package com.example.movementtracker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.movementtracker.R;

import static com.example.movementtracker.Utils.Util.*;
import static com.example.movementtracker.Utils.Constants.*;

public class MainActivity extends AppCompatActivity {


    RelativeLayout mStartBtn;
    boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutBind();
        checkGps();
    }

    private void layoutBind() {
        mStartBtn = findViewById(R.id.startBtn);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorkingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /* ************************** Permissions **************************** */

    private void checkGps() {
        checkLocationPermission();
        Intent checkGps = new Intent(this, LocationAccessActivity.class);
        startActivity(checkGps);
    }


    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                /* Show an explanation to the user *asynchronously* -- don't block
                 * this thread waiting for the user's response! After the user
                 * sees the explanation, try again to request the permission.
                 */
                AlertDialog alertDialog = createLocationRequestAlert(this);
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(0xFFFFCC00);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFFFCC00);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_CODE);
            }
        } else {
            mLocationPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                /* Permission was granted, yay! Do the
                 * location-related task you need to do.
                 */
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }

            } else {
                Intent noPermission = new Intent(this, NotAvailableGpsActivity.class);
                noPermission.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(noPermission);
            }
        }
    }
}
