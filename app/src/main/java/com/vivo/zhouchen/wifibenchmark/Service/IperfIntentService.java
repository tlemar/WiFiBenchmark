package com.vivo.zhouchen.wifibenchmark.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.vivo.zhouchen.wifibenchmark.CONSTANTS.PARAMETERS_KEYS;
import com.vivo.zhouchen.wifibenchmark.Models.IperfSpeedTable;
import com.vivo.zhouchen.wifibenchmark.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vivo on 2015/10/26.
 */

public class IperfIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    static Process iperfProc = null;
    final String iperfPath = "/data/data/com.example.myapp01/iperf ";
    final String iperfFilter = "iperf";
    String iperfKey = "iperf.start.cmd";

    String iperfResult = "iperf.result";

    String tag = "IperfIntentService";
    final String ACTION_IPERF_TERMINTATED = "iperf.terminated";
    Toast toast;

    String testItem = "";

    static int count = 0;

    private static int execute_time = 0;
    private static String execute_type = null;

    public IperfIntentService() {
        super("IperfIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getIntExtra(getString(R.string.controller_iperf_switch_key), 0)) {
            case R.string.controller_iperf_start:
                execute_time = intent.getIntExtra("execute_time", 0);
                execute_type = intent.getStringExtra("execute_type");
                testItem = intent.getStringExtra(PARAMETERS_KEYS.item);
                Log.e(tag, "test item is :" + testItem);
                try {
                    startIperf(intent.getStringExtra(getString(R.string.controller_iperf_cmd_key)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.string.controller_iperf_stop:
                try {
                    Log.e(tag, "stop iperf");
                    stopIperf();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        Log.e(tag, "onDestroy");
    }

    public void stopIperf() throws RemoteException {
        if (iperfProc != null) {
            iperfProc.destroy();
            Log.e("iperfService", "iperf process destroyed");
            iperfProc = null;
            Toast.makeText(this.getApplicationContext(), "iperf closed", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("iperfService", "iperf Process is null");
        }
    }

//    boolean isIperfTeminated(){
//        while ()
//        return false
//    }

    public void startIperf(String cmd) throws RemoteException {
//        Log.e("iperfService", "start iperf with the cmd:" + cmd);
        if (iperfProc == null) {
            try {
                if (cmd == null) {
                    Toast.makeText(this.getApplicationContext(), "input cmd is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean input = cmd.matches("(iperf )?((-[s,-server])|(-[c,-client] ([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]))|(-[c,-client] \\w{1,63})|(-[h,-help]))(( -[f,-format] [bBkKmMgG])|(\\s)|( -[l,-len] \\d{1,5}[KM])|( -[B,-bind] \\w{1,63})|( -[r,-tradeoff])|( -[v,-version])|( -[N,-nodelay])|( -[T,-ttl] \\d{1,8})|( -[U,-single_udp])|( -[d,-dualtest])|( -[w,-window] \\d{1,5}[KM])|( -[n,-num] \\d{1,10}[KM])|( -[p,-port] \\d{1,5})|( -[L,-listenport] \\d{1,5})|( -[t,-time] \\d{1,8})|( -[i,-interval] \\d{1,4})|( -[u,-udp])|( -[b,-bandwidth] \\d{1,20}[bBkKmMgG])|( -[m,-print_mss])|( -[P,-parallel] d{1,2})|( -[M,-mss] d{1,20}))*");
//                Log.e("iperfService", "isvalid is " + input);
                if (!input) {
                    Toast.makeText(this.getApplicationContext(), "please check the input cmd", Toast.LENGTH_SHORT).show();
                    return;
                }
                iperfProc = Runtime.getRuntime().exec(iperfPath + " " + cmd);
                //Log.e(tag, "exitValue 0:" + iperfProc.exitValue());
                Toast.makeText(this.getApplicationContext(), "iperf started", Toast.LENGTH_SHORT).show();
            } catch (IOException var7) {
                var7.printStackTrace();
//                Log.e("iperfService", "iperf failed to start");
                return;
            }

            final BufferedReader input1 = new BufferedReader(new InputStreamReader(iperfProc.getInputStream()));
            final BufferedReader error = new BufferedReader(new InputStreamReader(iperfProc.getErrorStream()));
            Thread readThread = new Thread(new Runnable() {
                String inputLine = null;
                String errorLine = null;
                //StringBuffer outs = new StringBuffer();
                String outs = "";
                int isTerminated = 0;

                public void run() {
                    try {
                        while ((inputLine = input1.readLine()) != null || (errorLine = error.readLine()) != null) {
                            if (inputLine != null) {
                                //outs.append(inputLine);
                                outs = inputLine;
                                extractAndSaveResult(inputLine);
                                if (inputLine.contains(" 0.0-")) {
                                    //setIsTerminated(isTerminated++);
                                    isTerminated++;
                                }
                            } else {
                                //outs.append(errorLine);
                                outs = errorLine;
                            }

                            sendIperfResultBroadcast();

                            if (isTerminated == 2) {
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d(tag, "iperf teminated");
                                Intent iperfFinished = new Intent(ACTION_IPERF_TERMINTATED);
                                // send iperf finish broadcast
                                iperfFinished.putExtra("iperf.state", 2);
                                sendBroadcast(iperfFinished);
                                isTerminated = 0;

                                try {
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                iperfFinished = new Intent(ACTION_IPERF_TERMINTATED);
                                // send iperf finish broadcast
                                iperfFinished.putExtra("iperf.state", 3);
                                sendBroadcast(iperfFinished);
                                break;
                            }
                        }
                    } catch (IOException var3) {
                        var3.printStackTrace();
                    }
                }

                private void sendIperfResultBroadcast() {
                    Intent inputIntent = new Intent();
                    //inputIntent.putExtra(iperfResult, outs.toString());
                    inputIntent.putExtra(iperfResult, outs);
                    outs = "";
                    inputIntent.setAction(iperfResult);
                    sendBroadcast(inputIntent);
                }

                private boolean extractAndSaveResult(String outLine) {
                    IperfSpeedTable speedTable = new IperfSpeedTable();
                    // 识别出line的特定字符串
                    Pattern mPattern = Pattern.compile("(Mbits/sec)|(Kbits/sec)|(bits/sec)");
                    Matcher matcher = mPattern.matcher(outLine.toString());
                    String metric;

                    String[] test = outLine.split(" ");
                    for (int i = 0; i < test.length; i++) {
//                        Log.e(tag, "test[" + i + "]is " + test[i]);

                        boolean isValid = mPattern.matcher(test[i]).matches();
//                        Log.e(tag, "isValid " + isValid);
                        if (isValid) {
                            float speed = Float.parseFloat(test[i - 1]);
//                            Log.e(tag, "speed is " + speed);

                            metric = test[i];
//                            Log.e(tag, "metric is " + metric);
                            if (metric.equals("Mbits/sec")) {
                                speedTable.setkSpeed(speed * 1024 + "");
                            } else if (metric.equals("Kbits/sec")) {
                                speedTable.setkSpeed(speed + "");
                            } else {
                                speedTable.setkSpeed("0");
                            }
                        }
                        // 如果速度不为null，则在数据库中记录一组数据
                        if (speedTable.getkSpeed() != null) {
                            speedTable.setIsBtOn("false");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hhmmss");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
                            String time = timeFormat.format(new Date());
                            String date = dateFormat.format(new Date());
                            Log.e(tag, "time is " + time);
                            Log.e(tag, "date is " + date);
                            speedTable.setSysTime(time);
                            speedTable.setSysDate(date);
                            speedTable.setItem(testItem);
                            speedTable.setExecuteType(execute_type);
                            speedTable.setExecuteTime(execute_time);
                            speedTable.saveThrows();

                        }
                    }
                    return true;
                }
            });
            readThread.start();
        } else {
            //Log.e(tag, "exitValue 0:" + iperfProc.exitValue());
//            Log.e("iperfService", "the iperf proc has been activated");
            Toast.makeText(this.getApplicationContext(), "iperf process has been started,\nplease stop first or restarted the phone", Toast.LENGTH_SHORT).show();
        }
    }

}
