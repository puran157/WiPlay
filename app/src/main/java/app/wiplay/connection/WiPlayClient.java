package app.wiplay.connection;

import java.net.Socket;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayClient extends WiPlaySocket {

    public WiPlayClient(Socket sock)
    {
        super(sock, "");
    }

    public WiPlayClient(Socket sock, String host)
    {
        super(sock, host);
    }
}
