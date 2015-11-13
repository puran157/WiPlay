package app.wiplay.connection;

import android.util.Log;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocket {

    /* Data Members */
    private Object socket;
    private String hostname;
    private boolean isServer;
    private WiPlaySocketPool pool;
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
                //hostname = ((ServerSocket) socket).getLocalSocketAddress().toString();
                hostname = getWifiApIpAddress();
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
            } catch (Exception e) {

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

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d(Constants.Tag, inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(Constants.Tag, ex.toString());
        }
        return null;
    }

    public void cleanUp()
    {
        pool.cleanUp();
        socket = null;
    }
}
