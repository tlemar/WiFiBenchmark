package com.vivo.zhouchen.wifibenchmark.TestPlaner;

import android.content.Context;
import android.os.Bundle;

import com.vivo.zhouchen.wifibenchmark.R;

import vivo.zhouchen.wifiBaseLibrary.netUtils.NetUtils;

import java.util.Map;

/**
 * Created by vivo on 2015/10/21.
 */
public class BtIperfTest extends IperfTest {

    Bundle paraBundle;
//    public BtIperfTest(Context context){
//        super(context);
//    }
    public BtIperfTest(Context context , Bundle bundle){
        super(context, bundle);
        paraBundle = bundle;
    }


    @Override
    public Map<String, String> checkConditions() {
        super.checkConditions();
        if(!netUtils.isBtConnected()){
            errorReasons.put(mContext.getString(R.string.error_type_bt), mContext.getString(R.string.error_code_bt_not_connect));
        }

        return errorReasons;
    }
}
