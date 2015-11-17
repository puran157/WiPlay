package app.wiplay.connection;

import java.net.Socket;

/**
 * Created by pchand on 11/12/2015.
 */
public class WiPlayServer extends WiPlaySocket {
    private WiPlaySocketPool pool;

    public WiPlayServer()
    {
        super();
        pool = new WiPlaySocketPool();
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
                pool.AddToPool(new WiPlayClient(clientSock));
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
