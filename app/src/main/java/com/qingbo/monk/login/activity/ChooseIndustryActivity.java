package com.qingbo.monk.login.activity;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.BaseIndustryBean;
import com.qingbo.monk.bean.IndustryBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择行业
 */
public class ChooseIndustryActivity extends BaseActivity {
    private List<IndustryBean> mList = new ArrayList<>();



    @Override
    protected int getLayoutId() {
       return R.layout.activity_choose_industry;
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
                            BaseIndustryBean mObj = GsonUtil.getInstance().json2Bean(json_data, BaseIndustryBean.class);
                            handleObj(mObj);
                        } else {
                            T.ss(msg);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleObj(BaseIndustryBean mObj) {
        if (mObj!=null) {
            if (!ListUtils.isEmpty(mObj.getList())) {
                mList.addAll(mObj.getList());
            }
        }
    }


}