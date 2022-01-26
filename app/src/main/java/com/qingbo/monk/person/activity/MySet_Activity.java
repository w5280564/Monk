package com.qingbo.monk.person.activity;

import androidx.annotation.RequiresApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.login.activity.LoginActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.eventbus.SocketUnbindEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DataCleanManager;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.view.MyArrowItemView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import butterknife.BindView;

/**
 * 我的设置
 */
public class MySet_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.phone_MyView)
    MyArrowItemView phone_MyView;
    @BindView(R.id.wechatContent_Tv)
    TextView wechatContent_Tv;
    @BindView(R.id.about_MyView)
    MyArrowItemView about_MyView;
    @BindView(R.id.clean_MyView)
    MyArrowItemView clean_MyView;
    @BindView(R.id.quit_Tv)
    TextView quit_Tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myset;
    }

    @Override
    protected void initLocalData() {
        String username = PrefUtil.getUser().getUsername();
        phone_MyView.getTvContent().setText(username);

        String auth_nickname = PrefUtil.getUser().getAuth_nickname();
        if (TextUtils.isEmpty(auth_nickname)) {
            wechatContent_Tv.setText("空");
        }
        wechatContent_Tv.setText(auth_nickname);
    }

    @Override
    protected void initView() {

        setData();
    }

    @Override
    protected void initEvent() {
        clean_MyView.setOnClickListener(this);
        about_MyView.setOnClickListener(this);
        quit_Tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean_MyView:
                new TwoButtonDialogBlue(mActivity, "确定要清理缓存吗？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
                    @Override
                    public void onClickRight() {
                        DataCleanManager.clearAllCache(mActivity);
                        setData();
                    }

                    @Override
                    public void onClickLeft() {
                    }
                }).show();
                break;
            case R.id.about_MyView:
                skipAnotherActivity(MySet_AboutMe_Activity.class);
                break;

            case R.id.quit_Tv:
                new TwoButtonDialogBlue(mActivity, "确定退出登录吗？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
                    @Override
                    public void onClickRight() {
                        getQuit();
                    }

                    @Override
                    public void onClickLeft() {
                    }
                }).show();
                break;
        }

    }

    private void getQuit() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Login_Logout, "退出登录", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    EventBus.getDefault().post(new SocketUnbindEvent(SocketUnbindEvent.SocketUnbind));//解绑WebSocketService
                    PrefUtil.clearSharePrefInfo();
                    BaseApplication.getInstance().clearActivity();
                    skipAnotherActivity(LoginActivity.class);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void setData() {
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(mContext);
            clean_MyView.getTvContent().setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}