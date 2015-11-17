package app.wiplay.connection;

import android.util.Log;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/22/2015.
 * TODO: It should be singleton
 */
public class PacketParser {

    private static void ParseAskPacket(byte[] data)
    {
        /* start sending the file to this client */
    }

    private static void ParseFilePacket(byte[] data)
    {
        /* start storing the packets into some tmp file */
    }

    private static void ParseFileDonePacket(byte[] data)
    {
        /* we can drop the data plain connection  in case of clients
        * In case of server we need to send play Command to all clients */
    }

    private static void ParsePlayPacket(byte[] data)
    {
        /* start playing the video */
    }

    private static void ParsePausePacket(byte[] data)
    {
        /* Pause the video and set the head to the time stamp received */
    }

    private static void ParseStopPacket(byte[] data)
    {
        /* Stop the video, delete the tmp file, close control plain connection also */
    }

    public static void ParsePacket(byte[] data, WiPlaySocket socket)
    {
        if(data == null)
            return;

        if(data[0] == Constants.ASK_FILE)
            ParseAskPacket(data);
        else if(data[0] == Constants.SEND_FILE)
            ParseFilePacket(data);
        else if(data[0] == Constants.FILE_DONE)
            ParseFileDonePacket(data);
        else if(data[0] == Constants.PLAY)
            ParsePlayPacket(data);
        else if(data[0] == Constants.PAUSE)
            ParsePausePacket(data);
        else if (data[0] == Constants.STOP)
            ParseStopPacket(data);
        else
            Log.e(Constants.Tag, "Invalid Packet");
    }
}
