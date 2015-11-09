package app.wiplay.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by pchand on 10/19/2015.
 * WiPlaySocketPool will maintain two threads for maps of socket,
 * (Incase of client the map will contain two sockets only (Control, Data socket))
 * One thread will loop over all sockets and write the data to wire and vice versa for other thread
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

    /* This method will start two threads, reader and writer
     * Reader will read from socket and write it to map
     * Writer will read from map and write it to socket
     * TODO: Increase the number of threads if connections exceeds threshold/ or we will not make in singleton
     */
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
                                byte[] arr = null;
                                ((Socket) keys[i]).getInputStream().read(arr, 0, arr.length);
                                struct.WriteToMap(arr);
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
                                byte[] arr = null;
                                struct.ReadFromMap(arr);
                                ((Socket) keys[i]).getOutputStream().write(arr, 0, arr.length);
                            }
                            catch (IOException e){}
                        }
                    }
                }
            }
        });

    }
    /*
    * New Socket will be added to the pool
    * */
    public void AddToPool(Socket socket)
    {
        WiPlaySocketStruct struct = new WiPlaySocketStruct();
        poolMap.put(socket, struct);
    }

    /* Write data to the socketpool Map
     */
    public void SendData(Socket socket, byte[] data)
    {
        poolMap.get(socket).PushToOutGoingData(data);
    }

    /*
        Read data from Socket pool map
        TODO: Is it really required?
     */
    public void ReadData(Socket socket, byte[] data)
    {
        /* TODO: Implement */
        poolMap.get(socket).ReadFromIncomingData(data);
    }

}
