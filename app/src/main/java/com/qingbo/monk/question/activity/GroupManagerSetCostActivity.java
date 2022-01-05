package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.dialog.InputStringDialog;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.view.MyArrowItemView;
import com.xunda.lib.common.view.MySwitchItemView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群管理_设置付费方式
 */
public class GroupManagerSetCostActivity extends BaseActivity {


    @BindView(R.id.free_Switch)
    MySwitchItemView free_Switch;
    @BindView(R.id.arrowItemView_cost)
    MyArrowItemView arrowItemView_cost;
    private String id,fee="";

    public static void actionStart(Context context, String id,String shequn_fee) {
        Intent intent = new Intent(context, GroupManagerSetCostActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("shequn_fee", shequn_fee);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_set_cost;
    }


    @Override
    protected void initEvent() {
        free_Switch.getSwitch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (free_Switch.getSwitch().isChecked()) {
                    feeSet("0");
                }else{
                    setCostFee(2);
                }
            }
        });
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        fee = getIntent().getStringExtra("shequn_fee");
        if ("0".equals(fee)) {
            free_Switch.getSwitch().setChecked(true);
            arrowItemView_cost.setVisibility(View.GONE);
        }else{
            free_Switch.getSwitch().setChecked(false);
            arrowItemView_cost.setVisibility(View.VISIBLE);
            arrowItemView_cost.getTvContent().setText(fee+"元");
        }
    }



    @OnClick({R.id.arrowItemView_cost})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowItemView_cost:
                setCostFee(1);
                break;
        }



    }

    private void setCostFee(int type) {
        showFeeDialog(fee,type);
    }


    private void showFeeDialog(String cost,int type) {
        InputStringDialog mInputStringDialog = new InputStringDialog(this, cost, new InputStringDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String content) {
                feeSet(content);
            }
        });
        mInputStringDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (type==2) {
                    free_Switch.getSwitch().setChecked(true);
                }
            }
        });
        mInputStringDialog.show();
    }


    /**
     * 入群费用设置
     */
    private void feeSet(String content) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        baseMap.put("fee", content);
        HttpSender sender = new HttpSender(HttpUrl.feeSet, "入群费用设置", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            if ("0".equals(content)) {
                                fee = "";
                                free_Switch.getSwitch().setChecked(true);
                                arrowItemView_cost.setVisibility(View.GONE);
                            }else{
                                fee = content;
                                free_Switch.getSwitch().setChecked(false);
                                arrowItemView_cost.setVisibility(View.VISIBLE);
                                arrowItemView_cost.getTvContent().setText(fee+"元");
                            }
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }
}
