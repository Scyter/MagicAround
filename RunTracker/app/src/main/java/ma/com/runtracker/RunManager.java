package ma.com.runtracker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class RunManager implements LocationListener {
    private static final String TAG = "RunManager";
    public static final String ACTION_LOCATION = "com.ma.runtracker.ACTION_LOCATION";

    private static final String PREFS_FILE = "runs";
    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 99;

    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    private static RunManager sRunManager;
    private Context mAppContext;
    private LocationManager locationManager;

    private RunManager(Context appContext) {
        mAppContext = appContext;
        locationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mHelper = new RunDatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public static RunManager get(Context c) {
        if (sRunManager == null) {
            sRunManager = new RunManager(c.getApplicationContext());
        }
        return sRunManager;
    }

    public Run getRun(long id) {
        Run run = null;
        RunDatabaseHelper.RunCursor cursor = mHelper.queryRun(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
            run = cursor.getRun();
        cursor.close();
        return run;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    public RunDatabaseHelper.LocationCursor queryLocationsForRun(long runId) {
        return mHelper.queryLocationsForRun(runId);
    }

    public void startLocationUpdates(Activity activity) {
        try {
            double longitude = 0.0;
            double latitude = 0.0;

            // Get GPS and network status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean locationServiceAvailable;
            Location location;
            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                locationServiceAvailable = false;
            }
            //else
            {
                locationServiceAvailable = true;

                Intent broadcast = new Intent(ACTION_LOCATION);
                PendingIntent pi = PendingIntent.getBroadcast(mAppContext, 0, broadcast, 0);


                if (isNetworkEnabled && ContextCompat.checkSelfPermission(mAppContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, pi);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateCoordinates(location);
                    }
                }
                if (isGPSEnabled && ContextCompat.checkSelfPermission(mAppContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, pi);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateCoordinates(location);
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    private void updateCoordinates(Location location) {
        broadcastLocation(location);
        Log.d("aaa", "updateCoordinates");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("aaa", "onLocationChanged");
        broadcastLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("aaa", "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("aaa", "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("aaa", "onProviderDisabled");
    }

    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        mAppContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            locationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    public boolean isTrackingRun(Run run) {
        return run != null && run.getId() == mCurrentRunId;
    }

    public Run startNewRun(Activity activity) {
        Run run = insertRun();
        startTrackingRun(run, activity);
        return run;
    }

    public void startTrackingRun(Run run, Activity activity) {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).apply();
        startLocationUpdates(activity);
    }

    public void stopRun() {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).apply();
    }

    private Run insertRun() {
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    public void insertLocation(Location loc) {
        if (mCurrentRunId != -1) {
            mHelper.insertLocation(mCurrentRunId, loc);
        } else {
            Log.e(TAG, "Location received with no tracking run; ignoring.");
        }
    }

    public Location getLastLocationForRun(long runId) {
        Location location = null;
        RunDatabaseHelper.LocationCursor cursor = mHelper.queryLastLocationForRun(runId);
        cursor.moveToFirst();
// Если набор не пуст, получить местоположение
        if (!cursor.isAfterLast())
            location = cursor.getLocation();
        cursor.close();
        return location;
    }

    public RunDatabaseHelper.RunCursor queryRuns() {
        return mHelper.queryRuns();
    }

}