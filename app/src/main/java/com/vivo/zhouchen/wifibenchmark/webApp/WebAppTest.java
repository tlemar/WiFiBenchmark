package com.vivo.zhouchen.wifibenchmark.webApp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.vivo.zhouchen.wifibenchmark.R;


import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

import org.apache.http.client.HttpClient;

public class WebAppTest extends AppCompatActivity {

    @Bind(R.id.WBT_downloadImage)
    Button mDownloadImage;

    @Bind(R.id.WBT_ImageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_app_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        Picasso.with(getApplicationContext()).setDebugging(true);
        Picasso.with(getApplicationContext()).load("https://www.baidu.com/img/bd_logo1.png").placeholder(R.drawable.ic_access_alarm_white_48dp).rotate(90).into(mImageView);


        mDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable getImage = new Runnable() {
                    public void run() {
                        // get the image from http://developerlife.com/theblog/wp-content/uploads/2007/11/news-thumb.png
                        // save it here (user.dir/FILENAME)
                        // file is saved here on emulator - /data/data/com.developerlife/files/file.png
                        try {
                            OkHttpClient mOkHttpClient = new OkHttpClient();
                            final Request request = new Request.Builder()
                                    .url("https://www.baidu.com/img/bd_logo1.png")
                                    .build();

                            Call call = mOkHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    Logger.e("failure");
                                }

                                @Override
                                public void onResponse(final Response response) throws IOException {
                                    //String htmlStr =  response.body().string();
                                    Logger.e("OKHTTP success" );
                                    Logger.e("code " + response.code());

                                    byte[] bRay = response.body().bytes();
                                    FileOutputStream fos = openFileOutput("test.png", Activity.MODE_WORLD_WRITEABLE);
                                    fos.write(bRay);
                                    fos.flush();
                                    fos.close();
                                    Logger.e("success to write");
                                }
                            });

                        }
                        catch (Exception e) {
                            Logger.e( "MainDriver: could not download and save PNG file", e);
                        }

                    }
                };
                new Thread(getImage).start();


            }
        });



    }

}
