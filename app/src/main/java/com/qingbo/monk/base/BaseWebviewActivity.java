package com.qingbo.monk.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import wendu.webviewjavascriptbridge.WVJBWebView;

/**
 * H5页面的基类
 *
 * @author ouyang
 */
public abstract class BaseWebviewActivity extends BaseActivity {

    protected ProgressBar mProgress;
    protected WVJBWebView mWebview;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        title = getIntent().getStringExtra("title");
    }


    @SuppressLint({"SetJavaScriptEnabled", "DefaultLocale"})
    protected void initWebView() {
        WebSettings setting = mWebview.getSettings();
        setting.setSupportZoom(false);// 不支持缩放
        setting.setBuiltInZoomControls(false);// 隐藏缩放工具
        setting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);// 使所有列的宽度不超过屏幕宽度
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

        setting.setJavaScriptEnabled(true);// 支持javascript
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setLoadsImagesAutomatically(true);
        // 开始自定义UserAgent
        setting.setUserAgentString(setting.getUserAgentString()
                + AndroidUtil.getCustomUserAgent(mActivity));
        mWebview.setVerticalScrollBarEnabled(true);
        mWebview.setHorizontalScrollBarEnabled(false);
        mWebview.requestFocus();
        mWebview.setDownloadListener(new DownLoadListener());
        mWebview.setWebChromeClient(webchromeClient);
        getDataFromH5();
        putDataToH5();
    }



    protected void loadUrl(String linkUrl) {

        L.e("加载h5链接是"+linkUrl);

        if (StringUtil.isBlank(linkUrl)) {
            T.ss("链接错误");
            finish();
        } else {
            mWebview.loadUrl(linkUrl) ;
        }
    }


    /**
     * 从H5获取参数
     */
    private void getDataFromH5() {











    }




    /**
     * 把参数传递给H5
     */
    private void putDataToH5() {
        mWebview.callHandler("testObjcCallback", SharePref.user().getToken(), new WVJBWebView.WVJBResponseCallback() {
            @Override
            public void onResult(Object data) {

            }

        });
    }







    public void setMyProgress(int newProgress){
        if(mProgress == null){
            return;
        }
        mProgress.setProgress(newProgress);
        if (newProgress == 100) {
            mProgress.setVisibility(View.GONE);
            mWebview.setVisibility(View.VISIBLE);
        } else {
            if (View.GONE == mProgress.getVisibility()) {
                mProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    WebChromeClient webchromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String t) {
            super.onReceivedTitle(view, t);
            if (!StringUtil.isBlank(t) && StringUtil.isBlank(title)) {
                if(titleBar!=null){
                    titleBar.setTitle(t);
                }
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            setMyProgress(newProgress);
        }
    };


    @Override
    public void onBackPressed() {
        judgeGoBack();
    }

    private void judgeGoBack() {
        if (mWebview.canGoBack()) {
            mWebview.goBackOrForward(-1);
        } else {
            finish();
        }
    }


    @Override
    public void onLeftClick() {
        judgeGoBack();
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            LinearLayout rootLayout = (LinearLayout) ((ViewGroup) findViewById(android.R.id.content))
                    .getChildAt(0);
            rootLayout.removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }



    /**
     * 简单的文件下载支持
     */
    private class DownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
