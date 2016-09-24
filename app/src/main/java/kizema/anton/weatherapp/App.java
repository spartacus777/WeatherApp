package kizema.anton.weatherapp;

import android.app.Application;
import android.os.Handler;

import com.activeandroid.ActiveAndroid;
import com.squareup.leakcanary.LeakCanary;

import kizema.anton.weatherapp.helpers.LocationHelper;
import kizema.anton.weatherapp.helpers.UILHelper;

public class App extends Application {

    public static Handler uiHandler;

    @Override public void onCreate() {
        super.onCreate();

        LocationHelper.getRealCoordinates(getApplicationContext());

        ActiveAndroid.initialize(getApplicationContext());
        LeakCanary.install(this);

        UILHelper.init(getApplicationContext());

        uiHandler = new Handler();
    }



}
