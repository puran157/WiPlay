package app.wiplay.connection;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import app.wiplay.constants.Constants;
import app.wiplay.framework.ThreadPool;

/**
 * Created by pchand on 11/12/2015.
 */
public class WiPlayServer {
    public static HashMap<SocketChannel, WiPlayClient> pool;
    private Context context;
    private ServerSocketChannel server;
    private int worker_count;
    private Selector event;
    public WiPlayServer(Context c)
    {
        context = c;
        pool = null;
        server = null;
        event = null;
        worker_count = 0;
    }

    public void Init() throws IOException {
        if(pool == null)
            pool = new HashMap<>(Constants.MAX_CLIENTS);
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        String wifi_addr = getWifiApIpAddress();
        if(wifi_addr != null) {
            server.socket().bind(new InetSocketAddress(wifi_addr, Constants.CONTROL_PORT));
        }
        event = Selector.open();
        server.register(event, SelectionKey.OP_ACCEPT);
        EventHandler();
    }

    public String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            Log.d(Constants.Tag, inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(Constants.Tag, ex.toString());
        }
        return null;
    }

    private void EventHandler()
    {
        Log.i(Constants.Tag, "Server Listening");
        Thread eventEmitter = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean exitThread = false;
                while (!exitThread && !Constants.exitAll) {
                    if (pool.size() < Constants.MAX_CLIENTS) {
                        try {
                            event.select();
                            Set keys = event.selectedKeys();
                            Iterator i = keys.iterator();
                            while (i.hasNext()) {
                                SelectionKey key = (SelectionKey) i.next();
                                i.remove();

                                if (key.isAcceptable()) {
                                    SocketChannel client = server.accept();
                                    client.configureBlocking(false);
                                    client.register(event, SelectionKey.OP_READ);
                                    client.register(event, SelectionKey.OP_WRITE);
                                    WiPlayClient obj = new WiPlayClient(client);
                                    pool.put(client, obj);
                                }

                                if(key.isReadable())
                                {
                                    SocketChannel client = (SocketChannel) key.channel();
                                    WiPlayClient obj = pool.get(client);
                                    /* add read event */
                                    ThreadPool.getInstance().AddReadEvent(obj);
                                }

                                if(key.isWritable())
                                {
                                    SocketChannel client = (SocketChannel) key.channel();
                                    WiPlayClient obj = pool.get(client);
                                    /* add read event */
                                    ThreadPool.getInstance().AddWriteEvent(obj);
                                }
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        exitThread = true;
                        Log.e(Constants.Tag, "Maximum clients limit reached, stop listening for more connection");
                    }
                }
            }
        });

        eventEmitter.start();
    }

    public void BroadCast(final byte[] data)
    {
        final ByteBuffer buffer = ByteBuffer.wrap(data);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Set<Socket> s = new HashSet<>();
                Iterator i = s.iterator();

                while(i.hasNext())
                {
                    pool.get(i.next()).Write(buffer);
                }
            }
        });
        t.start();
    }

    public void cleanUp()
    {
        //super.cleanUp();
        pool = null;
    }
}
