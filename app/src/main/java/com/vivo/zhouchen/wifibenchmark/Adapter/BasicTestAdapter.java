package com.vivo.zhouchen.wifibenchmark.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.Fragments.BasicFragment;
import com.vivo.zhouchen.wifibenchmark.Fragments.BasicTabFrags;

import java.util.ArrayList;
import java.util.List;

public class BasicTestAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
{

	public static String[] TITLES = new String[]
	{ "开关", "扫描", "连接", "漫游"};
	public List<BasicTabFrags> fragmentList;
	public BasicTestAdapter(FragmentManager fm)
	{
		super(fm);
		fragmentList = new ArrayList<>(TITLES.length);
		for (int i= 0; i< TITLES.length; i++){
			fragmentList.add(new BasicTabFrags(i));
		}
        Logger.e("basic test adapter");

	}

	@Override
	public BasicTabFrags getItem(int arg0)
	{
//		BasicTabFrags fragment;
//		fragment = new BasicTabFrags(arg0);
//		Logger.e("arg0 is " + arg0 );
		return fragmentList.get(arg0);
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

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		Logger.e("page scrolled.................");
	}

	@Override
	public void onPageSelected(int position) {
		Logger.e(".................");

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		Logger.e("------------------");

	}
}
