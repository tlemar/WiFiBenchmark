package com.vivo.zhouchen.wifibenchmark.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.Fragments.BasicTabFrags;
import com.vivo.zhouchen.wifibenchmark.Fragments.ThroughTabFrags;

import java.util.ArrayList;
import java.util.List;

public class ThroughputTestAdapter extends FragmentPagerAdapter
{


    public static String[] TITLES = new String[]
            { "单独上传", "单独下载", "竞争上传","竞争下载","蓝牙音乐干扰","蓝牙通话干扰"};
    public List<ThroughTabFrags> fragmentList;

    public ThroughputTestAdapter(FragmentManager fm)
    {
        super(fm);
        fragmentList = new ArrayList<>(TITLES.length);
        for (int i =0 ; i<TITLES.length; i++){
            fragmentList.add(new ThroughTabFrags(i));
        }
    }

    @Override
    public ThroughTabFrags getItem(int arg0)
    {
        return  fragmentList.get(arg0);
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
