package com.vivo.zhouchen.wifibenchmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;
import com.vivo.zhouchen.wifibenchmark.AnyEventType;
import com.vivo.zhouchen.wifibenchmark.AppContext;
import com.vivo.zhouchen.wifibenchmark.Fragments.BasicFragment;
import com.vivo.zhouchen.wifibenchmark.Fragments.NetWorkFragment;
import com.vivo.zhouchen.wifibenchmark.Fragments.ThroughputFragment;
import com.vivo.zhouchen.wifibenchmark.R;
import com.vivo.zhouchen.wifibenchmark.ottoAction.TestAction;

import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {



    private FragmentName currentFragment;

    private BasicFragment mBasicFrag;
    private ThroughputFragment mThroughputFrag;
    private NetWorkFragment mNetworkFrag;

    private int currentItem = -1;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    //
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = new ImageView(this);
        //imageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_share));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AppContext.getBusInstance().post(new TestAction());
            }
        });

        AppContext.getBusInstance().register(this);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new AnyEventType());

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initDrawer();
        mBasicFrag = new BasicFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_holder, mBasicFrag)
                .commit();
        Logger.e("activity on resume");

    }

    private void initDrawer() {

        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setItemsCanFocus(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        String[] leftSliderData = {getString(R.string.test_item_basic), getString(R.string.test_item_throughput), getString(R.string.test_item_network)
                , getString(R.string.test_item_account)};

        ArrayAdapter<String> navigationDrawerAdapter = new ArrayAdapter<>(
                MainActivity.this, R.layout.drawer_list_item, R.id.tv_list_item, leftSliderData);

        drawerList.setAdapter(navigationDrawerAdapter);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d("position is " + position);
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                Intent intent;

                switch (position) {
                    case 0:
                        Logger.e("basic clicked");
                        intent = new Intent(getApplicationContext(), BasicTestActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), ThroughputTestActivity.class);
                        startActivity(intent);

                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), NetworkTestActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }, 200);
                }
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


    private void fragmentSwitcher(Fragment fragment, int itemId,
                                  FragmentName fname, @AnimRes int animationEnter,
                                  @AnimRes int animationExit) {
        currentFragment = fname;
        if (currentItem == itemId) {
            // Don't allow re-selection of the currently active item
            return;
        }
        currentItem = itemId;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(String.valueOf(fname));

        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animationEnter, animationExit)
                .replace(R.id.main_fragment_holder, fragment)
                .commit();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }, 200);
        }
    }


    public enum FragmentName {
        Basic, Throughput, NetWork, Account
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Subscribe
    public void testAction(TestAction testAction) {
        //这里更新视图或者后台操作,从TestAction获取传递参数.
        Logger.e("receving action from otto");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy() {
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
