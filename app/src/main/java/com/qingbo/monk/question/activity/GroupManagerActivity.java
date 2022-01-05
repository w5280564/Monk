package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMoreInfoBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.view.MySwitchItemView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群管理
 */
public class GroupManagerActivity extends BaseActivity {
    @BindView(R.id.theme_Switch)
    MySwitchItemView theme_Switch;

    private String id,shequn_fee;

    public static void actionStart(Context context, GroupMoreInfoBean sheQunBean) {
        Intent intent = new Intent(context, GroupManagerActivity.class);
        intent.putExtra("sheQunBean", sheQunBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager;
    }


    @Override
    protected void initEvent() {
        theme_Switch.getSwitch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeSet();
            }
        });
    }

    /**
     * 设置预览主题
     */
    private void themeSet() {
        boolean isCheck = theme_Switch.getSwitch().isChecked();
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        baseMap.put("status", isCheck?"1":"2");//1是可以预览 2是不可以预览
        HttpSender sender = new HttpSender(HttpUrl.themeSet, "设置预览主题", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            theme_Switch.getSwitch().setChecked(isCheck);
                            //todo
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }


    @Override
    protected void initLocalData() {
        GroupMoreInfoBean sheQunBean = (GroupMoreInfoBean) getIntent().getSerializableExtra("sheQunBean");
        if (sheQunBean!=null) {
            id = sheQunBean.getSqId();
            shequn_fee = sheQunBean.getShequnFee();
            String showTheme = sheQunBean.getShowTheme();//	1是可以预览 2是不可以预览
            if ("1".equals(showTheme)) {
                theme_Switch.getSwitch().setChecked(true);
            }else{
                theme_Switch.getSwitch().setChecked(false);
            }
        }
    }



    @OnClick({R.id.arrowItemView_type, R.id.arrowItemView_manager, R.id.arrowItemView_partner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowItemView_type:
                GroupManagerSetCostActivity.actionStart(mActivity,id,shequn_fee);
                break;
            case R.id.arrowItemView_manager:
                GroupManagerOrPartnerListActivity.actionStart(mActivity,id,"3");//3是管理员
                break;
            case R.id.arrowItemView_partner:
                GroupManagerOrPartnerListActivity.actionStart(mActivity,id,"4");//4是合伙人
                break;
        }
    }
}
