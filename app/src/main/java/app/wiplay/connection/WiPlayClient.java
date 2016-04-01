package app.wiplay.connection;

import java.net.Socket;


/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayClient extends WiPlaySocket {

    private boolean canBeServer;

    public WiPlayClient(String host, WiPlayServer server)
    {
        super(host, server);
        canBeServer = false;
    }

    public WiPlayClient(Socket s, WiPlayServer server)
    {
        super(s, server);
    }

    public void setCanBeServer(boolean flag)
    {
        canBeServer = flag;
    }
}
