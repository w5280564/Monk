package com.qingbo.monk.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.SimpleImmersionFragment;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.webview.WebviewActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.xunda.lib.common.view.CountDownTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends SimpleImmersionFragment{
    protected MonkApplication mApplication;
    protected Context mContext;
    protected BaseActivity mActivity;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mApplication = (MonkApplication) MonkApplication.getInstance();
        mContext = mApplication.getContext();
        mActivity = (BaseActivity) getActivity();
        initLocalData();
        //view与数据绑定
        initView();
        initView(mRootView);
        //设置监听
        initEvent();
        //请求服务端接口数据
        getServerData();
        return mRootView;
    }



    /**
     * 绑定布局文件
     */
    protected abstract int getLayoutId();


    /**
     * 初始化本地数据
     */
    protected void initLocalData() {

    }


    /**
     * 初始化view
     */
    protected void initView() {

    }

    /**
     * 初始化view
     */
    protected void initView(View mRootView) {

    }


    /**
     * 设置监听
     */
    protected void initEvent() {

    }


    /**
     * 请求服务端接口数据
     */
    protected void getServerData() {

    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 建议在`自定义页面`的页面结束函数中调用
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 功能描述:简单地Activity的跳转(不携带任何数据)
     */
    protected void skipAnotherActivity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mActivity, targetActivity);
        startActivity(intent);
    }

    /**
     * 跳转到默认H5展示页面
     */
    protected void jumpToWebView(String title, String url) {
        Intent intent = new Intent(mActivity, WebviewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    protected void showCommonToastDialog(String description) {
        ToastDialog mDialog = new ToastDialog(mActivity, getString(R.string.toast_warm_prompt), description, getString(R.string.Sure), new ToastDialog.DialogConfirmListener() {
            @Override
            public void onConfirmClick() {

            }
        });
        mDialog.show();
    }

    /**
     * 跳转到查看图片页
     *
     * @param position
     * @param mImageList
     */
    public void jumpToPhotoShowActivity(int position, List<String> mImageList) {
        Intent intent = new Intent(mActivity, PhotoShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgList", (Serializable) mImageList);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 跳转到查看图片页(单张图片)
     *
     * @param image_url
     */
    protected void jumpToPhotoShowActivitySingle(String image_url) {
        Intent intent = new Intent(mActivity, PhotoShowActivity.class);
        intent.putExtra("image_url", image_url);
        intent.putExtra("single", true);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected View addEmptyView(String text, int imgResource) {
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.empty_view_layout, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView iv_noData = emptyView.findViewById(R.id.iv_noData);

        if (!StringUtil.isBlank(text)) {
            TextView tv_noData = emptyView.findViewById(R.id.tv_noData);
            tv_noData.setText(text);
        }

        if (imgResource != 0) {
            iv_noData.setImageResource(imgResource);
        } else {
            iv_noData.setImageResource(R.mipmap.icon_no_date);
        }

        return emptyView;
    }

    @Override
    public void initImmersionBar() {
    }

    /**
     * 获取验证码
     */
    protected void getSmsCode(String area_code,String phone, CountDownTextView tvGetCode) {
        HashMap<String, String> baseMap = new HashMap<String, String>();
        baseMap.put("area", area_code);
        baseMap.put("mobile", phone);
        HttpSender sender = new HttpSender(HttpUrl.sendCode, "获取验证码", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {//成功
                            T.ss(getString(R.string.Base_yzmyfs));
                            tvGetCode.start();
                        }

                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }











}

