package com.vivo.zhouchen.wifibenchmark.Conns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//import com.vivo.kiyou.iperf.MainActivity;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2015/10/26.
 */

public class ConnServer {

    private Socket mSocket = null;
    private ChatClient mChatClient = null;
    private ChatServer mChatServer = null;
    private int mPort = -1;

    private static final String TAG = "ConnServer";

    private static Context mContext = null;

    public ConnServer(Context mContext){
        this.mContext = mContext;
    }

    public boolean start(int port) {

        mChatServer = new ChatServer(mContext, port);

        return true;
    }

    public boolean stop() {
        if (mChatClient != null) {
            mChatClient.tearDown();
            updateMessages(1, "socket connection stoped.", true);
            mChatClient = null;
        }
        if (mChatServer != null) {
            mChatServer.tearDown();
            updateMessages(1, "server socket closed.", true);
            mChatServer = null;
        }
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

        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        intent.setAction("Conn");
        if (mContext != null)
            mContext.sendBroadcast(intent);
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
        updateMessages(1, "socket create successd, host: " + mSocket.getInetAddress() + " port: " + mSocket.getPort(), true);
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void setLocalPort(int port) {
        mPort = port;
    }

    public int getLocalPort() {
        return mPort;
    }

    private class ChatServer {
        ServerSocket mServerSocket = null;
        Thread mThread = null;

        public ChatServer(Context mContext, int port) {
            mThread = new Thread(new ServerThread(port));
            mThread.start();
        }

        public void tearDown() {
            mThread.interrupt();
            try {
                mServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error when closing server socket.");
            }
        }

        class ServerThread implements Runnable {

            int localPort;

            ServerThread(int port){
                localPort = port;
            }

            @Override
            public void run() {
                try {
                    // ServerSocket 把参数端口设为0，表示由操作系统来为服务器分配一个任意可用的端口
                    mServerSocket = new ServerSocket(localPort);
                    setLocalPort(mServerSocket.getLocalPort()); // 确定端口
                    updateMessages(1, "server started: host: " + mServerSocket.getLocalSocketAddress().toString() + " port: " + mServerSocket.getLocalPort(), true);

                    // 等待连接
                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(TAG, "ServerSocket Created, awaiting connction");

                        setSocket(mServerSocket.accept());
                        Log.d(TAG, "Connected test");

                        if (mSocket != null)
                            mChatClient = new ChatClient(mSocket, mContext);
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Error creating ServerSocket: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

}