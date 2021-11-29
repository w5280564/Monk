package com.qingbo.monk.login.activity;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

/**
 * 选择行业
 */
public class ChooseIndustryActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
       return R.layout.activity_choose_industry;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void getServerData() {
        getIndustryList();
    }


    /**
     * 获取行业列表
     */
    private void getIndustryList() {
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