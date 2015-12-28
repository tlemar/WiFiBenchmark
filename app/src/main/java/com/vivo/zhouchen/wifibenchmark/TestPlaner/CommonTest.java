package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.vivo.zhouchen.wifibenchmark.CONSTANTS.PARAMETERS_KEYS;
import com.vivo.zhouchen.wifibenchmark.Conns.ConnClient;
import com.vivo.zhouchen.wifibenchmark.Models.IperfSpeedTable;
import com.vivo.zhouchen.wifibenchmark.R;

import org.litepal.crud.DataSupport;
import vivo.zhouchen.wifiBaseLibrary.iperfService;
import vivo.zhouchen.wifiBaseLibrary.model.*;
import vivo.zhouchen.wifiBaseLibrary.netUtils.NetUtils;

import java.util.Map;
import java.util.Queue;

/**
 * Created by vivo on 2015/10/21.
 */
public class CommonTest implements ITestPlaner {
    WifiManager wifiManager ;
    NetUtils netUtils;
    Context mContext;
    String TAG = "CommonTest";

    String host = "192.168.2.1";
    int port = 50003;

    String currentDBPath = "//data//com.example.myapp01//databases//wifiTest.db";
    String testType = "";


    ConnClient connClient;
    CommonTest(Context context, Bundle mParas){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        netUtils = new NetUtils(context);
        this.mContext = context;
        errorReasons.clear();
        connClient = new ConnClient();

        testType = mContext.getString(mParas.getInt(PARAMETERS_KEYS.item,0));


        //deleteRecords();

    }

    private void deleteRecords() {
        DataSupport.deleteAll(SpeedTable.class);
        DataSupport.deleteAll(ConnInfoTable.class);
        DataSupport.deleteAll(WifiInfoTable.class);
        DataSupport.deleteAll(IperfSpeedTable.class);

        DataSupport.deleteAll(WifiStableTestTable.class);

        DataSupport.deleteAll(NetSpeedTable.class);
        DataSupport.deleteAll(TestEventLogTable.class);
    }
    // intent

    @Override
    public Map<String, String> checkConditions() {
        if (!netUtils.isWifiConnected()){
            errorReasons.put(mContext.getString(R.string.error_type_wifi),mContext.getString(R.string.error_code_wifi_not_connect));
        }
        return errorReasons;
    }

    @Override
    public Queue<Runnable> getTestActions() {
        return null;
    }

    @Override
    public boolean excuteTest(Queue<Runnable> actions) {
        return false;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean reportResults() {



        return false;
    }

    @Override
    public boolean excuateATest() {
        return false;
    }

    @Override
    public boolean stopTestForcely() {
        return false;
    }

}
