package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.*;
import android.content.res.Resources;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.os.*;
import android.util.Log;
import android.widget.Toast;


import com.vivo.zhouchen.wifibenchmark.CONSTANTS.PARAMETERS_KEYS;
import com.vivo.zhouchen.wifibenchmark.Conns.ConnClient;
import com.vivo.zhouchen.wifibenchmark.R;
import com.vivo.zhouchen.wifibenchmark.Service.IperfIntentService;

import vivo.zhouchen.wifiBaseLibrary.netUtils.NetUtils;

import java.io.*;
import java.lang.Process;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Queue;


/**
 * Created by vivo on 2015/10/21.
 */
public class IperfTest extends CommonTest {
    String tag = "IperfTest";
    String TAG = "IperfTest";


    final String iperfPath = "/data/data/com.example.myapp01/iperf";

    final String ACTION_IPERF_TERMINTATED = "iperf.terminated";
    final String KEY_IPERF_STATE = "iperf.state";

    Context mContext;
    NetUtils netUtils;

    private Intent iperfIntent = null;
    private Intent iperfServiceIntent = null;

    String host = null;
    String iperfPort = "5001";
    String ctrlPort = null;
    String durationTime = null;
    String windowSize = null;
    int isController = 1;

    String iperfCmd = null;
    String iperfCmd1 = null;

    String peerHost = null;
    String peerPort = null;

    private static int execute_time = 0;
    private static String execute_type = null;

    // iperf manager

    ServiceConnection iperfServiceConnection = null;
    ConnClient connClientPc;
    ConnClient connClientPeer;

    static boolean isReady;
    static boolean isConnected = false;
    Bundle paraBundles;

    SharedPreferences sharedPreferences;


    static int iperfState;


//    public IperfTest(Context context){
//        Log.e(tag, "IperfTest initialization");
////        sharedPreferences = mContext.getSharedPreferences().getSharedPreferences();
//    }


