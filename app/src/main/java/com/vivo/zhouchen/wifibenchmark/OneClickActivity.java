package com.vivo.zhouchen.wifibenchmark;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;


import butterknife.Bind;
import butterknife.ButterKnife;




import at.grabner.circleprogress.CircleProgressView;
import de.greenrobot.event.EventBus;

public class OneClickActivity extends AppCompatActivity {

    @Bind(R.id.PhaseIndicator)
    CircleProgressView mPhaseIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_click_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mPhaseIndicator.setTextSize(48);
        mPhaseIndicator.setText("OneKey click to get data");

        Logger.d("datada");

//        Iconify
//                .with(new FontAwesomeModule())
//                .with(new EntypoModule())
//                .with(new TypiconsModule())
//                .with(new MaterialModule())
//                .with(new MeteoconsModule())
//                .with(new WeathericonsModule())
//                .with(new SimpleLineIconsModule())
//                .with(new IoniconsModule());
//
//
//        ImageView imageView = new ImageView(this);
//
//        imageView.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_share));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        EventBus.getDefault().register(this);
    }

    public void onEvent(AnyEventType type) {
        Logger.d("receive type");
    }

}
