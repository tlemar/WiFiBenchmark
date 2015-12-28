package com.vivo.zhouchen.wifibenchmark;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestBottomTabsActivity extends AppCompatActivity {
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.tabLayout)
    TableLayout mBottomTabs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bottom_tabs);

        ButterKnife.bind(this);


    }
}
