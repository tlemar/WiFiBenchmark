package com.vivo.zhouchen.wifibenchmark.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.Adapter.BasicTestAdapter;
import com.vivo.zhouchen.wifibenchmark.Adapter.NetworkTestAdapter;
import com.vivo.zhouchen.wifibenchmark.Adapter.ThroughputTestAdapter;
import com.vivo.zhouchen.wifibenchmark.R;

@SuppressLint("ValidFragment")
public class ThroughTabFrags extends Fragment {
    private int pos;
    RecyclerView recyclerView;

    public TextView tv;

    @SuppressLint("ValidFragment")
    public ThroughTabFrags(int pos) {
        this.pos = pos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.e("onCreateView");
        View view = inflater.inflate(R.layout.frag, container, false);  // 初始化 view
        tv = (TextView) view.findViewById(R.id.id_tv);

        recyclerView = (RecyclerView) view.findViewById(R.id.frag_recyclerview);

        Logger.d("set tv :" + tv + "position is " + pos + "titles :" + ThroughputTestAdapter.TITLES[pos]);
//        tv.setText(ThroughputTestAdapter.TITLES[pos]);
        return view;
    }
}
