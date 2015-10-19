package app.wiplay.connection;

import java.net.Socket;

import app.wiplay.Constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private byte[] OutData;
    private byte[] InData;
    private boolean isOutDataAvailable;
    private boolean isInDataAvailable;

    public WiPlaySocketStruct()
    {
        isDataAvailable = false;
        OutData = new Byte(Constants.BUFFER_SIZE);
        InData = new Byte(Constants.BUFFER_SIZE);
    }

    public boolean PushToOutGoingData(byte[] data)
    {
        int available = Constants.BUFFER_SIZE - OutData.length;
        if(data.length <= available )
        {
            System.arraycopy(OutData, OutData.length, data, 0, data.length);
            isOutDataAvailable = true;
            return true;
        } else {
            /* Come back later and try again */
            return false;
        }
    }

    public boolean ReadFromIncomingData(byte[] data)
    {
        if(InData.length > 0 && isInDataAvailable) {
            System.arraycopy(data, 0, InData, 0, InData.length);
            isInDataAvailable = false;
        }
    }
}
