package kizema.anton.weatherapp.helpers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import kizema.anton.weatherapp.App;
import kizema.anton.weatherapp.model.UserPrefs;

public class GPSTrackerService extends Service implements LocationListener {

    private Location location;

    private static final long MIN_DISTANCE_UPDATES = 1;//1 km
    private static final long MIN_TIME = 1000;

    protected LocationManager locationManager;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            startWork();
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    private int startId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LOC", "onStartCommand " + startId);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        this.startId = startId;

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    private Location startWork() {
        Log.d("LOC", "calculateLocation");

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            getLocationFromProvider(LocationManager.NETWORK_PROVIDER);
        }

        if (locationManager.getAllProviders().contains(LocationManager.PASSIVE_PROVIDER)) {
            getLocationFromProvider(LocationManager.PASSIVE_PROVIDER);
        }

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            getLocationFromProvider(LocationManager.GPS_PROVIDER);
        }

        return location;
    }

    private void getLocationFromProvider(String provider) {
        locationManager.requestLocationUpdates(
                provider, MIN_TIME,
                MIN_DISTANCE_UPDATES, this);

        if (locationManager != null) {
            Log.d("LOC", "locationManager != null");
            location = locationManager
                    .getLastKnownLocation(provider);
            if (location != null) {
                Log.d("LOC", "location != null");

                Log.d("LOC", "diff : " + (System.currentTimeMillis() - location.getTime()) / (1000 * 60 * 60) + " hours");
                if (System.currentTimeMillis() - location.getTime() > 1000 * 60 * 60 * 24) {
                    Log.d("LOC", "Too old");
                    //Too old location
                    location = null;
                }

            }
        }
    }

    private void stopUsingGPS() {
        Log.d("LOC", "stopUsingGPS() ");
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTrackerService.this);
            locationManager = null;
        }

        App.uiHandler.post(new Runnable() {
            @Override
            public void run() {
                stopSelf(startId);
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("LOC", "onDestroy() ");
    }

    @Override
    public void onLocationChanged(final Location location) {
        this.location = location;

        Log.d("LOC", "Appeared location : newLat: " + location.getLatitude() +
                "  ; newLon: " + location.getLongitude());

        App.uiHandler.post(new Runnable() {
            @Override
            public void run() {
                UserPrefs prefs = UserPrefs.getPrefs();
                prefs.lat = location.getLatitude();
                prefs.lon = location.getLongitude();
                prefs.save();

                //send updated location broadcast
            }
        });

        //save and stop
        stopUsingGPS();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}


