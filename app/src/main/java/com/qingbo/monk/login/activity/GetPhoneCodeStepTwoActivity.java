package com.qingbo.monk.login.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.tuo.customview.VerificationCodeView;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.CountDownTextView;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 获取验证码第二步
 */
public class GetPhoneCodeStepTwoActivity extends BaseActivity {
    @BindView(R.id.get_codePhoneNumber)
    TextView phoneNUmberView;
    @BindView(R.id.tv_getCode)
    CountDownTextView tvGetCode;
    @BindView(R.id.et_code)
    VerificationCodeView mCodeView;
    private String area_code,phoneNumber;
    private boolean fromExitAct;


    public static void actionStart(Context context,String area_code,String phoneNumber) {
        Intent intent = new Intent(context, GetPhoneCodeStepTwoActivity.class);
        intent.putExtra("area_code",area_code);
        intent.putExtra("phoneNumber",phoneNumber);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_code;
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
        area_code = getIntent().getStringExtra("area_code");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNUmberView.setText("短信已发送至+"+phoneNumber);
    }


    @Override
    protected void getServerData() {
        getSmsCode(area_code,phoneNumber,tvGetCode);
    }

    @Override
    protected void initEvent() {
        mCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if(mCodeView.getInputContent().length()>=6){
                    finishInPut(mCodeView.getInputContent());
                }
            }

            @Override
            public void deleteContent() {
            }
        });
    }

    @OnClick({R.id.tv_getCode})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_getCode:
                getSmsCode(area_code,phoneNumber,tvGetCode);
                break;
        }
    }


    public void finishInPut(String smsCode) {
        codeLogin(smsCode);
    }


    /**
     * 手机号登录
     * @param smsCode
     */
    private void codeLogin(String smsCode) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("mobile", phoneNumber);
        baseMap.put("code", smsCode);
        HttpSender sender = new HttpSender(HttpUrl.mobileLogin, "手机号登录", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("登录成功");
                            UserBean obj = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                            if (obj != null) {
                                saveUserInfo(obj);
                            }else{
                                skipAnotherActivity(WelcomeActivity.class);
                            }
                        } else {
                            T.ss(msg);
                        }

                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    /**
     * 保存用户信息
     *
     * @param user 用户对象
     */
    private void saveUserInfo(UserBean user) {
        PrefUtil.saveUser(user);
        if (!StringUtil.isBlank(user.getPhoneNum())) {//单独保存手机号
            SharePref.local().setUserPhone(user.getPhoneNum());
        }
        skipAnotherActivity(WelcomeActivity.class);
    }






}