package com.qingbo.monk.person.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyWallet_Bean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

public class MyWallet_Activity extends BaseActivity {

    @BindView(R.id.smallChange_Tv)
    TextView smallChange_Tv;
    @BindView(R.id.withdrawal_Tv)
    TextView withdrawal_Tv;
    @BindView(R.id.settlement)
    TextView settlement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

    private void getUserData() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.User_wallet, "我的余额", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
//                if (refresh_layout.isRefreshing()) {
//                    refresh_layout.setRefreshing(false);
//                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    MyWallet_Bean myWallet_bean = GsonUtil.getInstance().json2Bean(json_data, MyWallet_Bean.class);
                    if (myWallet_bean != null){
                        originalValue(myWallet_bean.getTotal(), "0.00","¥ ", smallChange_Tv);
                        originalValue(myWallet_bean.getCan(), "0.00","¥", withdrawal_Tv);
                        originalValue(myWallet_bean.getWait(), "0.00","¥", settlement);

                    }
                }
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




}