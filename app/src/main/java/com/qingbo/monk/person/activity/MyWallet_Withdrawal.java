package com.qingbo.monk.person.activity;

import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyWallet_Balance_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的钱包-提现
 */
public class MyWallet_Withdrawal extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.balance_Tv)
    TextView balance_Tv;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.money_Et)
    EditText money_Et;


    @Override
    protected int getLayoutId() {
        return R.layout.wallet_withdrawal;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBalanceData();
    }

    @Override
    protected void initEvent() {
        tv_next.setOnClickListener(this);
        setTextWatcher();
    }

    MyWallet_Balance_Bean myWallet_balance_bean;

    private void getBalanceData() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Wallet_Balance, "可提现余额", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myWallet_balance_bean = GsonUtil.getInstance().json2Bean(json_data, MyWallet_Balance_Bean.class);
                    if (myWallet_balance_bean != null) {

                        String format = String.format("你的余额中有￥%1$s 可使用该方式体现", myWallet_balance_bean.getAmount());
                        String findStr = "￥" + myWallet_balance_bean.getAmount();
                        SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_EFB46E), format, findStr);

                        balance_Tv.setText(searchChange);
                        max = Double.parseDouble(myWallet_balance_bean.getAmount());
                    }
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + (CharSequence) value);
        }
    }

    @Override
    public void onRightClick() {
        skipAnotherActivity(MyWallet_DetailedList.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                postWithdrawalData();
                break;
        }
    }


    private void postWithdrawalData() {
        if (myWallet_balance_bean == null) {
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("", myWallet_balance_bean.getAmount() + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Wallet_Withdrawal, "提现", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(msg, 3000);

                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private double min = 0;
    private double max = 0;

    public void setTextWatcher() {
        money_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {
                    //从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            double num = Double.parseDouble(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                s = String.valueOf(max);//如果大于max，则内容为max
                                money_Et.setText(s);
                                T.s("提现金额不能超过" + max + "元", 3000);
                            } else if (num < min) {
                                s = String.valueOf(min);//如果小于min,则内容为min
                                money_Et.setText(s);
                            }
                            money_Et.setSelection(s.length());
                        } catch (NumberFormatException e) {
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return;
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}