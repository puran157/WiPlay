package app.wiplay.connection;

import android.util.Log;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

import app.wiplay.constants.Constants;
import app.wiplay.framework.WiPlayMaster;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocket {

    /* Data Members */
    private Object socket;
    private String hostname;
    private boolean isServer;
    private WiPlaySocketStruct queue;
    private WiPlayMaster callbackMaster;
    /* Private Methods */

    /* Public Methods */

    public WiPlaySocket(WiPlayMaster callback)
    {
        socket = null;
        hostname = "";
        isServer = true;
        queue = null;
        callbackMaster = callback;
    }

    public WiPlaySocket(Socket sock, String host, WiPlayMaster callback)
    {
        socket = sock;
        isServer = false;
        hostname = host;
        queue = new WiPlaySocketStruct(this);
        callbackMaster = callback;
    }

    public String getHostname()
    {
        return hostname;
    }

    public WiPlayMaster getCallbackMaster()
    {
        return callbackMaster;
    }

    public Socket getClientSocket()
    {
        if(isServer == false)
            return (Socket)socket;
        else
            return null;
    }

    public ServerSocket getServerSocket()
    {
        if(isServer == true)
            return (ServerSocket)socket;
        else
            return null;
    }



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
                if(isControl) {
                    socket = new Socket(hostname, Constants.CONTROL_PORT);
                }
                else {
                    socket = new Socket(hostname, Constants.DATA_PORT);
                }
                Log.i(Constants.Tag, "Client connected @"+hostname);
            }
        }
        catch(IOException e) {
            Log.i(Constants.Tag, "CreateSocket Exception " + e);
        }
    }

    public void Send(byte[] data)
    {
        queue.PushToOutData(data);
    }

    public void SendData(byte[] data) {
        if (data == null){
            Log.i(Constants.Tag, "No data to send\n");
            return;
        }
        try {
            this.getClientSocket().getOutputStream().write(data, 0, data.length);
        } catch (IOException e) {
            Log.i(Constants.Tag, "Exception happened while sending \n");
            e.printStackTrace();
        }
    }

    public int PacketType()
    {
        try {
            if(this.getClientSocket().getInputStream().available() > 0)
                return this.getClientSocket().getInputStream().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int ReadData(byte[] data)
    {
        //pool.ReadData(sock, data);
        int read = 0;
        try {
            if(this.getClientSocket().getInputStream().available() > 0)
                read =  this.getClientSocket().getInputStream().read(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return read;
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
        try {
            if(isServer)
                getServerSocket().close();
            else
                getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
        if(queue != null) {
            queue.cleanUp();
            queue = null;
        }
        hostname = null;
    }
}
