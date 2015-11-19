package app.wiplay.framework;

import android.util.Log;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayMaster {
    private WiPlayServer dataServer;
    //private WiPlayServer controlServer;
    public static String file_path;
    private boolean exitThread;

    public WiPlayMaster()
    {
        dataServer = new WiPlayServer(this);
        //controlServer = new WiPlayServer();
        exitThread = false;

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
