package app.wiplay.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import app.wiplay.Constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketPool {

    /* Private Data Members */
    private HashMap<Socket, WiPlaySocketStruct> poolMap;
    private Thread reader;
    private Thread writer;

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

        reader = new Thread(new Runnable() {
            @Override
            public void run() {
                /* read in Data from socket */
                while(true)
                {
                    if(!poolMap.isEmpty())
                    {
                        Object[] keys = poolMap.keySet().toArray();
                        for(int i = 0; i < keys.length; ++i)
                        {
                            /* Algo
                            * Read the BUFFER_SIZE - available bytes for now */
                            try {
                                WiPlaySocketStruct struct = poolMap.get(keys[i]);
                                byte[] arr = new byte[Constants.BUFFER_SIZE - struct.getInDataAvailable()];
                                ((Socket) keys[i]).getInputStream().read(arr, 0, arr.length);
                                struct.ReadData(arr);
                            }
                            catch(IOException e)
                            {}
                        }
                    }
                }
            }
        });

        writer = new Thread(new Runnable() {
            @Override
            public void run() {
                /* write data to socket */

                while(true)
                {
                    if(!poolMap.isEmpty())
                    {
                        Object[] keys = poolMap.keySet().toArray();

                        for(int i = 0; i < keys.length; ++i)
                        {
                            /* Algo
                            * Write the available bytes for now */
                            try {
                                WiPlaySocketStruct struct = poolMap.get(keys[i]);
                                byte[] arr = new byte[struct.getOutDataAvailable()];
                                struct.WriteData(arr);
                                ((Socket) keys[i]).getOutputStream().write(arr, 0, arr.length);
                            }
                            catch (IOException e){}
                        }
                    }
                }
            }
        });

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
        poolMap.get(socket).ReadFromIncomingData(data);
    }

}
