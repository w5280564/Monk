package com.qingbo.monk.login.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.HashMap;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunda.lib.common.bean.BaseUserBean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.WechatLoginEvent;
import com.xunda.lib.common.common.eventbus.WechatPayEvent;
import com.xunda.lib.common.common.http.H5Url;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    private boolean agreement;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initEvent() {
        cbAgreement.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initView() {
        registerEventBus();
    }

    /**
     * 微信登录成功结果回调
     *
     * @param event
     */
    @Subscribe
    public void onWechatLoginEvent(WechatLoginEvent event) {
        if (event.event_type == WechatLoginEvent.WECHAT_Login_RESULT) {
            finish();
        }
    }

    @OnClick({R.id.ll_wechat_login, R.id.ll_phone_login, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_login:
                if (!agreement) {
                    T.sl("请先阅读并同意下方服务条款");
                    return;
                }

                wechatThirdLogin();

                break;
            case R.id.ll_phone_login:

                if (!agreement) {
                    T.sl("请先阅读并同意下方服务条款");
                    return;
                }

                skipAnotherActivity(LoginWithCodeActivity.class);
                break;
            case R.id.tv_agreement:
                jumpToWebView("用户协议", H5Url.H5UserPolicy);
                break;
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            agreement = true;
        } else {
            agreement = false;
        }
    }


    @Override
    public void onBackPressed() {
        closeApp();
    }

}