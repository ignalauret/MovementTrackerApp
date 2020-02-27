package com.example.movementtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movementtracker.Listeners.MyLocationListener;
import com.example.movementtracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.movementtracker.Utils.Constants.LOCATION_LISTENER_MIN_DISTANCE;
import static com.example.movementtracker.Utils.Constants.LOCATION_LISTENER_MIN_TIME;
import static com.example.movementtracker.Utils.Constants.MOVING_TRACKER_CHECK_MILLISECONDS;
import static com.example.movementtracker.Utils.Constants.MOVING_TRACKER_CHECK_SECONDS;

public class WorkingActivity extends AppCompatActivity {

    // Layout elements
    CardView mMovingText;
    CardView mNotMovingText;
    CardView mPausedText;
    RelativeLayout mEndBtn;
    RelativeLayout mPauseBtn;
    TextView mPauseBtnText;
    // Location tools
    Handler handler = new Handler();
    Runnable runnable;
    LocationListener mLocationListener;
    LocationManager mLocationManager;
    boolean isMoving;
    boolean showedMessage;
    // Location data
    public static double currentLatitude;
    public static double currentLongitude;
    private double lastCheckedLatitude;
    private double lastCheckedLongitude;
    private int secondsMoving;
    ArrayList<Integer> totalTimes;
    ArrayList<Double> longitudes;
    ArrayList<Double> latitudes;
    ArrayList<Date> timeStamps;
    // Time tools
    Chronometer mWorkingTimeText;
    boolean working;
    long pauseOffset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        bindUI();
        setUp();
        requestLocUpdate();
        startRecording();
    }

    private void bindUI() {
        mMovingText = findViewById(R.id.cardView);
        mNotMovingText = findViewById(R.id.cardViewNotMoving);
        mWorkingTimeText = findViewById(R.id.workingTime);
        mPausedText = findViewById(R.id.cardViewPaused);
        mPauseBtnText = findViewById(R.id.pauseStartText);
        mPauseBtn = findViewById(R.id.pauseBtn);
        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(working) {
                    pauseWork();
                    mPauseBtnText.setText("Reanudar");
                } else {
                    resumeWork();
                    mPauseBtnText.setText("Pausar");
                }
            }
        });
        mEndBtn = findViewById(R.id.endBtn);
        mEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWork();
            }
        });
    }

    private void setUp() {
        totalTimes = new ArrayList<>();
        longitudes = new ArrayList<>();
        latitudes = new ArrayList<>();
        timeStamps = new ArrayList<>();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkMovement();
                totalTimes.add(secondsMoving);
                handler.postDelayed(runnable, MOVING_TRACKER_CHECK_MILLISECONDS);
            }
        };
        mLocationListener = new MyLocationListener(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        resumeWork();
        setMovingText(false);
    }


    private void pauseWork() {
        stopChronometer();
        stopMovementTrack();
        showPaused();
    }

    private void resumeWork() {
        startChronometer();
        startMovementTrack();
        showPaused();
    }

    private void endWork() {
        pauseWork();
        Intent intent = new Intent(WorkingActivity.this, StatisticsActivity.class);
        String secondsWorked = mWorkingTimeText.getText().toString();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("total_time", secondsWorked);
        intent.putExtra("worked_time", Integer.toString(secondsMoving));
        intent.putIntegerArrayListExtra("graph_points", totalTimes);
        intent.putExtra("longitudes", longitudes);
        intent.putExtra("latitudes", latitudes);
        intent.putExtra("time_stamps", timeStamps);
        startActivity(intent);
    }

    /* *********************** Get location methods ************************** */

    private void requestLocUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                   != PackageManager.PERMISSION_GRANTED) {
                Intent noPermission = new Intent(this, NotAvailableGpsActivity.class);
                startActivity(noPermission);
            }
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_LISTENER_MIN_TIME,
                LOCATION_LISTENER_MIN_DISTANCE, mLocationListener);
    }


    /* *********************** Get movement methods ************************** */

    private void startMovementTrack() {
        handler.postDelayed(runnable, MOVING_TRACKER_CHECK_MILLISECONDS);
    }

    private void stopMovementTrack() {
        handler.removeCallbacks(runnable);
    }

    private void startRecording() {
        secondsMoving = 0;
    }

    private void checkMovement() {
        if(currentLatitude == 0) return;
        if(lastCheckedLongitude == 0) {
            lastCheckedLatitude = currentLatitude;
            lastCheckedLongitude = currentLongitude;
            if(!showedMessage) {
                showedMessage = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "GPS Location successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return;
        }
        if(currentLatitude != lastCheckedLatitude || currentLongitude != lastCheckedLongitude) {
            // Moved
            secondsMoving += MOVING_TRACKER_CHECK_SECONDS;
            isMoving = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMovingText(true);
                }
            });
            lastCheckedLatitude = currentLatitude;
            lastCheckedLongitude = currentLongitude;
            longitudes.add(currentLongitude);
            latitudes.add(currentLatitude);
            timeStamps.add(Calendar.getInstance().getTime());
        } else {
            // Not moved
            isMoving = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMovingText(false);
                }
            });
        }
    }


    /* *************************** UI methods **************************** */

    private void setMovingText(boolean isMoving) {
        if(isMoving) {
            mMovingText.setVisibility(View.VISIBLE);
            mNotMovingText.setVisibility(View.INVISIBLE);
        } else {
            mMovingText.setVisibility(View.INVISIBLE);
            mNotMovingText.setVisibility(View.VISIBLE);
        }
    }

    private void showPaused() {
        if(!working) {
            mMovingText.setVisibility(View.INVISIBLE);
            mNotMovingText.setVisibility(View.INVISIBLE);
            mPausedText.setVisibility(View.VISIBLE);
        } else {
            mPausedText.setVisibility(View.INVISIBLE);
            setMovingText(isMoving);
        }
    }

    private void startChronometer() {
        if(!working) {
            mWorkingTimeText.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mWorkingTimeText.start();
            working = true;
        }
    }

    private void stopChronometer() {
        if(working) {
            mWorkingTimeText.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mWorkingTimeText.getBase();
            working = false;
        }
    }
}
