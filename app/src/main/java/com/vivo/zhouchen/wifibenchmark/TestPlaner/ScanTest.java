package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.vivo.zhouchen.wifibenchmark.R;

import org.litepal.crud.DataSupport;
import vivo.zhouchen.wifiBaseLibrary.model.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by vivo on 2015/10/21.
 */
public class ScanTest extends CommonTest {

    Bundle mParas;
    WifiManager wifiManager;
    String tag = "scanTest";
    int scanCount = 0;
    CountDownTimer wifiScan;


    public ScanTest(Context context, Bundle mParas) {
        super(context,mParas);
        errorReasons.clear();

        this.mParas = mParas;
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        mContext.registerReceiver(scanReceiver, intentFilter);
    }


    public Map<String, String> checkConditions() {
        if (!netUtils.isWifiConnected()) {
            errorReasons.put(mContext.getString(R.string.error_type_wifi), mContext.getString(R.string.error_code_wifi_not_connect));
        }
        return errorReasons;
    }


    @Override
    public Queue<Runnable> getTestActions() {
        return actions;
    }

    @Override
    public boolean excuteTest(Queue<Runnable> actions) {
//        Log.e(tag,"")
        wifiScan = new WifiTimeScan(3000 * 1000, 1000).start();

        return true;
    }

    @Override
    public boolean execute(){
//        new WifiTimeScan(300 * 1000, 3*1000).start();
        wifiScan = new WifiTimeScan(300 * 1000, 3000).start();
        return  true;
    }

    @Override
    public boolean stopTestForcely() {
        super.stopTestForcely();
        if(wifiScan!= null){
            wifiScan.cancel();
            Log.e(tag, "wifiscan cancelled");

        }
            return false;
    }


    class WifiTimeScan extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public WifiTimeScan(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            wifiManager.startScan();
        }

        @Override
        public void onFinish() {
            reportResults();
        }
    }

    @Override
    public boolean reportResults() {





        return false;
    }

    BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(tag, " action received is " + intent.getAction());
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
                String time = timeFormat.format(new java.util.Date());
                String date = dateFormat.format(new java.util.Date());
                WifiInfo crtWifi = wifiManager.getConnectionInfo();
                Toast.makeText(mContext, "ScanResult received " + scanCount++, Toast.LENGTH_SHORT).show();
                List<ScanResult> wifiScanResults = wifiManager.getScanResults();
                for (ScanResult result : wifiScanResults) {
                    // 相当于给每个扫描结果 单独创建一个对象，然后保存。
                    WifiInfoTable wifiTable = new WifiInfoTable();
                    wifiTable.setSysTime(time);
                    wifiTable.setSysDate(date);
                    wifiTable.setSsid(result.SSID);
                    wifiTable.setBssid(result.BSSID);
                    wifiTable.setRssi(result.level);
                    wifiTable.setCapbility(result.capabilities);
                    wifiTable.setFrequency(result.frequency);
                    // 这个地方不能使用 == 号
                    // 使用CrtWifi来标记当前所连接的wifi
                    if (result.BSSID.equals(crtWifi.getBSSID())) {
                        wifiTable.setIsCrtWifi(1);
                    } else {
                        wifiTable.setIsCrtWifi(0);
                    }
                    wifiTable.saveThrows();
                }
            }
        }
    };
}
