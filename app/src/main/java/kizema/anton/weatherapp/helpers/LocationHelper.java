package kizema.anton.weatherapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationHelper {

    private LocationHelper() {}

    public static void getRealCoordinates(Context context) {
        Log.d("LOC", "getRealCoordinates");

        Intent intent = new Intent(context, TrackerService.class);
        context.startService(intent);
    }

}

