package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.vivo.zhouchen.wifibenchmark.CONSTANTS.PARAMETERS_KEYS;
import com.vivo.zhouchen.wifibenchmark.Models.WifiOpenStressTable;

/**
 * Created by vivo on 2015/10/30.
 */
public class SwitchTest extends CommonTest{
    String tag = "SwitchTest ";
    int tries = 0;
    public SwitchTest(Context context, Bundle mParas) {
        super(context, mParas);


        tries = mParas.getInt(PARAMETERS_KEYS.tries);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        mContext.registerReceiver(switchReceiver,intentFilter);

    }

    @Override
    public boolean execute() {

        wifiManager.setWifiEnabled(false);

        return super.execute();

    }

    @Override
    public boolean stopTestForcely() {

        mContext.unregisterReceiver(switchReceiver);
        return false;
    }


    BroadcastReceiver switchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
                int preWifiState = intent.getIntExtra(WifiManager.EXTRA_PREVIOUS_WIFI_STATE,0);
                WifiOpenStressTable wifiOpenStressTable;
                if (wifiState == WifiManager.WIFI_STATE_ENABLED){
                    if (tries-- > 0){
                        wifiOpenStressTable = new WifiOpenStressTable();
                        wifiOpenStressTable.setPhase("close");
                        wifiOpenStressTable.setTime(System.currentTimeMillis()+"");
                        wifiOpenStressTable.save();
                        Log.e(tag, "setfalse");
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
            }
        }
    };
}
