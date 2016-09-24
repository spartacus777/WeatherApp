package kizema.anton.weatherapp;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import kizema.anton.weatherapp.activities.stations.WeatherListActivity;

public class SimpleRobotiumTest extends
        ActivityInstrumentationTestCase2<WeatherListActivity> {

    private Solo solo;

    public SimpleRobotiumTest() {
        super(WeatherListActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testListItemClickShouldDisplayToast() throws Exception {
        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", WeatherListActivity.class);

        // Click a button which will start a new Activity
        // Here we use the ID of the string to find the right button
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
