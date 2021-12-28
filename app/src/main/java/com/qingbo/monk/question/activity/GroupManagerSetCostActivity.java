package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.dialog.InputStringDialog;
import com.xunda.lib.common.view.MyArrowItemView;
import com.xunda.lib.common.view.MySwitchItemView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群管理_设置付费方式
 */
public class GroupManagerSetCostActivity extends BaseActivity {


    @BindView(R.id.disturb_Switch)
    MySwitchItemView disturbSwitch;
    @BindView(R.id.arrowItemView_cost)
    MyArrowItemView arrowItemView_cost;

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupManagerSetCostActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_set_cost;
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


    @OnClick({R.id.arrowItemView_cost,R.id.disturb_Switch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowItemView_cost:
                String cost = arrowItemView_cost.getTvContent().getText().toString();

                showDialog(cost);
                break;
            case R.id.disturb_Switch:


                break;

        }
    }

    private void showDialog(String cost) {
        InputStringDialog mInputStringDialog = new InputStringDialog(this, cost, new InputStringDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String content) {
                arrowItemView_cost.getTvContent().setText(content);
            }
        });
        mInputStringDialog.show();
    }
}
