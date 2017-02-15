package app.wiplay.connection;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import app.wiplay.constants.Constants;
import app.wiplay.framework.ReadEvent;

/**
 * Created by Puran Chand on 10/19/2015.
 */
public class WiPlaySocket {

    private SocketChannel _socketChannel;
    private SocketAddress _address;

    private WiPlaySocketStruct _dataQueue;

    public WiPlaySocket(String host) {
        _socketChannel = null;
        _address = null;
    }

    public WiPlaySocket(SocketChannel channel)
    {
        _socketChannel = channel;
    }

    public int Init(String host)
    {
        if(_socketChannel == null) {
            _address = new InetSocketAddress(host, Constants.CONTROL_PORT);
            try {
                _socketChannel = SocketChannel.open();
                _socketChannel.configureBlocking(false);
            } catch (Exception e) {
                Log.i(Constants.Tag, "WiplaySocket::Init " + e);
                return -1;
            }
        }

        return 0;
    }


    public int Connect()
    {
        try {
            _socketChannel.connect(_address);
            while(!_socketChannel.finishConnect()); //Lets wait for connect to be success here

        }
        catch (Exception e)
        {
            Log.i(Constants.Tag, "WiplaySocket::Connect "+e);
            return -1;
        }
        return 0;
    }

    public int Read()
    {
        int read = 0;
        try {
            ByteBuffer readBytes = ByteBuffer.allocate(Constants.BUFFER_SIZE);
            read = _socketChannel.read(readBytes);
            _dataQueue.PushReadData(readBytes.array());
        }
        catch(Exception e)
        {
            Log.i(Constants.Tag, "WiplaySocket::Read "+e);
            return -1;
        }
        return read;
    }

    public int Write()
    {
        int write = 0;
        try {
            ByteBuffer writeBytes = ByteBuffer.wrap(_dataQueue.PopWriteData());
            write = _socketChannel.write(writeBytes);

            if(write != writeBytes.capacity())
            {
                Log.w(Constants.Tag, "WiplaySocket::Write Write partial success, adding remaning data back to queue");
                ByteBuffer remaining = ByteBuffer.wrap(writeBytes.array(), write, writeBytes.capacity());
                _dataQueue.PushWriteData(remaining.array());
            }

        }
        catch(Exception e)
        {
            Log.i(Constants.Tag, "WiplaySocket::Write "+e);
            return -1;
        }
        return write;
    }

}
