package com.vivo.zhouchen.wifibenchmark.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.vivo.zhouchen.wifibenchmark.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {

    @Bind(R.id.RecyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.btn_add)
    Button mButton;

    List<Person> personList;
    PersonAdapter adapter;

    private int age = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        personList = new ArrayList<>();
        personList.add(new Person(1, "zhouchen", 15));
        adapter = new PersonAdapter(personList);
        mRecyclerView.setAdapter(adapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personList.add(new Person(1, "zc", age++));
                adapter.notifyDataSetChanged();
            }
        });
    }

}
