package com.example.movementtracker.Utils;

public class Constants {
    public static final int REQUEST_LOCATION_CODE = 101;
    public static final int LOCATION_REQUEST_INTERVAL = 30000;
    public static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;
    public static final int LOCATION_LISTENER_MIN_TIME = 1000;
    public static final int LOCATION_LISTENER_MIN_DISTANCE = 5;
    private static final int LOCATION_DECIMAL_ACCURACY = 4;
    public static final int MOVING_TRACKER_CHECK_SECONDS = 3;


    /* Dependent constants */
    public static final double LOCATION_ROUNDING = Math.pow(10, LOCATION_DECIMAL_ACCURACY);
    public static final int MOVING_TRACKER_CHECK_MILLISECONDS = MOVING_TRACKER_CHECK_SECONDS * 1000;
}
