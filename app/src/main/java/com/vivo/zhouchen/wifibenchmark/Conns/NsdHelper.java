package com.vivo.zhouchen.wifibenchmark.Conns;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

/**
 * Created by Administrator on 2015/10/26.
 */

public class NsdHelper {

    Context mContext = null;

    NsdManager mNsdManager = null;
    NsdManager.RegistrationListener mRegistrationListener = null;
    NsdManager.DiscoveryListener mDiscoveryListener = null;
    NsdManager.ResolveListener mResolveListener = null;

    public static final String SERVICE_TYPE = "_http._tcp.";

    public static final String TAG = "NsdHelper";
    public String mServiceName = "NsdChart";

    NsdServiceInfo mService;

    public NsdHelper(Context context){
        mContext = context;

        // 获取系统网络服务管理器
        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
    }

    public void initNsd(){
        initDiscoveryListener();
        initRegistrationListener();
        initResolveListener();
    }


    // 初始化注册网络服务发现事件监听器
    public void initDiscoveryListener(){
        Log.d(TAG, "initDiscoveryListener is run.");

        mDiscoveryListener = new NsdManager.DiscoveryListener(){

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "service discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "Discovery failed: Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "Discovery failed: Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Service discovery success " + serviceInfo);

                if (serviceInfo != null){
                    Log.d(TAG, "InitDiscoveryListener: NsdServiceInfo: " + serviceInfo);
                }

                if(!serviceInfo.getServiceType().equals(SERVICE_TYPE)){
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                } else if (serviceInfo.getServiceName().equals(mServiceName)){
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else if (serviceInfo.getServiceName().contains("NsdChart")){
                    try {
                        mNsdManager.resolveService(serviceInfo, mResolveListener);
                    } catch (IllegalArgumentException e){
                        Log.e(TAG, "resolve listener already in use");
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {

            }
        };
    }

    // 初始化网络服务连接事件监听器
    public void initResolveListener(){
        Log.d(TAG, "initResolveListener is run");
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                mService = serviceInfo;

                Log.d(TAG, "InitResolveListener: serviceInfo: " + mService);
            }

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed: Error code: " + errorCode);
            }
        };
    }

    // 初始化网络服务注册事件监听器
    public void initRegistrationListener(){
        Log.d(TAG, "iniRegistrationListener is run");
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                mServiceName = serviceInfo.getServiceName();
                Log.d(TAG, "Register succeeded. " + serviceInfo);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Register failed: Error code: " + errorCode);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Register failed: Error code: " + errorCode);
            }
        };
    }

    // 注册网络服务
    // 注册网络服务需要两样东西：网络服务的信息(NsdServiceInfo)和注册事件监听器(NsdManager.Registration)
    public void registerService(int port){
        Log.d(TAG, "registerService is run.");

        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        Log.d(TAG, "" + serviceInfo.getPort());
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        try {
            Log.d(TAG, "registerDervice info " + serviceInfo);
            mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
        } catch (IllegalArgumentException e){
            Log.e(TAG, "register " + e.getMessage());
        }
    }

    // 发现网络服务
    public void discoverServices(){
        Log.d(TAG, "discoveryServices is run.");
        try{
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "discovery " + e.getMessage());
        }
    }

    public void stopDiscovery() {
        try {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);

            Log.d(TAG,"discovery stop done.");
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "stopDiscovery: ", e);
        }

    }

    public NsdServiceInfo getChosenServiceInfo(){
        return mService;
    }

    public boolean tearDown(){
        try {
            Log.d(TAG, "NsdHelper: tearDown: stopServiceDiscovery");
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "NsdHelper: tearDown: stopServiceDiscovery: ", e);
        }

        try {
            Log.d(TAG, "NsdHelper: tearDown: unRegisterService");
            mNsdManager.unregisterService(mRegistrationListener);
            mService = null;

        } catch (IllegalArgumentException e){
            Log.d(TAG, "NsdHelper: tearDown: unRegisterService ", e);
        }

        return true;

    }
}