package com.vivo.zhouchen.wifibenchmark;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @Bind(R.id.webview_test)
    WebView mWebView;

    @Bind(R.id.webView_title)
    TextView mTextView;

    @Bind(R.id.nv_next)
    Button mNext;

    @Bind(R.id.nv_previous)
    Button mPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mWebView.canGoBack();
        mWebView.canGoForward();
        mWebView.canGoBackOrForward(3);

        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.loadUrl("http://www.baidu.com");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }


    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}
