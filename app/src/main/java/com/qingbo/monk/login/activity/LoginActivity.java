package com.qingbo.monk.login.activity;

import android.view.View;
import java.util.HashMap;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.bean.NanUserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.H5Url;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import butterknife.OnClick;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity{


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }






    @OnClick({R.id.ll_wechat_login, R.id.ll_phone_login, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_login:
                login();
                break;
            case R.id.ll_phone_login:
//                login();
                break;
            case R.id.tv_agreement:
                jumpToWebView("用户协议", H5Url.H5UserPolicy);
                break;
        }
    }


    private void login() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("osType", "2");//1苹果2安卓
        baseMap.put("equipmentName", android.os.Build.BRAND + "" + android.os.Build.MODEL);
        baseMap.put("meid", StringUtil.getIMEI());
        baseMap.put("version", android.os.Build.VERSION.RELEASE);


        HttpSender sender = new HttpSender(HttpUrl.accountLogin, "账号密码登录", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleSuccessData(json_data);
                        } else {
                            T.ss(msg);
                        }

                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendPost();
    }



    private void handleSuccessData(String json_data) {
        NanUserBean obj = GsonUtil.getInstance().json2Bean(json_data, NanUserBean.class);
        if (obj != null) {
            saveUserInfo(obj);
            login();
        }
    }




    /**
     * 保存用户信息
     *
     * @param user 用户对象
     */
    private void saveUserInfo(NanUserBean user) {
        PrefUtil.saveUser(user);
    }








}