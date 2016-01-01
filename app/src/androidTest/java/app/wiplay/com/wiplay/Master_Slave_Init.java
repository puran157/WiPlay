package app.wiplay.com.wiplay;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import app.wiplay.constants.Constants;
import app.wiplay.framework.WiPlayMaster;
import app.wiplay.framework.WiPlaySlaves;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

//TODO: This test is failing, FIX
public class Master_Slave_Init extends ApplicationTestCase<Application> {

    private WiPlayMaster master;
    private WiPlaySlaves slave1;
    private WiPlaySlaves slave2;
    private Context c;
    public Master_Slave_Init() {
        super(Application.class);
        c = getSystemContext();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void runTest() throws Throwable {
        Log.i(Constants.Tag, "Starting test");
        super.runTest();
        Creation();
        cleanUpOrdered();
        Creation();
        cleanUpUnordered();
        Log.i(Constants.Tag, "Ending test");
    }

    void Creation()
    {
        master = new WiPlayMaster(c);
        assertNotNull(master);
        slave1 = new WiPlaySlaves("localhost");
        assertNotNull(slave1);
        slave2 = new WiPlaySlaves("localhost");
        assertNotNull(slave2);

    }

    void cleanUpOrdered()
    {
        assertNotNull(master);
        assertNotNull(slave1);
        assertNotNull(slave2);
        slave1.cleanUp();
        slave2.cleanUp();
        master.cleanUp();

        master = null;
        slave2 = null;
        slave2 = null;
    }

    void cleanUpUnordered()
    {

        assertNotNull(master);
        assertNotNull(slave1);
        assertNotNull(slave2);

        master.cleanUp();
        slave1.cleanUp();
        slave2.cleanUp();

        master = null;
        slave2 = null;
        slave2 = null;

    }

    void SendFile()
    {
        assertNotNull(master);
        assertNotNull(slave1);
        assertNotNull(slave2);
        slave1.AskFile();
        slave2.AskFile();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}