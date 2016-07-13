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
    public static String file_path;
    private boolean exitThread;

    public WiPlayFramework(Context c)
    {
        server = null;
        exitThread = false;
        context = c;
    }

    public void Init(boolean isServer)
    {

    }

    public void PlayFile(int time)
    {
        Intent i = new Intent(context, FilePlay.class);
        i.putExtra("path", file_path);
        context.startActivity(i);

    }

    public void Pause(int time)
    {
        server.BroadCast(PacketCreator.CreatePausePacket(time));
    }

    public void Stop()
    {
        server.BroadCast(PacketCreator.CreateStopPacket());
    }

    public void cleanUp()
    {
        file_path = null;
    }
}
