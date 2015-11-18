package app.wiplay.connection;

import android.util.Log;

import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;

/**
 * Created by pchand on 10/22/2015.
 * TODO: It should be singleton
 */
public class PacketParser {

    private static void ParseAskPacket(byte[] data, final WiPlaySocket socket)
    {
        /* start sending the file to this client */
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                socket.getCallbackMaster().SendFile(socket);
            }
        });
        t.start();
    }

    private static void ParseFilePacket(byte[] data, WiPlaySocket socket)
    {
        /* start storing the packets into some tmp file */
        FileManager f = new FileManager(Constants.tmp_file);
        int length =        (data[1]<<24)&0xff000000|
                            (data[2]<<16)&0x00ff0000|
                            (data[3]<< 8)&0x0000ff00|
                            (data[4]<< 0)&0x000000ff;
        int offset =        (data[5]<<24)&0xff000000|
                            (data[6]<<16)&0x00ff0000|
                            (data[7]<< 8)&0x0000ff00|
                            (data[8]<< 0)&0x000000ff;

        byte[] buffer = new byte[length];

        for(int i = 0; i < length; ++i)
            buffer[i] = data[9+i];

        f.InitialiseWriter();
        f.WriteChunk(buffer, offset);
        f.DeInit();
    }

    private static void ParseFileDonePacket(byte[] data, WiPlaySocket socket)
    {
        /* we can drop the data plain connection  in case of clients
        * In case of server we need to send play Command to all clients */
    }

    private static void ParsePlayPacket(byte[] data, WiPlaySocket socket)
    {
        /* start playing the video */
    }

    private static void ParsePausePacket(byte[] data, WiPlaySocket socket)
    {
        /* Pause the video and set the head to the time stamp received */
    }

    private static void ParseStopPacket(byte[] data, WiPlaySocket socket)
    {
        /* Stop the video, delete the tmp file, close control plain connection also */
        //TODO: Stop the video check if master is not null
        socket.getCallbackMaster().cleanUp();
    }

    public static void ParsePacket(byte[] data, WiPlaySocket socket)
    {
        if(data == null)
            return;

        if(data[0] == Constants.ASK_FILE)
            ParseAskPacket(data, socket);
        else if(data[0] == Constants.SEND_FILE)
            ParseFilePacket(data, socket);
        else if(data[0] == Constants.FILE_DONE)
            ParseFileDonePacket(data, socket);
        else if(data[0] == Constants.PLAY)
            ParsePlayPacket(data, socket);
        else if(data[0] == Constants.PAUSE)
            ParsePausePacket(data, socket);
        else if (data[0] == Constants.STOP)
            ParseStopPacket(data, socket);
        else
            Log.e(Constants.Tag, "Invalid Packet");
    }
}
