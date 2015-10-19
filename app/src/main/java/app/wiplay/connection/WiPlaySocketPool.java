package app.wiplay.connection;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketPool {

    /* Private Data Members */
    private HashMap<Socket, WiPlaySocketStruct> poolMap;

    /* Private Methods */

    private Socket FindSocket()
    {
        /* TODO: Implement */
        return null;
    }

    private boolean IsDataAvailable()
    {
        /* TODO: Implement */
        return true;
    }


    /* Public Methods */

    public WiPlaySocketPool()
    {
        poolMap = new HashMap<>();

    }

    public void AddToPool(Socket socket)
    {
        WiPlaySocketStruct struct = new WiPlaySocketStruct();
        poolMap.put(socket, struct);
    }

    public void SendData(Socket socket, byte[] data)
    {
        poolMap.get(socket).PushToOutGoingData(data);
    }

    public void ReadData(Socket socket, byte[] data)
    {
        /* TODO: Implement */
    }

}
