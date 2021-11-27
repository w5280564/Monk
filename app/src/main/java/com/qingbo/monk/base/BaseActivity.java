package com.qingbo.monk.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.language.MultiLanguageUtil;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.xunda.lib.common.router.RouterActivityPath;
import com.xunda.lib.common.router.RouterIntentUtils;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.home.activity.MainActivity;
import com.qingbo.monk.webview.WebviewActivity;
import com.xunda.lib.common.view.CountDownTextView;

import org.greenrobot.eventbus.EventBus;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 基类Activity
 *
 * @author ouyang
 */

public abstract class BaseActivity extends FragmentActivity implements CustomTitleBar.TitleBarClickListener {
    protected MonkApplication mApplication;
    protected Context mContext;
    protected BaseActivity mActivity;
    protected CustomTitleBar titleBar;
    protected String title;
    protected int page = 1;
    protected int pageSize = 16;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 禁止横屏
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mApplication = (MonkApplication) MonkApplication.getInstance();
        mContext = mApplication.getContext();
        mActivity = this;
        mApplication.addActivity(this);
        //初始化本地数据
        initLocalData();
        //view与数据绑定
        initView();
        //设置监听
        initEvent();
        //请求服务端接口数据
        getServerData();
        setStatusBar();
    }




    /**
     * 跳转到登陆页
     */
    private void jumpToLoginActivity() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromExitAct", true);
        RouterIntentUtils.jumpTo(RouterActivityPath.Main.PAGER_LOGIN, bundle);
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
     * 设置状态栏
     */
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (titleBar == null) {
            View view = findViewById(R.id.title_bar);
            if (view != null) {
                titleBar = (CustomTitleBar) view;
                titleBar.setTitleBarClickListener(this);
                if (!TextUtils.isEmpty(title)) {
                    titleBar.setTitle(title);
                }
            }
        }
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
    public void onRightClick() {

    }

    /**
     * 页面回退
     * 栈中已经没有存在的页面时跳转到首页
     */
    protected void back() {
        int size = mApplication.getActivityCount();
        if (size <= 1) {
            skipAnotherActivity(MainActivity.class);
        }
        finish();
    }


    /**
     * 关闭app
     */
    protected void closeApp() {
        mApplication.clearActivity();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(getPackageName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mApplication.delActivity(this);

        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
        T.cancel();
    }


    /**
     * 通过id获取控件
     */
    @SuppressWarnings("unchecked")
    protected <T> T findMyViewById(int id) {
        return (T) findViewById(id);
    }


    /**
     * 功能描述:简单地Activity的跳转(不携带任何数据)
     */
    protected void skipAnotherActivity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mActivity, targetActivity);
        startActivity(intent);
    }

    /**
     * 功能描述：带数据的Activity之间的跳转
     */
    protected void skipAnotherActivity(Activity activity,
                                       Class<? extends Activity> targetActivity,
                                       HashMap<String, ? extends Object> hashMap) {
        Intent intent = new Intent(activity, targetActivity);
        Iterator<?> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator
                    .next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            }
            if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            }
            if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            }
            if (value instanceof Float) {
                intent.putExtra(key, (float) value);
            }
            if (value instanceof Double) {
                intent.putExtra(key, (double) value);
            }
        }
        activity.startActivity(intent);
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




    protected void showCommonToastDialog(String description, int type) {//1 点击确定会返回， 0 只是弹窗消失
        ToastDialog mDialog = new ToastDialog(this, getString(R.string.toast_warm_prompt), description, getString(R.string.Sure), new ToastDialog.DialogConfirmListener() {
            @Override
            public void onConfirmClick() {
                if (type == 1) {
                    back();
                }
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
    protected void jumpToPhotoShowActivity(int position, List<String> mImageList) {
        Intent intent = new Intent(mActivity, PhotoShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgList", (Serializable) mImageList);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }




    /**
     * 判断是否登录过
     *
     * @return
     */
    protected boolean isLogin() {
        return PrefUtil.isLogin();
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
            iv_noData.setImageResource(R.mipmap.icon_ty);
        }

        return emptyView;
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    /**
     * 获取验证码
     */
    protected void getSmsCode(String phone, CountDownTextView tvGetCode) {
        HashMap<String, String> baseMap = new HashMap<String, String>();
        baseMap.put("phone", phone);
        HttpSender sender = new HttpSender(HttpUrl.sendCode, "获取验证码", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {//成功
                            T.ss(getString(R.string.Base_yzmyfs));
                            tvGetCode.start();
                        } else {
                            T.ss(msg);
                        }

                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }






    @Override
    public void onBackPressed() {
        hideKeyboard();
        back();
    }

    @Override
    public void onLeftClick() {
        hideKeyboard();
        back();
    }


    /**
     * hide keyboard
     */
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
