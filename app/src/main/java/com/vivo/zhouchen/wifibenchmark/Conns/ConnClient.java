package com.vivo.zhouchen.wifibenchmark.Conns;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//import com.vivo.kiyou.iperf.MainActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2015/10/20.
 */
public class ConnClient  {

    private Socket mSocket = null;
    private ChatClient mChatClient = null;

    private static final String TAG = "ConnClient";

//    private MainActivity.ConnectionHandler mUpdateHandler = null;

    private boolean isClosed = false;
    private static String HOST = "";
    private static int PORT = 0;

    public ConnClient() {
//        mUpdateHandler = (MainActivity.ConnectionHandler) handler;
    }

    public boolean start(String host, int port) {


/*            InetAddress address = InetAddress.getByName(host);
            mSocket = new Socket(address, port);*/
        HOST = host;
        PORT = port;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName(HOST);
                    Log.d("kiyou", HOST+"  " + PORT);
                    setSocket(new Socket(address, PORT));
                } catch (UnknownHostException e) {
                    setSocket(null);
                    e.printStackTrace();
                } catch (IOException e) {
                    setSocket(null);
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            Thread.sleep(2000);

            if (mSocket == null) {
                updateMessages(1, "socket connected time out.", true);
                thread.interrupt();
                //thread.join();
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateMessages(1, "socket create successd, host: " + host + " port: " + port, true);
        if (mSocket != null)
            mChatClient = new ChatClient(mSocket);

        return true;
    }

    public boolean isClosed(){
        return isClosed;
    }

    public boolean stop() {
        if (mChatClient != null) {
            mChatClient.tearDown();
            updateMessages(1, "socket connection stoped.", true);
            mChatClient = null;
        }
        isClosed = true;
        return true;
    }

    public void sendMessage(String msg) {
        if (mChatClient != null) {
            mChatClient.sendMessage(msg);
        }
    }

    public synchronized void updateMessages(int tag, String msg, boolean local) {
        Log.d(TAG, "updateMessage being called.");

        // 1.链接状态 2.发送的数据 3.收到的数据

        Bundle messgeBundle = new Bundle();
        messgeBundle.putString("msg", msg);
        Message message = new Message();
        message.what = tag;
        message.setData(messgeBundle);
//        mUpdateHandler.sendMessage(message);
    }

    private synchronized void setSocket(Socket socket) {
        Log.d(TAG, "setSocket being called.");

        if (socket == null) {
            Log.d(TAG, "setting a null socket.");
        }

        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    // TODO(alexlucas): Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        mSocket = socket;
    }

    private Socket getSocket() {
        return mSocket;
    }

}
