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
public class HotSpotTest  extends ApplicationTestCase<Application> {

    private WiPlayHotSpot hotspot;
    private Context c;

    public HotSpotTest() {
        super(Application.class);
        //mApp.startActivity(new Intent(mApp.getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        c = getSystemContext();
    }

    @Override
    protected void runTest() throws Throwable
    {
        super.runTest();

        assertEquals(true,CreateHotspot());
        StartHotspot();
        CleanUp();
    }
    
    boolean CreateHotspot()
    {
        assertNotNull(c);
        hotspot = new WiPlayHotSpot(c);
        if(hotspot != null)
            return true;
        else
            return false;
    }

    void StartHotspot()
    {
        assertNotNull(hotspot);
        hotspot.StartHotSpot();
    }

    void Connect()
    {
        assertNotNull(hotspot);
        hotspot.ConnectToHotSpot();
    }

    void CleanUp()
    {
        assertNotNull(hotspot);
        hotspot.cleanUp();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
