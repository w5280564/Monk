package com.qingbo.monk.login.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.HashMap;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.bean.BaseUserBean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.H5Url;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登陆页面
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



    @OnClick({R.id.ll_wechat_login, R.id.ll_phone_login, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_login:
                if (!agreement) {
                    T.sl("请先阅读并同意下方服务条款");
                    return;
                }

//                thirdLogin();
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


    private void thirdLogin() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("osType", "2");//1苹果2安卓
        baseMap.put("equipmentName", android.os.Build.BRAND + "" + android.os.Build.MODEL);
        baseMap.put("meid", StringUtil.getIMEI());
        baseMap.put("version", android.os.Build.VERSION.RELEASE);
        HttpSender sender = new HttpSender(HttpUrl.mobileLogin, "第三方登录", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("登录成功");
                            BaseUserBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseUserBean.class);
                            saveUserInfo(obj);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendPost();
    }






    /**
     * 保存用户信息
     *
     * @param baseUserBean 用户对象
     */
    private void saveUserInfo(BaseUserBean baseUserBean) {
        if (baseUserBean!=null) {
            UserBean userObj = baseUserBean.getInfo();
            if (userObj==null) {
                return;
            }

            PrefUtil.saveUser(userObj,baseUserBean.getAccessToken());
            skipAnotherActivity(WelcomeActivity.class);
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



}