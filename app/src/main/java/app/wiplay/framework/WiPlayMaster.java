package app.wiplay.framework;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.filemanager.FileManager;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayMaster {
    private WiPlayServer dataServer;
    private WiPlayServer controlServer;
    private String file_path;
    private boolean exitThread;

    public WiPlayMaster(String file)
    {
        dataServer = new WiPlayServer();
        controlServer = new WiPlayServer();
        exitThread = false;
        file_path = file;

        Thread dataListen = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exitThread)
                    dataServer.Listen();
            }
        });

        Thread controlListen = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!exitThread)
                    controlServer.Listen();
            }
        });

        dataListen.start();
        controlListen.start();

    }

    public String getHostName()
    {
        return dataServer.gethostName();
    }

    public void SendFile(WiPlaySocket dataSocket, WiPlaySocket controlSocket)
    {
        FileManager fileManager = new FileManager(file_path);
        fileManager.Initialise();
        int bytesRead = -1;
        while(bytesRead != 0) {
            int start = fileManager.GetOffset();
            dataSocket.SendData(PacketCreator.CreateFilePacket(start, bytesRead, fileManager.GetChunk(bytesRead)));
        }
        controlSocket.SendData(PacketCreator.CreateFileDonePacket());
        fileManager.DeInit();
    }

    public void PlayFile(int time)
    {
        controlServer.SendData(PacketCreator.CreatePlayPacket(time));
    }

    public void Pause(int time)
    {
        controlServer.SendData(PacketCreator.CreatePausePacket(time));
    }

    public void Stop()
    {
        controlServer.SendData(PacketCreator.CreateStopPacket());
    }

    public void cleanUp()
    {
        exitThread = true;
        dataServer.cleanUp();
        controlServer.cleanUp();
        file_path = null;
    }
}
