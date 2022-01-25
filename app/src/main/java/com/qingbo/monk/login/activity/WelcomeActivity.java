package com.qingbo.monk.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.WebSocketHelper;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue_No_Finish;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 注册完以后的欢迎页
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    public static void actionStart(Context context,String openid,int isFromType) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.putExtra("isFromType",isFromType);
        intent.putExtra("openid",openid);
        context.startActivity(intent);
    }

    public static void actionStart(Context context,int band_wx,int isFromType) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.putExtra("isFromType",isFromType);
        intent.putExtra("band_wx",band_wx);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        int isFromType = getIntent().getIntExtra("isFromType",0);//1来自绑定微信  2来自绑定手机号  3正常
        if (isFromType==1) {
            String openid = getIntent().getStringExtra("openid");
            if (!StringUtil.isBlank(openid)) {//说明第一次绑定微信
                if (!PrefUtil.isLogin()) {//首次直接使用微信登录，不是先手机号登录（未登录状态），才弹出绑定手机号弹窗
                    showBindPhoneNumberDialog(openid);
                }
            }
        }else if(isFromType==2){
            int band_wx = getIntent().getIntExtra("band_wx",0);
            if (band_wx==0) {
                showBindWechatDialog();
            }
        }
    }

    private void showBindPhoneNumberDialog(String openid) {
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this,"为了您在扫地僧获得更好的用户体验，请绑定手机号。","退出登录","去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
            @Override
            public void onClickRight() {
                BindPhoneNumberActivity.actionStart(mActivity,openid);
            }

            @Override
            public void onClickLeft() {
                getQuit();
            }
        });
        mDialog.show();
    }

    private void showBindWechatDialog() {
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this,"为了您在扫地僧获得更好的用户体验，请绑定微信。","退出登录","去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
            @Override
            public void onClickRight() {
                wechatThirdLogin();
            }

            @Override
            public void onClickLeft() {
                getQuit();
            }
        });
        mDialog.show();
    }


    private void getQuit() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Login_Logout, "退出登录", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    PrefUtil.clearSharePrefInfo();
                    BaseApplication.getInstance().clearActivity();
                    skipAnotherActivity(LoginActivity.class);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }



    @OnClick({R.id.tv_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_join:
                skipAnotherActivity(LoginMoreInfoActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }


}
