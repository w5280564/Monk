package com.qingbo.monk.login.fragment;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * 登录填写更多信息第2步
 */
public class StepTwoFragmentLogin extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_two_fragment_login;
    }


    @OnClick({R.id.tv_back,R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ZERO));
                break;
            case R.id.tv_next:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO));
                break;

        }
    }
}
