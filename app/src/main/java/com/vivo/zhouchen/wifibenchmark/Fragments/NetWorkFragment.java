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
import com.vivo.zhouchen.wifibenchmark.Adapter.BasicTestAdapter;
import com.vivo.zhouchen.wifibenchmark.Adapter.NetworkTestAdapter;
import com.vivo.zhouchen.wifibenchmark.R;

/**
 * Created by zhouchen on 2015/12/24.
 */
public class NetWorkFragment extends Fragment {

    private Context context;
    private View mainView;
    TabPageIndicator mTabPageIndicator;
    ViewPager mViewPager;
    private NetworkTestAdapter mAdapter ;


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
        mTabPageIndicator = (TabPageIndicator) mainView.findViewById(R.id.id_indicator);
        mAdapter = new NetworkTestAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);
        Logger.d("adapter is " + mAdapter + "ViewPager is " + mViewPager + "indicator is " + mTabPageIndicator);

    }

    private void checkPermissions() {

    }

}
