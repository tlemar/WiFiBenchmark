package com.vivo.zhouchen.wifibenchmark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.bmob.push.PushConstants;

public class PushReceiver extends BroadcastReceiver {
    public PushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String string = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Toast.makeText(AppContext.getInstance(), string, Toast.LENGTH_SHORT).show();
        }
    }
}
