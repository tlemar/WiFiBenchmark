package com.vivo.zhouchen.wifibenchmark.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vivo.zhouchen.wifibenchmark.Fragments.BasicTabFrags;
import com.vivo.zhouchen.wifibenchmark.Fragments.NetTabFrags;

public class NetworkTestAdapter extends FragmentPagerAdapter
{

    public static String[] TITLES = new String[]
            { "加载图片", "加载视频", "加载网页","文件下载"};

    public NetworkTestAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0)
    {
        NetTabFrags fragment;
        fragment = new NetTabFrags(arg0);
        return  fragment;
    }

    @Override
    public int getCount()
    {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return TITLES[position];
    }

}
