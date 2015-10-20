package app.wiplay.Constants;

import java.net.SocketAddress;
import java.util.Random;

/**
 * Created by pchand on 10/19/2015.
 */
public class Constants {

    public final static int PORT = 1570;
    public final static String Tag="WiPlay";
    public final static int BUFFER_SIZE = 2*2048;
    public final static int HOTSPOT_CHAR_LEN = 12;
    public enum WiPlayEvent {
        CONNECTING,
        CONNECTED,
        CANT_CONNECT,

        REQUEST_FILE,
        STREAM_FILE,

        PLAY_FILE,
    };

    private final static String ALL_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()-";

    public static String GetRandomString()
    {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(HOTSPOT_CHAR_LEN);
        for(int i=0; i < HOTSPOT_CHAR_LEN; ++i)
            sb.append(ALL_CHAR).charAt(rnd.nextInt(ALL_CHAR.length()));
        return sb.toString();
    }
}
