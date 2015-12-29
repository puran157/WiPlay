package app.wiplay.com.wiplay;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import app.wiplay.connection.WiPlayHotSpot;
import app.wiplay.ui.MainActivity;

/**
 * Created by pchand on 12/29/2015.
 */
public class HotSpotTest  extends ActivityUnitTestCase<MainActivity> {

    private WiPlayHotSpot hotspot;
    private Context c;

    public HotSpotTest() {
        super(MainActivity.class);
        //mApp.startActivity(new Intent(mApp.getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
        c = getActivity().getApplicationContext();
    }

    @Override
    protected void runTest() throws Throwable
    {
        super.runTest();
        hotspot = new WiPlayHotSpot(c);
        hotspot.StartHotSpot();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
