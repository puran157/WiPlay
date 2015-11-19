package app.wiplay.connection;

import java.net.Socket;

import app.wiplay.filemanager.FileManager;
import app.wiplay.framework.WiPlayMaster;

/**
 * Created by pchand on 11/12/2015.
 */
public class WiPlayServer extends WiPlaySocket {
    private WiPlaySocketPool pool;

    public WiPlayServer(WiPlayMaster callback)
    {
        super(callback);
        pool = new WiPlaySocketPool();
        CreateSocket(true);
    }

    public WiPlaySocketPool getPool() { return pool;}

    public String gethostName()
    {
        return super.getHostname();
    }

    public void Listen()
    {
        if(getServerSocket() != null) {
            try {
                Socket clientSock = getServerSocket().accept();
                pool.AddToPool(new WiPlayClient(clientSock, this.getCallbackMaster()));
            } catch (Exception e) {

            }
        }
    }

    public void cleanUp()
    {
        super.cleanUp();
        pool.cleanUp();
        pool = null;
    }
}
