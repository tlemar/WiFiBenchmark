package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import com.vivo.zhouchen.wifibenchmark.CONSTANTS.PARAMETERS_KEYS;
import com.vivo.zhouchen.wifibenchmark.Models.WifiOpenStressTable;
import com.vivo.zhouchen.wifibenchmark.R;

import vivo.zhouchen.wifiBaseLibrary.netUtils.NetUtils;

import java.util.Map;
import java.util.Queue;

/**
 * Created by vivo on 2015/10/21.
 */
public class ConnectTest extends CommonTest {
    NetUtils netUtils;
    String tag = "connectTest";

    int tries;
    public ConnectTest(Context context, Bundle mParas) {
        super(context, mParas);
        netUtils = new NetUtils(context);
        //errorReasons.clear();
        tries = mParas.getInt(PARAMETERS_KEYS.tries);

        IntentFilter intentFilter = new IntentFilter();
        // this is for switch test;
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mContext.registerReceiver(connectReceiver, intentFilter);

    }

    @Override
    public Map<String, String> checkConditions() {
        if (!netUtils.isWifiConnected()) {
            errorReasons.put(mContext.getString(R.string.error_type_wifi), mContext.getString(R.string.error_code_wifi_not_connect));
        }
        if (tries<=0){
            errorReasons.put(mContext.getString(R.string.error_type_tries_fail), mContext.getString(R.string.error_code_tries_fail));
        }
        return errorReasons;
    }

    @Override
    public Queue<Runnable> getTestActions() {
        return actions;
    }

    @Override
    public boolean stopTestForcely() {

        mContext.unregisterReceiver(connectReceiver);
        return false;
    }

    @Override
    public boolean excuteTest(Queue<Runnable> actions) {
        return false;
    }

    @Override
    public boolean execute(){
        wifiManager.setWifiEnabled(false);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wifiManager.setWifiEnabled(true);
        return false;
    }
    BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(tag,"actions is " + intent.getAction() + tries);
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
                int preWifiState = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE,0);
                WifiOpenStressTable wifiOpenStressTable;
                if (wifiState == WifiManager.WIFI_STATE_ENABLED){
                    if (tries-- >0){
                        wifiOpenStressTable = new WifiOpenStressTable();
                        wifiOpenStressTable.setPhase("close");
                        wifiOpenStressTable.setTime(System.currentTimeMillis()+"");
                        wifiOpenStressTable.save();
                        Log.e(tag,"setfalse");
                        wifiManager.setWifiEnabled(false);

                    }else {
                        return;
                    }
                } else if (wifiState == WifiManager.WIFI_STATE_DISABLED ){
                    wifiOpenStressTable = new WifiOpenStressTable();
                    wifiOpenStressTable.setPhase("open");
                    wifiOpenStressTable.setTime(System.currentTimeMillis()+"");
                    wifiOpenStressTable.save();
                    Log.e(tag,"settrue");
                    wifiManager.setWifiEnabled(true);
                }

            } else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                String detailState = networkInfo.getDetailedState().toString();
                Log.e(tag, "detailState is " + detailState);

                Log.e(tag, "networkInfo  is " + networkInfo.getState());
                Log.e(tag, "detail State   is " + networkInfo.getDetailedState());
            }
        }
    };


    @Override
    public boolean reportResults() {
        return false;
    }
}
