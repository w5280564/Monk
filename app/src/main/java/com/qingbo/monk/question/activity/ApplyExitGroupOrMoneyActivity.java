package com.qingbo.monk.question.activity;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMoreInfoBean;
import com.qingbo.monk.bean.GroupQuit_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
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
    @BindView(R.id.groupTitle_Tv)
    TextView groupTitle_Tv;
    @BindView(R.id.groupTip_Tv)
    TextView groupTip_Tv;

    private String back;
    private String id;
    private String shequn_fee;
    private GroupMoreInfoBean sheQunBean;

    /**
     * @param context
     * @param back       1可以退款0不可以退款
     * @param id         社群id
     * @param shequn_fee 退群费
     */
    public static void actionStart(Context context, String back, String id, String shequn_fee, GroupMoreInfoBean sheQunBean) {
        Intent intent = new Intent(context, ApplyExitGroupOrMoneyActivity.class);
        intent.putExtra("back", back);
        intent.putExtra("id", id);
        intent.putExtra("shequn_fee", shequn_fee);
        intent.putExtra("sheQunBean", sheQunBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_group_money;
    }

    @Override
    protected void initLocalData() {
         sheQunBean = (GroupMoreInfoBean) getIntent().getSerializableExtra("sheQunBean");
        back = getIntent().getStringExtra("back");
        id = getIntent().getStringExtra("id");
        shequn_fee = getIntent().getStringExtra("shequn_fee");
        if ("1".equals(back)) {//申请退款
            tvToast1.setText("申请确定后入群费用自动退回至APP钱包");
            tvConfirm.setText("申请确认");
        } else {
            tvToast1.setText("申请确定后入群费用概不退还");
            tvConfirm.setText("退出社群");
            if (sheQunBean != null) {
                String role = sheQunBean.getRole();
                if (role.equals("3")) {//1管理员2合伙人0一般用户3群主
                    tvConfirm.setText("解散社群");
                    groupTitle_Tv.setText("您正在进行解散社群操作");
                    groupTip_Tv.setText("解散群后：");
                    tvToast2.setText("申请确定后直接解散社群");
                }
            }
        }


    }

    @OnClick(R.id.tv_confirm)
    public void onClick() {
        showCancelRoleDialog();
    }

    private void showCancelRoleDialog() {
        String format = "确定退出该社群吗";
        if (sheQunBean.getRole().equals("3")) {
             format = "确定解散该社群吗";
        }
        if (TextUtils.equals(back, "1")) {
            double v = Double.parseDouble(shequn_fee);
            if (v > 0) {
                format = String.format("退出社群将返还%1$s到钱包", shequn_fee);
            }

        }
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(this, format, "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
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
        baseMap.put("client", "1");
        HttpSender sender = new HttpSender(HttpUrl.joinGroup, "退出社群", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("操作成功");
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.EXIT_GROUP));
                            finish();
                        } else if (code == 45661) {//3天内的付费用户退出社群
                            GroupQuit_Bean groupQuit_bean = GsonUtil.getInstance().json2Bean(json_root, GroupQuit_Bean.class);
                            leaveGroup();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    /**
     * 三天内付费用户退款
     */
    private void leaveGroup() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.Leave_Group, "退款", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("退款成功");
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.EXIT_GROUP));
                            finish();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }


}
