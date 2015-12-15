package com.vivo.zhouchen.wifibenchmark;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.logger.Logger;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by zhouchen on 2015/12/12.
 */
public class AppContext extends Application {
    private static AppContext instance;


    public static AppContext getInstance() {
        Logger.e("context is " + instance);
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Logger.init("zc") ;                // default PRETTYLOGGER or use just init()
        Bmob.initialize(this, "492119a3da2275e2a3e79e8dea105102");
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "492119a3da2275e2a3e79e8dea105102");
        Iconify.with(new FontAwesomeModule());
        instance = this;
        Logger.e("context is " + instance + " on create ") ;
    }

}
