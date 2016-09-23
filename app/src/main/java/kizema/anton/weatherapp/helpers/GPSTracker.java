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
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by somename on 23.09.2016.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    private Location location;

    private static final long MIN_DISTANCE_UPDATES = 1;//1 km
    private static final long MIN_TIME = 1000;

    protected LocationManager locationManager;

    private OnLocationChangedListener listener;

    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }

    public GPSTracker(Context context) {
        this.context = context;
        calculateLocation();
    }

    public void setLocationChangeListener(OnLocationChangedListener listener) {
        this.listener = listener;
    }

    public Location calculateLocation() {
        Log.d("LOC", "calculateLocation");

        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);

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

    public void stopUsingGPS() {
        Log.d("LOC", "stopUsingGPS() ");
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
            locationManager = null;
        }
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(final Location location) {
        this.location = location;
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     */
    public void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

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


