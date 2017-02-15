package app.wiplay.connection;

import java.util.LinkedList;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private LinkedList<byte[]> _readData;
    private LinkedList<byte[]> _writeData;
    private WiPlaySocket sock;
    private int offSet;

    public WiPlaySocketStruct(WiPlaySocket socket)
    {
        _readData = new LinkedList<>();
        _writeData = new LinkedList<>();
        offSet = 0;
        sock = socket;
    }

    public void PushWriteData(byte[] data)
    {
        _writeData.addLast(data);
        offSet += data.length;
    }

    public byte[] PopWriteData()
    {
        if(_writeData.isEmpty())
            return null;
        byte[] data = _writeData.getFirst();
        _writeData.removeFirst();
        return data;
    }

    public void PushReadData(byte[] data)
    {
        _readData.addLast(data);
    }

    public byte[] PopReadData()
    {
        if(_readData == null || _readData.isEmpty())
            return null;
        byte[] data = _readData.getFirst();
        _readData.removeFirst();
        return data;
    }

    public void cleanUp()
    {
        _writeData.clear();
        _writeData = null;
        _readData.clear();
        _readData = null;
        Constants.exitAll = true;
    }
}
