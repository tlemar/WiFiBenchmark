package com.vivo.zhouchen.wifibenchmark.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.AppContext;
import com.vivo.zhouchen.wifibenchmark.R;
import com.vivo.zhouchen.wifibenchmark.ottoAction.TestAction;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BottomTabsActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.showItem)
    TextView mShowItem;
    @Bind(R.id.ll_basic)
    LinearLayout linearLayout_basic;
    @Bind(R.id.ll_account)
    LinearLayout linearLayout_account;
    @Bind(R.id.ll_network)
    LinearLayout linearLayout_network;
    @Bind(R.id.ll_throughput)
    LinearLayout linearLayout_throughput;
    @Bind(R.id.tv_account)
    TextView textView_accout;
    @Bind(R.id.tv_basic)
    TextView textView_basic;
    @Bind(R.id.tv_throughput)
    TextView textView_throughput;
    @Bind(R.id.tv_network)
    TextView textView_network;
    LinearLayout mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        linearLayout_account.setOnClickListener(this);
        linearLayout_basic.setOnClickListener(this);
        linearLayout_network.setOnClickListener(this);
        linearLayout_throughput.setOnClickListener(this);

        mSelected = linearLayout_basic;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AppContext.getBusInstance().post(new TestAction());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        mSelected.callOnClick();
    }

    private void reset() {
        textView_throughput.setTextColor(Color.GRAY);
        textView_accout.setTextColor(Color.GRAY);
        textView_basic.setTextColor(Color.GRAY);
        textView_network.setTextColor(Color.GRAY);
    }

    @Override
    public void onClick(View v) {
        reset();
        switch (v.getId()) {
            case R.id.ll_throughput:
                textView_throughput.setTextColor(Color.BLACK);
                mShowItem.setText("Throughput");
                mSelected  = linearLayout_throughput;
                break;
            case R.id.ll_network:
                mShowItem.setText("network");
                mSelected = linearLayout_network;
                textView_network.setTextColor(Color.BLACK);
                break;
            case R.id.ll_account:
                mSelected = linearLayout_account;
                textView_accout.setTextColor(Color.BLACK);
                mShowItem.setText("account");
                break;
            case R.id.ll_basic:
                mSelected = linearLayout_basic;
                mShowItem.setText("basic");
                textView_basic.setTextColor(Color.BLACK);
                break;
            default:
                break;
        }
    }
}
