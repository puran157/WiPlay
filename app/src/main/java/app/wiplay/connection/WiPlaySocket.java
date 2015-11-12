package app.wiplay.connection;

import android.util.Log;

import java.io.IOException;
import java.net.*;
import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 * TODO: Shall we make it singleton?
 */
public class WiPlaySocket {

    /* Data Members */
    private Object socket;
    private String hostname;
    private boolean isServer;
    private WiPlaySocketPool pool; //TODO: make it array so more threads can be started to handle multiple requests
    /* Private Methods */

    /* Public Methods */

    public WiPlaySocket()
    {
        socket = null;
        hostname = null;
        pool = new WiPlaySocketPool();
        this.isServer = false;
    }

    public WiPlaySocket(WiPlayServer sock)
    {
        socket = null;
        hostname = null;
        pool = new WiPlaySocketPool(sock);
        this.isServer = true;
    }

    public String getHostname()
    {
        return hostname;
    }
    public WiPlaySocketPool getPool() { return pool;}

    /* this call will take care for Bind, Connect */
    public void CreateSocket(boolean isControl)
    {
        try {
            if (isServer) {
                if(isControl)
                    socket = new ServerSocket(Constants.CONTROL_PORT);
                else
                    socket = new ServerSocket(Constants.DATA_PORT);
                hostname = ((ServerSocket) socket).getLocalSocketAddress().toString();
                Log.i(Constants.Tag,"Server Socket created @"+hostname);
            }
            else {
                /* Its a client Socket */
                if(isControl)
                    socket  = new Socket(hostname, Constants.CONTROL_PORT);
                else
                    socket  = new Socket(hostname, Constants.DATA_PORT);
            }
        }
        catch(IOException e) {
            Log.i(Constants.Tag, "CreateSocket Exception " + e);
        }
    }

    public void Listen()
    {
        if(socket != null) {
            try {
                Socket clientSock = ((ServerSocket) socket).accept();
                pool.AddToPool((clientSock));
            } catch (IOException e) {

            }
        }
    }

    public void SendData(Socket sock, byte[] data)
    {
        pool.SendData(sock, data);
    }

    public void ReadData(Socket sock, byte[] data)
    {
        pool.ReadData(sock, data);
    }
}
