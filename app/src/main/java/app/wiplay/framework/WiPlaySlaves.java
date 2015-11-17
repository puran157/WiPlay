package app.wiplay.framework;

import app.wiplay.connection.PacketCreator;
import app.wiplay.connection.WiPlayClient;

/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlaySlaves {
    private WiPlayClient dataSock;
    private WiPlayClient controlSock;

    public WiPlaySlaves(String host)
    {
        dataSock = new WiPlayClient(null, host);
        controlSock = new WiPlayClient(null, host);
    }

    public void AskFile()
    {
        controlSock.SendData(PacketCreator.CreateAskPacket());
    }

    public void Play(int time)
    {
        controlSock.SendData(PacketCreator.CreatePlayPacket(time));
    }

    public void Pause(int time)
    {
        controlSock.SendData(PacketCreator.CreatePausePacket(time));
    }
}
