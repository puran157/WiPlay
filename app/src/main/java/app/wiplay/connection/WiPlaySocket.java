package app.wiplay.connection;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.*;
import app.wiplay.Constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocket {

    /* Data Members */
    private Object socket;
    private String hostname;
    private WiPlaySocketPool pool;
    private WiPlayHotSpot hotSpot;
    /* Private Methods */

    /* Public Methods */

    public WiPlaySocket(boolean isServer)
    {
        socket = null;
        hostname = null;
        hotSpot = null;
        pool = new WiPlaySocketPool();
    }

    /* this call will take care for Bind, Connect */
    public void CreateSocket(boolean isServer, Context context, boolean isControl)
    {
        try {
            if (isServer) {
                if(isControl)
                    socket = new ServerSocket(Constants.CONTROL_PORT);
                else
                    socket = new ServerSocket(Constants.DATA_PORT);
                hostname = ((ServerSocket) socket).getInetAddress().getHostName();
                hotSpot = new WiPlayHotSpot(hostname, context);
                Log.i(Constants.Tag,"Server Socket created @"+hostname+":1570");
            }
            else {
                /* Its a client Socket */
                hostname = hotSpot.getHostName();
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
        try {
            Socket clientSock = ((ServerSocket) socket).accept();
            pool.AddToPool((clientSock));
        }
        catch(IOException e)
        {

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
