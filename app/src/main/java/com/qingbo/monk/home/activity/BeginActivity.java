package com.qingbo.monk.home.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.FragmentActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.MonkApplication;
import com.qingbo.monk.login.activity.LoginActivity;
import com.qingbo.monk.login.activity.WelcomeActivity;
import com.qingbo.monk.webview.WebviewActivity;
import com.xunda.lib.common.common.http.H5Url;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.dialog.PrivacyPolicyDialog;
import com.xunda.lib.common.dialog.PrivacyPolicyDialogAgain;

/**
 * 启动页面
 */
public class BeginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        judgePrivacyPolicy();
        setStatusBar();
    }



    private void judgePrivacyPolicy() {
        int isAgree = SharePref.local().getIsAgreePrivacyPolicy();
        if(isAgree==1){//已同意过隐私政策
            goToWhere();
        }else{//未同意>弹窗
            showPrivacyPolicyDialog();
        }
    }



    /**
     * 设置状态栏
     */
    private void setStatusBar() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(false)
                .init();
    }




    private void showPrivacyPolicyDialog() {
        PrivacyPolicyDialog mPrivacyPolicyDialog = new PrivacyPolicyDialog(this, new PrivacyPolicyDialog.OnPrivacyPolicyChooseListener() {
            @Override
            public void notAgree() {
                showPrivacyPolicyDialogAgain();
            }

            @Override
            public void agree() {
                SharePref.local().setIsAgreePrivacyPolicy(1);
                MonkApplication.getInstance().initPrivatePolicySDK();
                goToWhere();
            }

            @Override
            public void clickPrivacyPolicy() {
                jumpToWebView("隐私政策", H5Url.H5PrivatePolicy);
            }

            @Override
            public void clickAgreement() {
                jumpToWebView("用户协议", H5Url.H5UserPolicy);
            }
        });
        mPrivacyPolicyDialog.show();
    }

    /**
     * 跳转到默认H5展示页面
     */
    protected void jumpToWebView(String title, String url) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void showPrivacyPolicyDialogAgain() {
        PrivacyPolicyDialogAgain mPrivacyPolicyDialog = new PrivacyPolicyDialogAgain(this, new PrivacyPolicyDialogAgain.OnPrivacyPolicyAgainChooseListener() {
            @Override
            public void exit() {
                closeApp();
            }

            @Override
            public void agree() {
                SharePref.local().setIsAgreePrivacyPolicy(1);
                MonkApplication.getInstance().initPrivatePolicySDK();
                goToWhere();
            }
        });
        mPrivacyPolicyDialog.show();
    }



    /**
     * 关闭app
     */
    protected void closeApp() {
        MonkApplication.getInstance().clearActivity();
        finish();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(getPackageName());
    }




    @Override
    public void onBackPressed() {

    }


    /**
     * 根据情况跳转
     */
    private void goToWhere() {
        if (PrefUtil.isLogin()) {
            jumpToMainActivity();
        }else{
            jumpToLoginActivity();
        }
        finish();
    }


    /**
     * 跳转到首页
     */
    private void jumpToMainActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
//        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转到首页
     */
    private void jumpToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
