package com.qingbo.monk.login.fragment;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.login.activity.GetPhoneCodeStepTwoActivity;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * 登录填写更多信息第1步
 */
public class StepOneFragmentLogin extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment;
    }


    @OnClick({R.id.arrowItemView_industry,R.id.arrowItemView_year,R.id.arrowItemView_city,R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.arrowItemView_industry:

                break;
            case R.id.arrowItemView_year:

                break;
            case R.id.arrowItemView_city:

                break;
            case R.id.tv_next:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE));
                break;

        }
    }
}
