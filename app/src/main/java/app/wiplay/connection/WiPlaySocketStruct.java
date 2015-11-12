package app.wiplay.connection;

import java.util.LinkedList;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private LinkedList<byte[]> OutData;
    private LinkedList<byte[]> InData;
    private int offSet;

    public WiPlaySocketStruct()
    {
        OutData = new LinkedList<>();
        InData = new LinkedList<>();
        offSet = 0;
    }

    public boolean PushToOutGoingData(byte[] data)
    {
        OutData.addLast(data);
        offSet += data.length;
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
