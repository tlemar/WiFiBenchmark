package com.vivo.zhouchen.wifibenchmark.Service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vivo.zhouchen.wifibenchmark.Conns.ConnClient;
import com.vivo.zhouchen.wifibenchmark.Conns.ConnServer;


/**
 * Created by Administrator on 2015/11/3.
 */
public class ConnIntentService extends IntentService {

    // connecte message

    private String localHost = null;
    private String localprot = null;

    private String pcHost = null;
    private String pcLocal = null;

    private String peerHost = null;
    private String peerPort = null;

    // connecte state
    private static ConnServer connServer = null;
    private static ConnClient connClient_pc = null;
    private static ConnClient connClient_peer = null;

    private boolean isController = false;

    public ConnIntentService(){
        super("ConnIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // initial message for connect
        initConnMessage(intent);
    }

    public void initConnMessage(Intent intent){
        localprot = intent.getStringExtra("LocalPort");
        pcHost = intent.getStringExtra("PcHost");
        pcLocal = intent.getStringExtra("PcLocal");
        peerHost = intent.getStringExtra("PeerHost");
        peerPort = intent.getStringExtra("PeerPort");
        isController = intent.getBooleanExtra("IsController", false);
    }

    public boolean initConnectPc(){

        return false;
    }

    public boolean initConnectServer(){
        return false;
    }

    public boolean initConnectPeer(){
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean sendMessage(String msg){
        return false;
    }

    public boolean updateMessage(String msg, int state){
        return false;
    }

    BroadcastReceiver sendBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    BroadcastReceiver recvBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
