package com.qingbo.monk.person.activity;

import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;

import java.util.HashMap;

/**
 * 我的钱包-提现
 */
public class MyWallet_Withdrawal extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.wallet_withdrawal;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.User_wallet, "提现", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
//                if (refresh_layout.isRefreshing()) {
//                    refresh_layout.setRefreshing(false);
//                }
//                if (code == Constants.REQUEST_SUCCESS_CODE) {
//                    MyWallet_Bean myWallet_bean = GsonUtil.getInstance().json2Bean(json_data, MyWallet_Bean.class);
//                    if (myWallet_bean != null){
//
//                    }
//                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
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
}