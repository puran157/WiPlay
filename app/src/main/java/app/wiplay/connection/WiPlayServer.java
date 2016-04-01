package app.wiplay.connection;

import android.content.Context;
import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;

import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;

/**
 * Created by pchand on 11/12/2015.
 */
public class WiPlayServer extends WiPlaySocket {
    private ArrayList<WiPlayClient> pool;
    private String file_path;
    private Context context;

    public WiPlayServer(Context c)
    {
        pool = new ArrayList<>(Constants.MAX_CLIENTS);
        context = c;
    }

    public void setFile_path(String f)
    {
        file_path = f;
    }

    public void Listen()
    {
        Log.i(Constants.Tag, "Server Listening");
        Thread dataListen = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean exitThread = false;
                while (!exitThread) {
                    if (getServerSocket() != null) {
                        if (pool.size() < Constants.MAX_CLIENTS) {
                            try {
                                Socket clientSock = getServerSocket().accept();
                                pool.add(new WiPlayClient(clientSock, WiPlayServer.this));
                            } catch (Exception e) {

                            }
                        } else {
                            exitThread = true;
                            Log.e(Constants.Tag, "Maximum clients limit reached, stop listening for more connection");
                        }
                    }
                }
            }
        });

        dataListen.start();
    }

    public void SendFile(final WiPlayClient client)
    {
        FileManager fManager = new FileManager(file_path);
        int bytesRead = -1;
        while(bytesRead != 0) {
            int start = fManager.GetOffset();
            byte[] data = fManager.GetChunk(bytesRead);
            client.Send(PacketCreator.CreateFilePacket(start, bytesRead, data));
        }
    }

    public void cleanUp()
    {
        super.cleanUp();
        pool = null;
    }
}
