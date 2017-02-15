package app.wiplay.framework;

import java.net.Socket;

import app.wiplay.connection.WiPlaySocket;

/**
 * Created by Puran Chand on 12/1/17.
 */

public class ReadEvent extends Event {

    private WiPlaySocket _socket;

    public ReadEvent(WiPlaySocket s)
    {
        _socket = s;
    }

    public int Execute()
    {
        //TODO: Implement This
        return 0;
    }
}
