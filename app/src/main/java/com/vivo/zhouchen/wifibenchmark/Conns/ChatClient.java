package com.vivo.zhouchen.wifibenchmark.Conns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

//import com.vivo.kiyou.iperf.MainActivity;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ChatClient {

    private InetAddress mAddress = null;
    private int PORT;

    private final String TAG = "ChatClient";

    private Thread mSendThread = null;
    private Thread mRecThread = null;
    private Socket mSocket = null;

    private static Context mContext = null;

    public ChatClient(Socket socket) {
        Log.d(TAG, "Creating ChatClient");

        mSocket = socket;

//        mUpdateHandler = (MainActivity.ConnectionHandler) handler;

        mRecThread = new Thread(new ReceivingThread());
        mRecThread.start();
        mSendThread = new Thread(new SendingThread());
        mSendThread.start();
    }


    public ChatClient(Socket socket, Context context){
        Log.d(TAG, "Creating ChatClient");

        mSocket = socket;

        mContext = context;

        mRecThread = new Thread(new ReceivingThread());
        mRecThread.start();
        mSendThread = new Thread(new SendingThread());
        mSendThread.start();
    }

    private Socket getSocket() {
        return mSocket;
    }

    public void tearDown() {
        try {
            if (getSocket() != null) {
                getSocket().close();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "socket closed.");
            }

        } catch (IOException e) {
            Log.e(TAG, "Error when closing socket.");
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

    public void sendMessage(String msg) {
        try {
            Socket socket = getSocket();

            if (socket == null) {
                Log.d(TAG, "Socket is null, wtf?");
            } else if (socket.getOutputStream() == null) {
                Log.d(TAG, "Socket output stream is null, wtf?");
            }

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true);
            out.println(msg);
            out.flush();

        } catch (UnknownHostException e) {
            Log.d(TAG, "Unknown Host", e);
        } catch (IOException e) {
            Log.d(TAG, "I/O Exception", e);
        } catch (Exception e) {
            Log.d(TAG, "Error 3", e);
        }

        Log.d(TAG, "Client sent message: " + msg);
    }

    class SendingThread implements Runnable {

        private BlockingQueue<String> mMessageQueue = null;
        private int QUEUE_CAPADITY = 10;

        public SendingThread() {
            Log.d(TAG, "SendingThread being called.");
            mMessageQueue = new ArrayBlockingQueue<String>(QUEUE_CAPADITY);
        }

        @Override
        public void run() {

            while (true) {
                try {
                    String msg = mMessageQueue.take();
                    Log.d(TAG, "msg: " + msg);
                    sendMessage(msg);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Message sending loop interrupted, exiting.");
                }
            }
        }
    }

    class ReceivingThread implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "ReceivingThread starting");

            BufferedReader input;
            try {
                input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                while (!Thread.currentThread().isInterrupted()){
                    String messageStr = null;
                    messageStr = input.readLine();
                    if (messageStr != null){
                        Log.d(TAG, "Read from the stream: " + messageStr);
                        updateMessages(3, messageStr, true);
                    } else {
                        Log.d(TAG, "The nulls! The nulls!");
                        Log.d(TAG, "The nulls! The nulls!");
                        break;
                    }
                }
                input.close();
            } catch (IOException e) {
                Log.e(TAG, "Server loop error: ", e);
            }
        }

    }
}