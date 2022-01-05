package com.qingbo.monk.question.activity;


import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyExitGroupOrMoneyActivity extends BaseActivity {
    @BindView(R.id.tv_toast1)
    TextView tvToast1;
    @BindView(R.id.tv_toast2)
    TextView tvToast2;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private String back;//1可以退款0不可以退款
    private String id;//社群id


    public static void actionStart(Context context, String back, String id) {
        Intent intent = new Intent(context, ApplyExitGroupOrMoneyActivity.class);
        intent.putExtra("back",back);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_group_money;
    }

    @Override
    protected void initLocalData() {
        back = getIntent().getStringExtra("back");
        id = getIntent().getStringExtra("id");
        if ("1".equals(back)){//申请退款
            tvToast1.setText("申请确定后宇宙币自动退回APP钱包");
            tvConfirm.setText("申请确认");
        }else{
            tvToast1.setText("申请确定后宇宙币概不退还");
            tvConfirm.setText("退出社群");
        }
    }

    @OnClick(R.id.tv_confirm)
    public void onClick() {
        showCancelRoleDialog();
    }

    private void showCancelRoleDialog() {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(this,"确定退出该社群吗","取消","确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                exitGroup();
            }

            @Override
            public void onClickLeft() {

            }
        });
        mDialog.show();
    }

    /**
     * 退出社群
     */
    private void exitGroup() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.joinGroup, "退出社群", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("操作成功");
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.EXIT_GROUP));
                            finish();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }
}
