package com.qingbo.monk.login.activity;

import android.view.View;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.tuo.customview.VerificationCodeView;
import com.xunda.lib.common.bean.NanUserBean;
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
    private String phoneNumber;
    private boolean fromExitAct;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_code;
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNUmberView.setText("+"+phoneNumber);


    }


    @Override
    protected void getServerData() {
//        getSmsCode(phoneNumber,tvGetCode);
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
                getSmsCode(phoneNumber,tvGetCode);
                break;
        }
    }


    public void finishInPut(String smsCode) {
        skipAnotherActivity(WelcomeActivity.class);
//        codeLogin(smsCode);
    }


    /**
     * 验证码登录
     * @param smsCode
     */
    private void codeLogin(String smsCode) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("phoneNum", phoneNumber);
        baseMap.put("smsCode", smsCode);
        baseMap.put("osType", "2");//1/2：手机/平板
        baseMap.put("equipmentName", android.os.Build.BRAND + "" + android.os.Build.MODEL);
        baseMap.put("meid", StringUtil.getIMEI());
        baseMap.put("version", android.os.Build.VERSION.RELEASE);
        HttpSender sender = new HttpSender(HttpUrl.accountLogin, "验证码登录", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("登录成功");
                            NanUserBean obj = GsonUtil.getInstance().json2Bean(json_data, NanUserBean.class);
                            if (obj != null) {
                                saveUserInfo(obj);
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
    private void saveUserInfo(NanUserBean user) {
        PrefUtil.saveUser(user);
        if (!StringUtil.isBlank(user.getPhoneNum())) {//单独保存手机号
            SharePref.local().setUserPhone(user.getPhoneNum());
        }
    }






}