package com.qingbo.monk.person.activity;

import static com.xunda.lib.common.common.utils.StringUtil.isNumber;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyWallet_Bean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.bean.WalletDetailBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.MyArrowItemView;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 交易详情
 */
public class MyWallet_DetailedList_Detail extends BaseActivity {
    @BindView(R.id.style_MyView)
    MyArrowItemView style_MyView;
    @BindView(R.id.from_MyView)
    MyArrowItemView from_MyView;
    @BindView(R.id.time_MyView)
    MyArrowItemView time_MyView;
    @BindView(R.id.order_MyView)
    MyArrowItemView order_MyView;
    @BindView(R.id.to_MyView)
    MyArrowItemView to_MyView;
    @BindView(R.id.mode_MyView)
    MyArrowItemView mode_MyView;
    @BindView(R.id.state_MyView)
    MyArrowItemView state_MyView;
    @BindView(R.id.remarks_MyView)
    MyArrowItemView remarks_MyView;
    @BindView(R.id.moneyCount_Tv)
    TextView moneyCount_Tv;
    @BindView(R.id.cost_Tv)
    TextView cost_Tv;


    private String orderId;

    public static void actionStart(Context context, String orderId) {
        Intent intent = new Intent(context, MyWallet_DetailedList_Detail.class);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mywallet_detailedlist_detail;
    }

    @Override
    protected void initLocalData() {
        orderId = getIntent().getStringExtra("orderId");
    }

    @Override
    protected void initView() {
//        style_MyView.getTvContent().setGravity(Gravity.START);
//        from_MyView.getTvContent().setGravity(Gravity.START);
//        time_MyView.getTvContent().setGravity(Gravity.START);
//        order_MyView.getTvContent().setGravity(Gravity.START);
//        to_MyView.getTvContent().setGravity(Gravity.START);
//        mode_MyView.getTvContent().setGravity(Gravity.START);
//        state_MyView.getTvContent().setGravity(Gravity.START);
        remarks_MyView.getTvContent().setGravity(Gravity.CENTER_VERTICAL);
        remarks_MyView.getTvContent().setMaxLines(2);
    }

    @Override
    protected void getServerData() {
        getUserData();
    }


    private void getUserData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("orderId",orderId);
        HttpSender httpSender = new HttpSender(HttpUrl.Wallet_Order_Detail, "订单详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
//                if (refresh_layout.isRefreshing()) {
//                    refresh_layout.setRefreshing(false);
//                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    WalletDetailBean walletDetailBean = GsonUtil.getInstance().json2Bean(json_data, WalletDetailBean.class);
                    if (walletDetailBean != null) {
//                        moneyCount_Tv.setText(walletDetailBean.getMoney());
                        setMoney(walletDetailBean.getMoney(),moneyCount_Tv);
//                        cost_Tv.setText("鹅先知手续费"+walletDetailBean.);
                        String s = styleString(walletDetailBean.getType());
                        style_MyView.getTvContent().setText(s);
                        from_MyView.getTvContent().setText(walletDetailBean.getPayUserName());
                        time_MyView.getTvContent().setText(walletDetailBean.getCreateTime());
                        order_MyView.getTvContent().setText(walletDetailBean.getTradeNo());
                        to_MyView.getTvContent().setText(walletDetailBean.getBenefitUserName());
                        mode_MyView.getTvContent().setText(walletDetailBean.getPayType());
                        String s1 = payStatus(walletDetailBean.getStatus());
                        state_MyView.getTvContent().setText(s1);
                        remarks_MyView.getTvContent().setText(walletDetailBean.getTradeDesc());
                    }
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     *
     * @param style SQ付费,TK退款，CZ充值 SR收入
     * @return
     */
    private String styleString(String style){
        String styleName ="";
        if (TextUtils.equals(style,"SQ")) {
            styleName = "付费";
        }else if (TextUtils.equals(style,"TK")) {
            styleName = "退款";
        }else if (TextUtils.equals(style,"CZ")) {
            styleName = "充值";
        }else if (TextUtils.equals(style,"SR")) {
            styleName = "收入";
        }
        return styleName;
    }
    /**
     *
     * @param style 0结算中 1已结算 2已退款 3已提现
     * @return
     */
    private String payStatus(String style){
        String styleName ="";
        if (TextUtils.equals(style,"0")) {
            styleName = "结算中 ";
        }else if (TextUtils.equals(style,"1")) {
            styleName = "已结算 ";
        }else if (TextUtils.equals(style,"2")) {
            styleName = "已退款";
        }else if (TextUtils.equals(style,"3")) {
            styleName = "已提现";
        }
        return styleName;
    }

    private void setMoney(String money,TextView countTv){
        boolean number = isNumber(money);
        if (number) {
            double v = Double.parseDouble(money);
            if (v > 0) {
                countTv.setText("+" + money);
            } else if (v < 0) {
                countTv.setText(money + "");
            }
        }
    }

}