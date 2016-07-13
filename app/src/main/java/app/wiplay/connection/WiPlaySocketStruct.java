package app.wiplay.connection;

import java.util.LinkedList;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/19/2015.
 */
public class WiPlaySocketStruct {

    private LinkedList<byte[]> OutData;
    private LinkedList<byte[]> InData;
    private WiPlayClient sock;
    private int offSet;
    private Thread callback;
    private Thread reader;
    private Thread writer;

    public WiPlaySocketStruct(WiPlayClient socket)
    {
        OutData = new LinkedList<>();
        InData = new LinkedList<>();
        offSet = 0;
        //exitThread = false;
        sock = socket;

        callback = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Constants.exitAll)
                {
                    CallbackImpl();
                }
            }
        });
        callback.start();

        writer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Constants.exitAll)
                {
//                    sock.Write(PopFromOutData());
                }
            }
        });

        reader = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Constants.exitAll) {
                    byte [] data = null;
                    //PacketCreator.AllocateBuffer(sock.PacketType(), data);
                    if(data.length > 1)
                        //sock.Read(data);
                    PushToInData(data);
                }
            }
        });
    }

    public void PushToOutData(byte[] data)
    {
        OutData.addLast(data);
        offSet += data.length;
    }

    public byte[] PopFromOutData()
    {
        if(OutData.isEmpty())
            return null;
        byte[] data = OutData.getFirst();
        OutData.removeFirst();
        return data;
    }

    public void PushToInData(byte[] data)
    {
        InData.addLast(data);
    }

    public byte[] PopFromInData()
    {
        if(InData == null || InData.isEmpty())
            return null;
        byte[] data = InData.getFirst();
        InData.removeFirst();
        return data;
    }


    public void CallbackImpl()
    {
            PacketParser.ParsePacket(PopFromInData(), sock);
    }

    public void cleanUp()
    {
        OutData.clear();
        OutData = null;
        InData.clear();
        InData = null;
        Constants.exitAll = true;
    }
}
