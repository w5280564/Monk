package com.qingbo.monk.base.rich.handle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.qingbo.monk.base.BaseActivity;

/**
 * 文字超链能够点击
 * 2022-8-6 waylen
 */
public class htmlClick {

    //文字超链能够点击
    public static void handleHtmlClickAndStyle(Activity activity, TextView textview, Spanned spanned) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
        URLSpan[] urls = spannableStringBuilder.getSpans(0, spanned.length(), URLSpan.class);
        for (URLSpan url : urls) {
            CustomURLSpan myURLSpan = new CustomURLSpan((BaseActivity) activity, url.getURL());
            int start = spannableStringBuilder.getSpanStart(url);
            int end = spannableStringBuilder.getSpanEnd(url);
            int flags = spannableStringBuilder.getSpanFlags(url);
            spannableStringBuilder.setSpan(myURLSpan, start, end, flags);
            spannableStringBuilder.removeSpan(url);

        }
        textview.setText(spannableStringBuilder);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //自定义CustomURLSpan，用来替换默认的URLSpan
    private  static  class CustomURLSpan extends ClickableSpan {
        private BaseActivity mActivity;
        private String mUrl;

        CustomURLSpan(BaseActivity activity, String url) {
            mUrl = url;
            mActivity = activity;
        }

        @Override
        public void onClick(View view) {
            //此处处理点击事件  mUrl 为<a>标签的href属性
            mActivity.jumpToWebView("", mUrl);
//            jumpToWebView("", mUrl);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#1e5494"));//设置文本颜色
//            ds.setUnderlineText(false);//取消下划线
        }
    }


}


