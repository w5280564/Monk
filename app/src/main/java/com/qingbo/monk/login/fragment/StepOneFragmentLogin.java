package com.qingbo.monk.login.fragment;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.BigAreaCodeBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 登录填写更多信息第1步
 */
public class StepOneFragmentLogin extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment_login;
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

    @Override
    protected void getServerData() {
        getIndustryListList();
    }


    /**
     * 获取行业列表
     */
    private void getIndustryListList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.industryList, "获取行业列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {

                        } else {
                            T.ss(msg);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }
}
