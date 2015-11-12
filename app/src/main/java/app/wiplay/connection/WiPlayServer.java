package app.wiplay.connection;

import java.util.HashMap;

import app.wiplay.filemanager.FileManager;

/**
 * Created by pchand on 11/12/2015.
 */
public class WiPlayServer {
    private String file_path;
    private WiPlaySocket dataSocket;
    private WiPlaySocket controlSocket;

    public WiPlayServer(String file)
    {
        file_path = file;
        dataSocket = new WiPlaySocket(this);
        controlSocket = new WiPlaySocket(this);
        Thread createSocket = new Thread(new Runnable() {
            @Override
            public void run() {
                dataSocket.CreateSocket(false); /* Creates Data server */
                controlSocket.CreateSocket(true); /* Creates Control server */
            }
        });

        createSocket.run();

    }

    public String gethostName()
    {
        return dataSocket.getHostname(); /* return anyones hostname string */
    }

    public void GoLive()
    {
        Thread dataListen = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                    dataSocket.Listen();
            }
        });

        Thread controlListen = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                    controlSocket.Listen();
            }
        });

        dataListen.run();
        controlListen.run();
    }

    public void StartSharingFile()
    {
        Thread startSharingFile = new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager fileManager = new FileManager(file_path);
                fileManager.Initialise();
                int bytesRead = -1;
                while(bytesRead != 0)
                    fileManager.SendChunk(bytesRead);
                fileManager.DeInit();
            }
        });

        startSharingFile.run();
    }
}
