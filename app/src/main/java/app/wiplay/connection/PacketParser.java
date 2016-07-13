package app.wiplay.connection;

import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;
import app.wiplay.framework.WiPlayFramework;
import app.wiplay.ui.FilePlay;
import app.wiplay.ui.MainActivity;

/**
 * Created by pchand on 10/22/2015.
 * TODO: It should be singleton
 */
public class PacketParser {

    private static FilePlay video_instance;

    private static void ParseAskPacket(byte[] data, final WiPlayClient client)
    {
        /* start sending the file to this client */
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int bytesRead = 0;
                FileManager f = new FileManager(WiPlayFramework.file_path);
                f.InitialiseReader();
                while(!Constants.exitAll)
                {
                    byte[] data = f.GetChunk(bytesRead);
                    ByteBuffer buffer = ByteBuffer.wrap(data);
                    if(bytesRead < 0)
                        break;
                    try {
                        client.Write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private static void ParseFilePacket(byte[] data, WiPlayClient client)
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

        /* if we've got enough file. lets broadcast play command and start playing video */
        if(offset >= Constants.THRESHOLD_BEFORE_PLAY)
        {

        }
    }

    private static void ParseFileDonePacket(byte[] data, WiPlayClient client)
    {
        /* we can drop the data plain connection  in case of clients */

        return;
    }

    private static void ParsePlayPacket(byte[] data, WiPlayClient client)
    {
        /* start playing the video */

        int time =      (data[1]<<24)&0xff000000|
                        (data[2]<<16)&0x00ff0000|
                        (data[3]<< 8)&0x0000ff00|
                        (data[4]<< 0)&0x000000ff;

        if(video_instance != null)
            video_instance.PlayVideo(time);

    }

    private static void ParsePausePacket(byte[] data, WiPlayClient client)
    {
        /* Pause the video and set the head to the time stamp received */
        int time =      (data[1]<<24)&0xff000000|
                (data[2]<<16)&0x00ff0000|
                (data[3]<< 8)&0x0000ff00|
                (data[4]<< 0)&0x000000ff;
        if(video_instance != null)
            video_instance.PauseVideo(time);
    }

    private static void ParseStopPacket(byte[] data, WiPlayClient client)
    {
        /* Stop the video, delete the tmp file, close control plain connection also */
        if(video_instance != null) {
            video_instance.StopVideo();
            video_instance = null;
        }
        //client.cleanUP();
    }

    public static void ParsePacket(byte[] data, WiPlayClient client)
    {
        if(data == null)
            return;

        if(data[0] == Constants.ASK_FILE)
            ParseAskPacket(data, client);
        else if(data[0] == Constants.SEND_FILE)
            ParseFilePacket(data, client);
        else if(data[0] == Constants.FILE_DONE)
            ParseFileDonePacket(data, client);
        else if(data[0] == Constants.PLAY)
            ParsePlayPacket(data, client);
        else if(data[0] == Constants.PAUSE)
            ParsePausePacket(data, client);
        else if (data[0] == Constants.STOP)
            ParseStopPacket(data, client);
        else
            Log.e(Constants.Tag, "Invalid Packet");
    }

    public static void SetFilePlay(FilePlay filePlay)
    {
        video_instance = filePlay;
    }
}
