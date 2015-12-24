package com.vivo.zhouchen.wifibenchmark.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;
import com.vivo.zhouchen.wifibenchmark.AnyEventType;
import com.vivo.zhouchen.wifibenchmark.AppContext;
import com.vivo.zhouchen.wifibenchmark.R;
import com.vivo.zhouchen.wifibenchmark.TabAdapter;
import com.vivo.zhouchen.wifibenchmark.ottoAction.TestAction;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    TabPageIndicator mTabPageIndicator;
    ViewPager mViewPager;
    private TabAdapter mAdapter ;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent();
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mTabPageIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);


        mTabPageIndicator.setViewPager(mViewPager, 0);

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_share));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AppContext.getBusInstance().post(new TestAction());
            }
        });

        List<String> mHotSpotPackageNames = new ArrayList<String>();
        mHotSpotPackageNames.add("com.vivo.easyshare");

        Logger.e(" is easyShare " + mHotSpotPackageNames.contains("com.vivo.easyshare"));
        Logger.e(" is test" + mHotSpotPackageNames.contains("test"));

        Logger.e("10010 name :" + getPackageManager().getNameForUid(10010));


        AppContext.getBusInstance().register(this);

        EventBus.getDefault().register(this);

        EventBus.getDefault().post(new AnyEventType());

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Subscribe
    public void testAction(TestAction testAction){
        //这里更新视图或者后台操作,从TestAction获取传递参数.
        Logger.e("receving action from otto");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy(){
        AppContext.getBusInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(AnyEventType event) {/* Do something */
        Logger.e(" event bus calls");
    }
}
