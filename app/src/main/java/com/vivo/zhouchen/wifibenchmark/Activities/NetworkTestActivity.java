package com.vivo.zhouchen.wifibenchmark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.vivo.zhouchen.wifibenchmark.Fragments.BasicFragment;
import com.vivo.zhouchen.wifibenchmark.Fragments.NetWorkFragment;
import com.vivo.zhouchen.wifibenchmark.R;

public class NetworkTestActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_holder, new NetWorkFragment())
                .commit();

        Logger.e("oncreate");

    }

    @Override
    protected  void onResume() {
        super.onResume();
        initDrawer();
    }

    private void initDrawer() {

        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setItemsCanFocus(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        String[] leftSliderData = {getString(R.string.test_item_basic), getString(R.string.test_item_throughput), getString(R.string.test_item_network)
                , getString(R.string.test_item_account)};

        ArrayAdapter<String> navigationDrawerAdapter = new ArrayAdapter<>(
                NetworkTestActivity.this, R.layout.drawer_list_item, R.id.tv_list_item, leftSliderData);

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


}
