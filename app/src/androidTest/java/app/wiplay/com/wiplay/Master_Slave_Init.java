package app.wiplay.com.wiplay;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import app.wiplay.constants.Constants;
import app.wiplay.framework.WiPlayMaster;
import app.wiplay.framework.WiPlaySlaves;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class Master_Slave_Init extends ApplicationTestCase<Application> {

    private WiPlayMaster master;
    private WiPlaySlaves slave1;
    private WiPlaySlaves slave2;
    public Master_Slave_Init() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void runTest() throws Throwable {
        Log.i(Constants.Tag, "Starting test");
        super.runTest();
        master = new WiPlayMaster();
        Thread.sleep(5000);
        slave1 = new WiPlaySlaves("localhost");
        slave2 = new WiPlaySlaves("localhost");
        slave1.cleanUp();
        slave2.cleanUp();
        master.cleanUp();
        slave1 = null;
        slave2 = null;
        master = null;
        Log.i(Constants.Tag,"Ending test");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}