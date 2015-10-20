package app.wiplay.connection;

import java.net.Socket;

import app.wiplay.Constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private byte[] OutData;
    private byte[] InData;
    private int OutDataAvailable;
    private int InDataAvailable;

    public WiPlaySocketStruct()
    {
        OutDataAvailable = 0;
        InDataAvailable = 0;
        OutData = new byte[Constants.BUFFER_SIZE];
        InData = new byte[Constants.BUFFER_SIZE];
    }

    public int getInDataAvailable() {
        return InDataAvailable;
    }

    public int getOutDataAvailable() {
        return OutDataAvailable;
    }

    public boolean PushToOutGoingData(byte[] data)
    {
        if(data.length <= OutDataAvailable )
        {
            System.arraycopy(OutData, OutData.length, data, 0, data.length);
            OutDataAvailable += data.length;
            return true;
        } else {
            /* Come back later and try again */
            return false;
        }
    }

    public boolean ReadFromIncomingData(byte[] data)
    {
        if(InDataAvailable > 0) {
            System.arraycopy(data, 0, InData, 0, data.length);
            InDataAvailable -= data.length;
            return true;
        }
        else
        {
            /* fallback algo */
            return false;
        }
    }

    public void ReadData(byte[] data)
    {
        System.arraycopy(data, 0, InData, InDataAvailable, data.length);
        InDataAvailable += data.length;
    }

    public void WriteData(byte[] data)
    {
        System.arraycopy(OutData, 0, data, 0, OutDataAvailable);
        OutDataAvailable = 0;
    }
}