    public IperfTest(Context context, Bundle bundle) {
        super(context, bundle);

        Log.d("kiyou_iperf_test", "iperfTest");

//        this(context);
        mContext = context;
        netUtils = new NetUtils(context);
        errorReasons.clear();

        paraBundles = bundle;
        //retrive the parameters
        ctrlPort = paraBundles.getString(PARAMETERS_KEYS.ctrlPort);
        iperfPort = paraBundles.getString(PARAMETERS_KEYS.iperfPort);
        host = paraBundles.getString(PARAMETERS_KEYS.ipAddrRemote);
        durationTime = paraBundles.getString(PARAMETERS_KEYS.durationTime);
        windowSize = paraBundles.getString(PARAMETERS_KEYS.windowSize);

        peerHost = paraBundles.getString(PARAMETERS_KEYS.peerHost);
        peerPort = paraBundles.getString(PARAMETERS_KEYS.peerPort);
        isController = paraBundles.getInt("controller", 1);

        execute_time = paraBundles.getInt("execute_time");
        execute_type = paraBundles.getString("execute_type");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_IPERF_TERMINTATED);
        mContext.registerReceiver(iperfStateReceiver, intentFilter);
    }

    @Override
    public Map<String, String> checkConditions() {
        Log.d("kiyou_iperf_test", "checkConditions");
        if (!checkIperfCmd()) {
            Log.e(tag, "checkIperfCmd :" + iperfCmd);
            errorReasons.put(mContext.getString(R.string.error_type_iperf_cmd), mContext.getString(R.string.error_code_iperf_cmd_error));
        }

        if (!netUtils.isWifiConnected()) {
            errorReasons.put(mContext.getString(R.string.error_type_wifi), mContext.getString(R.string.error_code_wifi_not_connect));
        }
        if (!initIperf()) {
            errorReasons.put(mContext.getString(R.string.error_type_iperf_init), mContext.getString(R.string.error_code_iperf_init_fail));
        }
        if (!initPcConnection()) {
            errorReasons.put(mContext.getString(R.string.error_type_connect), mContext.getString(R.string.error_code_connect_fail));
            connClientPc.sendMessage("STOP_IPERF");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connClientPc.sendMessage("STOP_SOCKET");
            Log.e(tag, "init iperf  error");
        }
        Log.e(tag, "checkIperfCmd : 1" + iperfCmd);

        if (paraBundles.getInt(PARAMETERS_KEYS.item) == R.string.iperf_dul_download_key ||
                paraBundles.getInt(PARAMETERS_KEYS.item) == R.string.iperf_dul_upload_key
                ) {
            Log.e(tag, "test1");

            if (!initPeerConnection()) {
                errorReasons.put(mContext.getString(R.string.error_type_peer_connect), mContext.getString(R.string.error_code_peer_conn_fail));
            }
        }
        Log.e(tag, "test2");

        return errorReasons;
    }

    @Override
    public Queue<Runnable> getTestActions() {
        return actions;
    }

    @Override
    public boolean excuteTest(Queue<Runnable> actions) {
        return false;
    }

    @Override
    public boolean execute() {
        Log.d("kiyou", "execute");
        excuateATest();
        return false;
    }

    @Override
    public boolean stopTestForcely() {

        mContext.unregisterReceiver(iperfStateReceiver);
        if (connClientPc != null) {
            connClientPc.sendMessage("STOP_SOCKET");
            connClientPc = null;
        }
        stopIperf();
        return true;
    }

    @Override
    public boolean reportResults() {


        return false;
    }


    @Override
    public boolean excuateATest() {

        switch (paraBundles.getInt(PARAMETERS_KEYS.item)) {
            case R.string.iperf_uni_upload_key:

                connClientPc.sendMessage("Config: server Cmd: " + "-s -i 1 -p " + iperfPort);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startIperf();

                break;
            case R.string.iperf_uni_download_key:
                startIperf();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String iperfCommand_send = "Config: server Cmd: " + "-c IP -p " + iperfPort + " -i 1 " + " -w " + windowSize + "M -t " + durationTime;
                if (wifiManager.isWifiEnabled()) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    String ip = (ipAddress & 0xFF) + "." +
                            ((ipAddress >> 8) & 0xFF) + "." +
                            ((ipAddress >> 16) & 0xFF) + "." +
                            (ipAddress >> 24 & 0xFF);
                    Log.d(TAG, "my ip is : " + ip);
                    iperfCommand_send = iperfCommand_send.replace("IP", ip);
                    connClientPc.sendMessage(iperfCommand_send);
                }

                break;
            case R.string.bt_call_upload_key:
                break;
            case R.string.bt_play_upload_key:
//                connClientPc.sendMessage("Config: server Cmd: " +"-s -i 1 -p " + iperfPort);
                break;

            case R.string.bt_call_download_key:
            case R.string.bt_play_download_key:
                break;
            case R.string.iperf_dul_upload_key:
                if (isController == 0)
                    break;
                connClientPc.sendMessage("Config: server Cmd: " + "-s -i 1 -p " + iperfPort);
                String port2 = Integer.toString(Integer.parseInt(iperfPort) + 1);
                connClientPc.sendMessage("Config: server Cmd: " + "-s -i 1 -p " + port2);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startIperf();
                break;
            case R.string.iperf_dul_download_key:
                if (isController == 0)
                    break;

                startIperf();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                iperfCommand_send = "Config: server Cmd: " + "-c IP -p " + iperfPort + " -i 1 " + " -w " + windowSize + "M -t " + durationTime;
                if (wifiManager.isWifiEnabled()) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    String ip = (ipAddress & 0xFF) + "." +
                            ((ipAddress >> 8) & 0xFF) + "." +
                            ((ipAddress >> 16) & 0xFF) + "." +
                            (ipAddress >> 24 & 0xFF);
                    Log.d(TAG, "my ip is : " + ip);
                    String iperfCommand_send1 = iperfCommand_send.replace("IP", ip);

                    connClientPc.sendMessage(iperfCommand_send1);
                }
                String port3 = Integer.toString(Integer.parseInt(iperfPort) + 1);
                String iperfCommand_send2 = "Config: server Cmd: " + "-c " + peerHost + " -p " + port3 +
                        " -i 1 " + " -w " + windowSize + "M -t " + durationTime;
                connClientPc.sendMessage(iperfCommand_send2);
                break;
            default:
                break;
        }

        return true;
    }

    public static synchronized void setIperfState(int state) {
        iperfState = state;
    }

    BroadcastReceiver iperfStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(tag, " action received " + intent.getAction());
            if (intent.getAction().equals(ACTION_IPERF_TERMINTATED)) {
                setIperfState(intent.getIntExtra(KEY_IPERF_STATE, 0));

                if (iperfState == 2) {
                    stopIperf();
                    setIperfState(0);
                }
            }
        }
    };

    private void startIperf() {
        Intent startIntent = new Intent(mContext, IperfIntentService.class);
        startIntent.putExtra(mContext.getString(R.string.controller_iperf_switch_key), R.string.controller_iperf_start);
        startIntent.putExtra("execute_time", execute_time);
        startIntent.putExtra("execute_type", execute_type);
        startIntent.putExtra(mContext.getString(R.string.controller_iperf_cmd_key), iperfCmd);
        startIntent.putExtra(PARAMETERS_KEYS.item, paraBundles.getInt(PARAMETERS_KEYS.item));
        mContext.startService(startIntent);
    }

    private void stopIperf() {

        Log.d("kiyou_stop_iperf", "stop_iperf");
        if ((isController != 0) && (connClientPc != null)) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (connClientPc != null) {
                connClientPc.sendMessage("STOP_IPERF");
                connClientPc.sendMessage("STOP_SOCKET");
                connClientPc.stop();
                connClientPc = null;
            }
        }

        Intent stopIntent = new Intent(mContext, IperfIntentService.class);
        stopIntent.putExtra(mContext.getString(R.string.controller_iperf_switch_key), R.string.controller_iperf_stop);
        mContext.startService(stopIntent);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean initIperf() {
        InputStream in;
        try {
            //The asset "iperf" (from assets folder) inside the activity is opened for reading.
            in = mContext.getResources().getAssets().open("iperf");
            //in = getResources().getAssets().open("iperf");
        } catch (IOException e2) {
            return false;
        }
        try {
            //Checks if the file already exists, if not copies it.
            new FileInputStream(iperfPath);
        } catch (FileNotFoundException e1) {
            try {
                //The file named "iperf" is created in a system designated folder for this application.
                OutputStream out = new FileOutputStream(iperfPath, false);
                // Transfer bytes from "in" to "out"
                byte[] buf = new byte[1024];
                int len;
                int count = 0;
                while ((len = in.read(buf)) > 0) {
//                    Log.e(tag, "len is : " + len);
                    count++;
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                //After the copy operation is finished, we give execute permissions to the "iperf" executable using shell commands.
                Process processChmod = Runtime.getRuntime().exec("/system/bin/chmod 777 " + iperfPath);
                // Executes the command and waits untill it finishes.
                processChmod.waitFor();
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    private boolean initPcConnection() {
        Log.d("kiyou_iperf_test", "initPcConnection");

        if (isController == 0)
            return true;
        connClientPc = new ConnClient();
        boolean isConnected = false;

        if (host != null && ctrlPort != null) {
            isConnected = connClientPc.start(host, Integer.parseInt(ctrlPort));
            Log.e(tag, "isConnected " + isConnected);
        }
        return isConnected;
    }

    private boolean initPeerConnection() {
        return true;
    }

    public boolean checkIperfCmd() {

        iperfCmd = "";
        Log.e(tag, "port is " + (ctrlPort == null));
        Log.e(tag, "port :" + ctrlPort + "|" + "" + (ctrlPort.equals("")));

        if (ctrlPort == null || host == null || durationTime == null || windowSize == null) {
            return false;
        }
        switch (paraBundles.getInt(PARAMETERS_KEYS.item)) {
            case R.string.iperf_dul_download_key:
            case R.string.bt_call_download_key:
            case R.string.bt_play_download_key:
            case R.string.iperf_uni_download_key:
                iperfCmd = "-s -i 1 -w " + windowSize + "M";
                break;
            case R.string.bt_call_upload_key:
            case R.string.bt_play_upload_key:
            case R.string.iperf_dul_upload_key:
            case R.string.iperf_uni_upload_key:
                iperfCmd = "-c " + host + " -p " + iperfPort + " -i 1 " + " -w " + windowSize + "M -t " + durationTime;
                break;
            default:
                iperfCmd = null;
                return false;

        }
        Log.d(tag, "iperf cmd is " + iperfCmd);
        return true;
    }


    public void saveDbFile() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            Log.e(tag, "sd.canWrite() is " + sd.canWrite());
            if (sd.canWrite()) {
                Log.e(tag, "sd.canWrite ");
                String currentDbPath = "//data//com.example.myapp01//databases//wifiTest.db";
                // 创建wifiTest文件夹，以避免出现ENOENT错误
                File dbDir = new File(sd, "//wifiTest");
                dbDir.mkdirs();

                SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");
                String time = timeFormat.format(new java.util.Date());
                String date = dateFormat.format(new java.util.Date());

                String backupDbPath = "//wifiTest//wifiTest_" + date + "_" + time + ".db";
                File currentDB = new File(data, currentDbPath);
                File backupDB = new File(sd, backupDbPath);

                Log.e(tag, "currentDB.exists() is " + currentDB.exists());
                if (currentDB.exists()) {
                    Log.e(tag, "currentDB.exists()");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e(tag, "currentDB.exists()");
                    Toast.makeText(mContext, "已经复制文件到数据库中", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "当前数据库不存在", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "没有访问SD卡的权限, 复制失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.toString());
        }
    }


}
