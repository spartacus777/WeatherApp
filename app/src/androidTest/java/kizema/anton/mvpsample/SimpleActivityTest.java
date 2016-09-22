package kizema.anton.mvpsample;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import kizema.anton.mvpsample.activities.stations.StationsListActivity;

/**
 * Created by somename on 22.09.2016.
 */
public class SimpleActivityTest extends
        ActivityInstrumentationTestCase2<StationsListActivity> {

    private Solo solo;

    public SimpleActivityTest() {
        super(StationsListActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testListItemClickShouldDisplayToast() throws Exception {
        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", StationsListActivity.class);

        // Click a button which will start a new Activity
        // Here we use the ID of the string to find the right button
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
