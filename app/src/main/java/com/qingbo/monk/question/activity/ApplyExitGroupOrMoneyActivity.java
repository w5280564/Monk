package com.qingbo.monk.question.activity;


import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
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

    }
}
