package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.StockCombinationListBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

public class PersonDetail_Fragment extends BaseFragment {
    private String nickname;

    /**
     * @param
     * @return
     */
    public static PersonDetail_Fragment newInstance(String nickname) {
        Bundle args = new Bundle();
        args.putString("nickname", nickname);
        PersonDetail_Fragment fragment = new PersonDetail_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
         nickname = getArguments().getString("nickname");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.persondetail_fragment;
    }

    @Override
    protected void getServerData() {
        getListData(true);
    }

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Postion, "人物", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    StockCombinationListBean stockCombinationListBean = GsonUtil.getInstance().json2Bean(json_data, StockCombinationListBean.class);
                    if (stockCombinationListBean != null) {
//                        handleSplitListData(stockCombinationListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }



}
