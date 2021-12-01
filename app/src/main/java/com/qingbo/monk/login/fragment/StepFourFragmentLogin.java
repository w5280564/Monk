package com.qingbo.monk.login.fragment;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.Topic_Bean;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 登录填写更多信息第4步
 */
public class StepFourFragmentLogin extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_four_fragment_login;
    }


    @OnClick({R.id.tv_back,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO));
                break;
            case R.id.tv_save:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_FOUR));
                break;

        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getEdit_Info();
    }

    private void getEdit_Info() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("get_resource","");
        requestMap.put("interested","");
        HttpSender httpSender = new HttpSender(HttpUrl.interestedList, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                Topic_Bean topic_bean = GsonUtil.getInstance().json2Bean(json_data, Topic_Bean.class);

            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

}
