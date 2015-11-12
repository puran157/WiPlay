package app.wiplay.constants;

import java.util.Random;

/**
 * Created by pchand on 10/19/2015.
 * Constants
 */
public class Constants {

    public final static int CONTROL_PORT = 1570;
    public final static int DATA_PORT = 2570;
    public final static String Tag="WiPlay";
    public final static int BUFFER_SIZE = 2*2048;
    public final static int HOTSPOT_CHAR_LEN = 12;
    public enum WiPlayEvent {
        CONNECTING,
        CONNECTED,
        CANT_CONNECT
    };
    public final static int ASK_FILE = 0,
                            SEND_FILE = 1,
                            FILE_DONE = 2,
                            PLAY = 3,
                            PAUSE = 4,
                            STOP = 5;

    private final static String ALL_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()-";

    public final static String FILE_TYPE[]= {"mp3","mp4", "avi", "3gp", "m4a", "mkv" };
    public final static String action = "ACTION";
    public final static String ACTION[] = {"FILE_CHOOSE", "QR_SCAN"};

    public static String GetRandomString(int len)
    {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0; i < len; ++i)
            sb.append(ALL_CHAR.charAt(rnd.nextInt(ALL_CHAR.length())));
        return sb.toString();
    }
}
