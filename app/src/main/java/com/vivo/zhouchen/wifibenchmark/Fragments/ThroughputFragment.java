package com.vivo.zhouchen.wifibenchmark.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.viewpagerindicator.TabPageIndicator;
import com.vivo.zhouchen.wifibenchmark.Adapter.ThroughputTestAdapter;
import com.vivo.zhouchen.wifibenchmark.R;

/**
 * Created by zhouchen on 2015/12/24.
 */
public class ThroughputFragment extends Fragment {

    private Context context;
    public View mainView;
    public TabPageIndicator mTabPageIndicator;
    public ViewPager mViewPager;
    public ThroughputTestAdapter mAdapter ;

    public ThroughTabFrags getCurrentFragment() {
        int pos = mViewPager.getCurrentItem();
        return mAdapter.getItem(pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.e("onCreateView");
        View view = inflater.inflate(R.layout.basic_test, container, false);
        context = view.getContext();
        mainView = view;
        init();
        return view;
    }

    private void init() {
        checkPermissions();
        mViewPager = (ViewPager) mainView.findViewById(R.id.id_viewpager);

        mViewPager.setOffscreenPageLimit(8);

        mTabPageIndicator = (TabPageIndicator) mainView.findViewById(R.id.id_indicator);
        mAdapter = new ThroughputTestAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);
        Logger.d("adapter is " + mAdapter + "ViewPager is " + mViewPager + "indicator is " + mTabPageIndicator);
    }

    private void checkPermissions() {

    }

}
