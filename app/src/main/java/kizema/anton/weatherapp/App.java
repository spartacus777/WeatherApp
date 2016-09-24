package kizema.anton.weatherapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.squareup.leakcanary.LeakCanary;

import kizema.anton.weatherapp.helpers.LocationHelper;
import kizema.anton.weatherapp.helpers.UILHelper;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        LocationHelper.init(this);

        ActiveAndroid.initialize(getApplicationContext());
        LeakCanary.install(this);

        UILHelper.init(getApplicationContext());
    }



}
