package com.qingbo.monk.webview;

import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseWebviewActivity;


/**
 * 默认的H5展示页面
 */
public class WebviewActivity extends BaseWebviewActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        mWebview = findViewById(R.id.webview);
        mProgress = findViewById(R.id.progress);
        initWebView();
        String linkUrl = getIntent().getStringExtra("url");
        L.e(linkUrl + "");

        if (!StringUtil.isBlank(linkUrl)) {
            if(!linkUrl.startsWith("http")){
                linkUrl = HttpUrl.server + linkUrl;
            }
            loadUrl(linkUrl);
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        titleBar.hideRight();
    }

}
