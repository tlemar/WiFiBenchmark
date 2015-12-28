package com.vivo.zhouchen.wifibenchmark.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.Adapter.BasicTestAdapter;
import com.vivo.zhouchen.wifibenchmark.Adapter.NetworkTestAdapter;
import com.vivo.zhouchen.wifibenchmark.R;

@SuppressLint("ValidFragment")
public class NetTabFrags extends Fragment
{
    private int pos;

    @SuppressLint("ValidFragment")
    public NetTabFrags(int pos)
    {
        this.pos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag, container, false);  // 初始化 view
        TextView tv = (TextView) view.findViewById(R.id.id_tv);

        Logger.d("set tv :" + tv + "position is " + pos + "titles :" + NetworkTestAdapter.TITLES[pos]);

        tv.setText(NetworkTestAdapter.TITLES[pos]);
        return view;
    }
}
