package app.wiplay.framework;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayClient;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlaySlaves {
    private WiPlayClient dataSock;
    //private WiPlayClient controlSock;

    public WiPlaySlaves(String host)
    {
       //TODO:FIX this dataSock = new WiPlayClient(null, host);
        dataSock = new WiPlayClient(null, host, null);
    }

    public void AskFile()
    {
        dataSock.SendData(PacketCreator.CreateAskPacket());
    }

    public void Play(int time)
    {
        dataSock.SendData(PacketCreator.CreatePlayPacket(time));
    }

    public void Pause(int time)
    {
        dataSock.SendData(PacketCreator.CreatePausePacket(time));
    }
}
