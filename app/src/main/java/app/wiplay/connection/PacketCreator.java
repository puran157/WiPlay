package app.wiplay.connection;

import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 10/22/2015.
 * It will be a singleton class
 */
public class PacketCreator {

    public static byte[] CreateAskPacket()
    {
        byte packet[] = new byte[1];
        packet[0] = Constants.ASK_FILE;
        return packet;
    }

    public static byte[] CreateFilePacket(int startOff, int length, byte[] data)
    {
        byte control_p;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] length_p = new byte[4];
        byte[] offset_p = new byte[4];
        control_p = Constants.SEND_FILE;
        for(int i =0; i < 4; ++i)
        {
            length_p[i] = (byte)(length >>> (i*8));
            offset_p[i] = (byte)(startOff >>> (i*8));
        }
        try {
            out.write(control_p);
            out.write(length_p);
            out.write(offset_p);
            out.write(data);
        } catch(IOException e){}
        return out.toByteArray();
    }

    public static byte[] CreateFileDonePacket()
    {
        byte[] packet = new byte[1];
        packet[0] = Constants.FILE_DONE;
        return packet;
    }

    /* time: time in seconds from the start of the file */
    public static byte[] CreatePlayPacket(int time)
    {
        byte control_p = Constants.PLAY;
        byte[] time_p = new byte[4];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(int i=0; i < 4; ++i)
            time_p[i] = (byte)(time >>> (i*8));
        try {
            out.write(control_p);
            out.write(time_p);
        }catch(IOException e){}
        return out.toByteArray();
    }

    public static byte[] CreatePausePacket(int time)
    {
        byte control_p = Constants.PAUSE;
        byte[] time_p = new byte[4];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(int i=0; i < 4; ++i)
            time_p[i] = (byte)(time >>> (i*8));
        try {
            out.write(control_p);
            out.write(time_p);
        }catch(IOException e){}
        return out.toByteArray();
    }

    public static byte[] CreateStopPacket()
    {
        byte control_p[] = new byte[1];
        control_p[0] = Constants.STOP;
        return control_p;
    }

    public static void AllocateBuffer(int type, byte[] buffer)
    {
        if(type == Constants.ASK_FILE)
            buffer = new byte[1];
        else if(type == Constants.SEND_FILE)
            buffer = new byte[Constants.BUFFER_SIZE + 9];
        else if(type == Constants.FILE_DONE)
            buffer = new byte[1];
        else if(type == Constants.PLAY || type == Constants.PAUSE)
            buffer = new byte[5];
        else if (type == Constants.STOP)
            buffer = new byte[1];
        else {
            Log.e(Constants.Tag, "Invalid Packet");
            return;
        }
        buffer[0] = (byte)type;
    }
}
