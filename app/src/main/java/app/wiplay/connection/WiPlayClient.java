package app.wiplay.connection;

import java.net.Socket;

import app.wiplay.framework.WiPlayMaster;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayClient extends WiPlaySocket {

    public WiPlayClient(Socket sock, WiPlayMaster callback)
    {
        super(sock, "", callback);
    }

    public WiPlayClient(Socket sock, String host, WiPlayMaster callback)
    {
        super(sock, host, callback);
    }
}
