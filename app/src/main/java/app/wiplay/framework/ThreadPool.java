package app.wiplay.framework;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import app.wiplay.connection.WiPlayClient;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.constants.Constants;

/**
 * Created by pchand on 7/12/2016.
 */
public class ThreadPool {

    ArrayList<Thread> threads;
    Runnable readRunnable;
    Runnable writeRunnable;
    Lock lock;
    Condition readCondition;
    Condition writeCondition;
    ArrayList<WiPlaySocket> readEventList;
    ArrayList<WiPlaySocket> writeEventList;
    static ThreadPool instance;

    private ThreadPool()
    {

    }
    public static ThreadPool getInstance()
    {
        instance = new ThreadPool();
        return instance;
    }

    public void Init()
    {
        lock = new ReentrantLock();
        readCondition = lock.newCondition();
        writeCondition = lock.newCondition();
        readEventList = new ArrayList<>();
        writeEventList = new ArrayList<>();

        readRunnable = new Runnable() {
            @Override
            public void run() {
                while(!Constants.exitAll) {
                    lock.lock();
                    try {
                        readCondition.await();
                        synchronized (readEventList) {
                            if (!readEventList.isEmpty()) {
                                WiPlaySocket socket = readEventList.get(0);
                                //Perform read
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

            }
        };

        writeRunnable = new Runnable() {
            @Override
            public void run() {
                while(!Constants.exitAll) {
                    lock.lock();
                    try {
                        synchronized (writeEventList) {
                        if(writeEventList.isEmpty())
                            writeCondition.await();
                                WiPlaySocket client = writeEventList.get(0);
                                //Perform Write
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }

            }
        };

        threads = new ArrayList<>();

        for(int i = 0; i < Constants.WORKER; ++i)
        {
            Thread thread;
            if(i%2 == 0)
                thread = new Thread(readRunnable);
            else
                thread = new Thread(writeRunnable);
            threads.add(thread);
            thread.start();
        }
    }


    public void AddReadEvent(WiPlaySocket obj)
    {
        readEventList.add(obj);
        readCondition.signal();
    }

    public void AddWriteEvent(WiPlaySocket obj)
    {
        writeEventList.add(obj);
        writeCondition.signal();
    }
}
