package kizema.anton.weatherapp.helpers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class LocationHelper {
    private static Context context;

    public interface OmLocationReceived{
        void onReceivedLocation(double lat, double lon);
    }

    private LocationHelper() {
    }

    public static void init(Context c) {
        context = c;
    }

    public static void getRealCoordinates(final OmLocationReceived listener) {
        Log.d("LOC", "getRealCoordinates");
        final GPSTracker gpsTracker = new GPSTracker(context);
        gpsTracker.setLocationChangeListener(new GPSTracker.OnLocationChangedListener() {
            @Override
            public void onLocationChanged(Location l) {
                if (l != null) {
                    gpsTracker.stopUsingGPS();
                    listener.onReceivedLocation(l.getLatitude(), l.getLongitude());
                }
            }
        });

        if (gpsTracker.getLocation() != null) {
            gpsTracker.stopUsingGPS();
            listener.onReceivedLocation(gpsTracker.getLocation().getLatitude(), gpsTracker.getLocation().getLongitude());
        }
    }

    public static void updateLocation(final double newLat, final double newLon) {
        Log.d("LOC", "Appeared location : newLat: " + newLat + "  ; newLon: " + newLon);

    }


}

