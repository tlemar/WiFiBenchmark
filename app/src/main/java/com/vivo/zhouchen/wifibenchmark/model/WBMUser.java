package com.vivo.zhouchen.wifibenchmark.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by zhouchen on 2015/12/13.
 */
public class WBMUser extends BmobObject {
    private String userName ;
    private String passWord ;


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
