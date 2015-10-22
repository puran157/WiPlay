package app.wiplay.connection;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import app.wiplay.Constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private LinkedList<byte[]> OutData;
    private LinkedList<byte[]> InData;

    public WiPlaySocketStruct()
    {
        OutData = new LinkedList<>();
        InData = new LinkedList<>();
    }

    public boolean PushToOutGoingData(byte[] data)
    {
        OutData.addLast(data);
        return true;
    }

    public boolean ReadFromIncomingData(byte[] data)
    {
        data = InData.getFirst();
        InData.removeFirst();
        return true;
    }

    public void WriteToMap(byte[] data)
    {
        InData.addLast(data);
    }

    public void ReadFromMap(byte[] data)
    {
        data = OutData.getFirst();
        OutData.removeFirst();
    }
}
