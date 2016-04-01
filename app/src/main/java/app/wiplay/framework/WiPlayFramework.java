package app.wiplay.framework;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.Socket;
import java.net.SocketAddress;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayClient;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.constants.Constants;
import app.wiplay.filemanager.FileManager;
import app.wiplay.ui.FilePlay;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayFramework {
    private WiPlayServer server;
    private WiPlayClient client;
    private Context context;
    private String file_path;
    private boolean exitThread;
    private String host_name;

    public WiPlayFramework(Context c)
    {
        server = null;
        exitThread = false;
        context = c;
        host_name = null;
    }

    public void Init(boolean isServer, String host_name)
    {
        if(isServer) {
            server = new WiPlayServer(context);
            server.Listen();
        }
        else {
            client = new WiPlayClient(host_name, null);
            client.Send(PacketCreator.CreateAskPacket());
        }

    }

    public void PlayFile(int time)
    {
        Intent i = new Intent(context, FilePlay.class);
        i.putExtra("path", file_path);
        context.startActivity(i);

    }

    public void Pause(int time)
    {
        server.SendData(PacketCreator.CreatePausePacket(time));
    }

    public void Stop()
    {
        server.SendData(PacketCreator.CreateStopPacket());
    }

    public void setFile_path(String path)
    {
      server.setFile_path(path);
    }

    public String getHost_name()
    {
        if(server != null)
            return server.getHostname();
        else if(client != null)
            return client.getHostname();
        else
            return "NO host name set";
    }

    public void cleanUp()
    {
        exitThread = true;
        if(server != null)
        server.cleanUp();
        else if(client != null)
            client.cleanUp();
        else
            Log.i(Constants.Tag, "Do nothing");
        file_path = null;
    }
}
