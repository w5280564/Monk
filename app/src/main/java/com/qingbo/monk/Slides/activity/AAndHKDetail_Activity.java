package com.qingbo.monk.Slides.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.AAndHkDetail_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 内部人详情
 */
public class AAndHKDetail_Activity extends BaseActivity {
    @BindView(R.id.title_Tv)
    TextView title_Tv;
    @BindView(R.id.content_Tv)
    TextView content_Tv;


    private String uuid;
    private String isShowTop;
    private String type;

    /**
     * @param context
     * @param uuid
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String uuid, String isShowTop, String type) {
        Intent intent = new Intent(context, AAndHKDetail_Activity.class);
        intent.putExtra("uuid", uuid);
        intent.putExtra("isShowTop", isShowTop);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_aandhk;
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_main_color)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void initLocalData() {
        uuid = getIntent().getStringExtra("uuid");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void getServerData() {
        getUserDetail(false);
    }


    AAndHkDetail_Bean aAndHkDetail_bean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("uuid", uuid);
        HttpSender httpSender = new HttpSender(HttpUrl.Insider_Detail, "内部人详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    aAndHkDetail_bean = GsonUtil.getInstance().json2Bean(json_root, AAndHkDetail_Bean.class);
                    if (aAndHkDetail_bean != null) {
                        title_Tv.setText(aAndHkDetail_bean.getData().getDetail().getNewsTitle());
                        content_Tv.setText(aAndHkDetail_bean.getData().getDetail().getNewsContent());

                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


}