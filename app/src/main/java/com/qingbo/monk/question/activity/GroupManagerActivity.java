package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.view.MySwitchItemView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群管理
 */
public class GroupManagerActivity extends BaseActivity {


    @BindView(R.id.disturb_Switch)
    MySwitchItemView disturbSwitch;

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupManagerActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initLocalData() {

    }

    @Override
    protected void getServerData() {

    }


    @OnClick({R.id.arrowItemView_type, R.id.arrowItemView_manager, R.id.arrowItemView_partner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowItemView_type:
                skipAnotherActivity(GroupManagerSetCostActivity.class);
                break;
            case R.id.arrowItemView_manager:
                break;
            case R.id.arrowItemView_partner:
                break;
        }
    }
}
