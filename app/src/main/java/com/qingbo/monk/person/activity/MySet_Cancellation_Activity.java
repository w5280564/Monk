package com.qingbo.monk.person.activity;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.login.activity.LoginActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.SocketUnbindEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MySet_Cancellation_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.cancellation_Tv)
    TextView cancellation_Tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myset_cancellation;
    }


    @Override
    protected void initEvent() {
        cancellation_Tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancellation_Tv:
                new TwoButtonDialogBlue(mActivity, "确定要注销当前账户吗？", "暂不注销", "确定注销", new TwoButtonDialogBlue.ConfirmListener() {
                    @Override
                    public void onClickRight() {
                        getCancellation();
                    }
                    @Override
                    public void onClickLeft() {
                    }
                }).show();
                break;
        }
    }

    /**
     * 注销账户
     */
    private void getCancellation() {
        HashMap<String, String> requestMap = new HashMap<>();
        String id = PrefUtil.getUser().getId();
        requestMap.put("id",id);
        HttpSender httpSender = new HttpSender(HttpUrl.User_AwayOver, "注销账户", requestMap, new MyOnHttpResListener() {
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
        httpSender.sendPost();
    }


}