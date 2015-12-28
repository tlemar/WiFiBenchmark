package com.vivo.zhouchen.wifibenchmark.Models;

/**
 * Created by Administrator on 2015/10/31.
 */
import org.litepal.crud.DataSupport;
import vivo.zhouchen.wifiBaseLibrary.model.WifiEnvTable;

public class IperfSpeedTable extends DataSupport {
    private String sysDate = null;
    private String sysTime = null;
    private String mode = null;
    private String location = null;
    private String item = null;
    private String tcpUdp = null;
    private String kSpeed = null;
    private String isBtOn = null;
    private String isGpsOn = null;
    private String isLteOn = null;
    private WifiEnvTable mWifiEnv = null;
    private String isWifiScan = null;

    private int executeTime = 0;
    private String executeType = null;

    private int interval = 0;

    public IperfSpeedTable() {
    }

    public String getExecuteType() {
        return this.executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public int getExecuteTime() {
        return this.executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public String getSysTime() {
        return this.sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getkSpeed() {
        return this.kSpeed;
    }

    public void setkSpeed(String kSpeed) {
        this.kSpeed = kSpeed;
    }

    public String getIsBtOn() {
        return this.isBtOn;
    }

    public void setIsBtOn(String isBtOn) {
        this.isBtOn = isBtOn;
    }

    public String getIsGpsOn() {
        return this.isGpsOn;
    }

    public void setIsGpsOn(String isGpsOn) {
        this.isGpsOn = isGpsOn;
    }

    public String getIsLteOn() {
        return this.isLteOn;
    }

    public void setIsLteOn(String isLteOn) {
        this.isLteOn = isLteOn;
    }

    public WifiEnvTable getmWifiEnv() {
        return this.mWifiEnv;
    }

    public void setmWifiEnv(WifiEnvTable mWifiEnv) {
        this.mWifiEnv = mWifiEnv;
    }

    public String getIsWifiScan() {
        return this.isWifiScan;
    }

    public void setIsWifiScan(String isWifiScan) {
        this.isWifiScan = isWifiScan;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSysDate() {
        return this.sysDate;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public String getTcpUdp() {
        return this.tcpUdp;
    }

    public void setTcpUdp(String tcpUdp) {
        this.tcpUdp = tcpUdp;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
