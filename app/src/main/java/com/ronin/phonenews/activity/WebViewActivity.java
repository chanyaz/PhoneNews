package com.ronin.phonenews.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ronin.cc.util.ExtKt;
import com.ronin.phonenews.R;
import com.ronin.phonenews.javascriptbridge.BridgeWebView;
import com.ronin.phonenews.util.OnDoubleClickListener;

import org.jetbrains.annotations.Nullable;

import cn.waps.AppConnect;
import cn.waps.AppListener;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    private static final String KEY_WEBVIEW_AD_SHOW_TIME = "KEY_WEBVIEW_AD_SHOW_TIME";
    private static final int MSG_WEBVIEW_RELOAD = 0X001;
    public static String WEB_URL = "key_web_url";
    public static String WEB_TITLE = "key_web_title";

    private BridgeWebView webview;
    private String mWebUrl = "www.baidu.com";
    private String mWebTitle = "闻娱多多";
    private Toolbar toolbar;
    private ProgressBar id_progressbar;
    private FrameLayout layout_error;
    private RelativeLayout news_layout_ad;
    private LinearLayout layout_ad_banner;
    // 抽屉广告布局
    private View slidingDrawerView;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_WEBVIEW_RELOAD:

                break;
        }
    }

    public static void action(Activity ay, String title, final String url) {
        Intent intent = new Intent(ay, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_TITLE, title);
        intent.putExtra(WebViewActivity.WEB_URL, url);
        ay.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            mWebUrl = bun.getString(WEB_URL);
            mWebTitle = bun.getString(WEB_TITLE);
        }

        initView();

    }


    protected void initView() {
        news_layout_ad = (RelativeLayout) findViewById(R.id.news_layout_ad);
        layout_ad_banner = (LinearLayout) findViewById(R.id.layout_ad_banner);

        initToolBar();
        initWebview();
        loadUrl();
        initErrorLayout();
    }

    /**
     * 初始化错误布局
     */
    private void initErrorLayout() {
        id_progressbar = (ProgressBar) findViewById(R.id.id_progressbar);
        layout_error = (FrameLayout) findViewById(R.id.layout_error);
        layout_error.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View view) {
                webview.clearView();
                layout_error.setVisibility(View.GONE);
                id_progressbar.setVisibility(View.VISIBLE);

                webview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webview.reload();
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化广告显示
     */
    private void initAdShow() {
        if (layout_ad_banner != null) {
            layout_ad_banner.removeAllViews();
        }
        // 10秒刷新一次
        AppConnect.getInstance(WebViewActivity.this)
                .showMiniAd(WebViewActivity.this, layout_ad_banner, 10);
        //加载广告
        AppConnect.getInstance(WebViewActivity.this)
                .showBannerAd(WebViewActivity.this,
                        layout_ad_banner, new AppListener() {

                            @Override
                            public void onBannerClose() {
                                super.onBannerClose();

                            }

                            @Override
                            public void onBannerNoData() {
                                super.onBannerNoData();

                            }
                        });

        AppConnect.getInstance(this).initAdInfo();

    }

    /**
     * webview load url
     */
    private void loadUrl() {
        if (!TextUtils.isEmpty(mWebUrl)) {
            webview.post(new Runnable() {
                @Override
                public void run() {
                    webview.loadUrl(mWebUrl);
                }
            });
        }
    }

    /**
     * 初始化actionBar
     */
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_close);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle(mWebTitle);
    }

    /**
     * 初始化 webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initWebview() {
        webview = (BridgeWebView) findViewById(R.id.id_bridgewebview);
        webview.init();
        // webview.setDefaultHandler(new DefaultHandler());
        // 设置加载文件的格式
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        // 设置支持javascript
        webview.getSettings().setJavaScriptEnabled(true);
        // 是否保存密码
        webview.getSettings().setSavePassword(false);
        // 优先不使用缓存
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 屏蔽复制事件
//        webview.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
        // use html5 viewport attribute
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        // 取消优先加载图片
        // webview.getSettings().setBlockNetworkImage(false);
        // 启用硬件加速
        webview.getSettings().setPluginState(PluginState.ON);
        // 设置支持缩放
        webview.getSettings().setSupportZoom(true); // 设置是否支持缩放
        webview.getSettings().setBuiltInZoomControls(true); // 设置是否显示内建缩放工具
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        // 设置允许访问文件
        webview.getSettings().setAllowFileAccess(false);
        // 设置Web视图
        // webview.setWebViewClient(new HelloWebViewClient());
        // webview.setWebChromeClient(wvcc);
        // 设置下载监听
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        // 设置缩放
        webview.setBackgroundColor(0);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


                return super.shouldOverrideUrlLoading(view, request);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!ExtKt.isNetwork(WebViewActivity.this)) {
                    layout_error.setVisibility(View.VISIBLE);
                } else {
                    id_progressbar.setVisibility(View.VISIBLE);
                    layout_error.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }


            @Override
            public void onReceivedError(final WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                view.stopLoading();
                view.clearView();
//                layout_error.setVisibility(View.VISIBLE);
                toolbar.setTitle(getString(R.string.app_name));
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 80) {
                    id_progressbar.setVisibility(View.GONE);
                    layout_error.setVisibility(View.GONE);
                    if (newProgress == 100) {
                        //页面加载完成后，开始加载广告
                        initAdShow();
                    }
                } else {
                    if (id_progressbar.getVisibility() == View.GONE) {
                        id_progressbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);
        webview.clearHistory();
        webview.destroy();

    }

    /*
     * 返回键事件
     */
    public void goBack() {
        this.finish();
    }

    @Override
    public void finish() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        }
        super.finish();
    }

    /**
     * 拦截物理返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                goBack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;

        }
    }

}
