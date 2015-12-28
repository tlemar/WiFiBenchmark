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
import com.vivo.zhouchen.wifibenchmark.R;

@SuppressLint("ValidFragment")
public class BasicTabFrags extends Fragment
{
	public int pos;
	public RecyclerView recyclerView;
	public TextView tv;


	@SuppressLint("ValidFragment")
	public BasicTabFrags(int pos)
	{
		this.pos = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag, container, false);  // 初始化 view
		tv = (TextView) view.findViewById(R.id.id_tv);
		Logger.d("set tv :" + tv + "position is " + pos + "titles :" + BasicTestAdapter.TITLES[pos]);
		tv.setText(BasicTestAdapter.TITLES[pos]);
		recyclerView = (RecyclerView) view.findViewById(R.id.frag_recyclerview);


		return view;

	}
}
