package com.vivo.zhouchen.wifibenchmark.Models;

import org.litepal.crud.DataSupport;

/**
 * Created by vivo on 2015/10/30.
 */
public class WifiOpenStressTable extends DataSupport{

    private String time;
    private String itme;
    private String phase;

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItme() {
        return itme;
    }

    public void setItme(String itme) {
        this.itme = itme;
    }
}
