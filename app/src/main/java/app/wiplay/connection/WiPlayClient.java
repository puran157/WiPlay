package app.wiplay.connection;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import app.wiplay.constants.Constants;
import app.wiplay.framework.ThreadPool;


/**
 * Created by pchand on 11/17/2015.
 */
public class WiPlayClient {

    /* for cases when server is going down */
    private WiPlaySocketStruct queue;
    private SocketChannel client;
    private Selector selector;
    private String host;
    private boolean exitThread;

    public WiPlayClient(SocketChannel channel) {
        queue = new WiPlaySocketStruct(this);
        client = channel;
        selector = null;
        host = null;
        exitThread = false;
    }

    public WiPlayClient(String host_) {
        queue = new WiPlaySocketStruct(this);
        client = null;
        host = host_;
        selector = null;
        exitThread = false;
    }

    /* to be called in client mode only */
    public void Init() throws IOException {
        boolean connected = false;
        client = SocketChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress(host, Constants.CONTROL_PORT));
        selector = Selector.open();

        client.register(selector, SelectionKey.OP_CONNECT);

        while (selector.select(5000) > 0) {
            Set keys = selector.selectedKeys();
            Iterator iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                iter.remove();

                SocketChannel channel = (SocketChannel) key.channel();

                if (key.isConnectable()) {
                    connected = true;
                    if (channel.isConnectionPending())
                        channel.finishConnect();

                    client.register(selector, SelectionKey.OP_READ);
                    client.register(selector, SelectionKey.OP_WRITE);
                }
            }
        }

        if (connected) {
            Thread eventEmitter = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!exitThread && !Constants.exitAll) {
                        try {
                            selector.select();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Set keys = selector.selectedKeys();
                        Iterator i = keys.iterator();
                        while (i.hasNext()) {
                            SelectionKey key = (SelectionKey) i.next();
                            i.remove();
                            if (key.isReadable()) {
                                ThreadPool.getInstance().AddReadEvent(this);
                            }

                            if (key.isWritable()) {
                                ThreadPool.getInstance().AddWriteEvent(this);
                            }
                        }
                    }
                }

            });

            eventEmitter.start();
        }
    }

    public void Read(ByteBuffer data) throws IOException {
        client.read(data);
    }

    public void Write(ByteBuffer data) throws IOException {
        client.write(data);
    }

    public void cleanUp() throws IOException {
        exitThread = true;
        queue.cleanUp();
        queue = null;
        client.close();
        client = null;
        selector.close();
        selector = null;
    }
}
