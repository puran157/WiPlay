package app.wiplay.framework;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;
import app.wiplay.ui.FilePlay;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayMaster {
    private WiPlayServer dataServer;
    private Context context;
    //private WiPlayServer controlServer;
    public static String file_path;
    private boolean exitThread;

    public WiPlayMaster(Context c)
    {
        dataServer = new WiPlayServer(this);
        //controlServer = new WiPlayServer();
        exitThread = false;
        context = c;

        Thread dataListen = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(Constants.Tag, "Server Listening");
                while(!exitThread)
                    dataServer.Listen();
            }
        });

        /*Thread controlListen = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exitThread)
                    controlServer.Listen();
            }
        });*/

        dataListen.start();
        //controlListen.start();

    }

    public void SendFile(WiPlaySocket dataSocket)
    {
        FileManager fileManager = new FileManager(file_path);
        fileManager.InitialiseReader();
        int bytesRead = -1;
        while(bytesRead != 0) {
            int start = fileManager.GetOffset();
            dataSocket.SendData(PacketCreator.CreateFilePacket(start, bytesRead, fileManager.GetChunk(bytesRead)));
        }
        dataSocket.SendData(PacketCreator.CreateFileDonePacket());
        fileManager.DeInit();
    }

    public String getHostName()
    {
        return dataServer.gethostName();
    }

    public void PlayFile(int time)
    {
        dataServer.SendData(PacketCreator.CreatePlayPacket(time));
        Intent i = new Intent(context, FilePlay.class);
        i.putExtra("path", file_path);
        context.startActivity(i);
    }

    public void Pause(int time)
    {
        dataServer.SendData(PacketCreator.CreatePausePacket(time));
    }

    public void Stop()
    {
        dataServer.SendData(PacketCreator.CreateStopPacket());
    }

    public void cleanUp()
    {
        exitThread = true;
        dataServer.cleanUp();
        //controlServer.cleanUp();
        file_path = null;
    }
}
