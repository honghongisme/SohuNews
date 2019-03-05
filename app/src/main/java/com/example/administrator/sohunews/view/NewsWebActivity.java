package com.example.administrator.sohunews.view;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.sohunews.R;

public class NewsWebActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView mWebView;

    private View mLoadingLayout;
    private ImageView loadingIv;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        ButterKnife.bind(this);
        init();
        startWebView();
    }

    public void init() {
        url = getIntent().getStringExtra("url");
        startLoadingAnim();
    }

    public void startWebView() {/*
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");*/

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopLoadingAnim();
            }
        });
    }

    public void startLoadingAnim() {
        mLoadingLayout = findViewById(R.id.loading_view);
        loadingIv = mLoadingLayout.findViewById(R.id.loading_iv);
        ((AnimationDrawable) loadingIv.getDrawable()).start();
    }

    public void stopLoadingAnim() {
        ((AnimationDrawable) loadingIv.getDrawable()).stop();
        mLoadingLayout.setVisibility(View.GONE);
    }
}
