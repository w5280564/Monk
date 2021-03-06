package com.qingbo.monk.person.activity;

import android.view.View;
import android.widget.TextView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.viewTouchDelegate;
import com.xunda.lib.common.bean.ApkBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.H5Url;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.OnHttpResListener;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.MyArrowItemView;
import java.util.HashMap;
import butterknife.BindView;

public class MySet_AboutMe_Activity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.update_MyView)
    MyArrowItemView update_MyView;
    @BindView(R.id.service_Tv)
    TextView service_Tv;
    @BindView(R.id.privacy_Tv)
    TextView privacy_Tv;

    @BindView(R.id.tv_version)
    TextView tv_version;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_myset_aboutme;
    }

    @Override
    protected void initEvent() {
        service_Tv.setOnClickListener(this);
        privacy_Tv.setOnClickListener(this);
        update_MyView.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(service_Tv, 50);
        viewTouchDelegate.expandViewTouchDelegate(privacy_Tv, 50);

        String versionName = "当前版本" + AndroidUtil.getVersionName(mContext);
        tv_version.setText(versionName);
    }

    @Override
    protected void getServerData() {
        checkUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.service_Tv:
                jumpToWebView("用户协议", H5Url.H5UserPolicy);
                break;
            case R.id.privacy_Tv:
                jumpToWebView("隐私政策", H5Url.H5PrivatePolicy);
                break;
            case R.id.update_MyView:
                isUpdate();
                break;

        }
    }

    ApkBean apkObj;
    /**
     * 版本更新检测
     */
    private void checkUpdate() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("version", String.valueOf(AndroidUtil.getVersionName(mContext)));
        HttpSender sender = new HttpSender(HttpUrl.appVersion, "版本更新检测",
                baseMap, new OnHttpResListener() {
            @Override
            public void onComplete(String json, int status, String description, String data) {
                if (status == Constants.REQUEST_SUCCESS_CODE) {
                     apkObj = GsonUtil.getInstance().json2Bean(data, ApkBean.class);
                    if (apkObj != null) {
                        String tempIsForceUpdate = apkObj.getIs_force_update();//0不需要强制更新，1强制更新，2不需要更新
                        if (!StringUtil.isBlank(tempIsForceUpdate)) {
                            int isForceUpdate = Integer.parseInt(tempIsForceUpdate);
                            if (isForceUpdate != 2) {
                                update_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }
        }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void isUpdate(){
        if (apkObj != null) {
            String tempIsForceUpdate = apkObj.getIs_force_update();//0不需要强制更新，1强制更新，2不需要更新
            if (!StringUtil.isBlank(tempIsForceUpdate)) {
                int isForceUpdate = Integer.parseInt(tempIsForceUpdate);
                if (isForceUpdate != 2) {
                    skipAnotherActivity(MySet_AboutMe_Update.class);
                }else {
                    T.s("已是最新版本，无需更新",2000);
                }
            }
        }

    }

}