package com.qingbo.monk.login.activity;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.H5Url;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.CountDownTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码登录
 */
public class LoginWithCodeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_number_before)
    TextView tv_number_before;
    @BindView(R.id.et_phoneNumber)
    EditText et_phoneNumber;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_agreement)
    TextView tv_agreement;
    private boolean agreement;
    private ActivityResultLauncher mActivityResultLauncher;
    private String area_code = "86";



    @Override
    protected int getLayoutId() {
       return R.layout.activity_login_with_code;
    }



    @Override
    protected void initEvent() {
        cbAgreement.setOnCheckedChangeListener(this);
    }


    @Override
    protected void initView() {
        tv_number_before.setText("+"+area_code);
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result!=null) {
                    int resultCode = result.getResultCode();
                    if (resultCode==RESULT_OK) {
                        area_code = result.getData().getStringExtra("area_code");
                        tv_number_before.setText("+"+area_code);
                    }
                }
            }
        });

        setSe(tv_agreement);
    }





    @OnClick({R.id.ll_area_code,R.id.tv_send_code,R.id.iv_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_area_code:
                Intent intent = new Intent(mActivity, AreaCodeListActivity.class);
                mActivityResultLauncher.launch(intent);
                break;
            case R.id.tv_send_code:
                String phoneNumber = StringUtil.getEditText(et_phoneNumber);

                if (StringUtil.isBlank(phoneNumber)) {
                    T.ss("请输入手机号");
                    return;
                }

                if (!agreement) {
                    T.sl("请先阅读并同意下方服务条款");
                    return;
                }

                getSmsCodeLogin(phoneNumber);
                break;
            case R.id.iv_wechat:
                wechatThirdLogin();
                break;

        }
    }

    /**
     * 获取验证码
     */
    private void getSmsCodeLogin(String phoneNumber) {
        HashMap<String, String> baseMap = new HashMap<String, String>();
        baseMap.put("area", area_code);
        baseMap.put("mobile", phoneNumber);
        HttpSender sender = new HttpSender(HttpUrl.sendCode, "获取验证码", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {//成功
                            T.ss(getString(R.string.Base_yzmyfs));
                            GetPhoneCodeStepTwoActivity.actionStart(mActivity,area_code,phoneNumber);
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            agreement = true;
        } else {
            agreement = false;
        }
    }

    private void setSe(TextView login_txt) {
        String str = "《用户协议》和《隐私政策》";
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        final int start = str.indexOf("《");//第一个出现的位置
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                jumpToWebView("用户协议", H5Url.H5UserPolicy);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue));
                ds.setUnderlineText(false);
            }
        }, start, start + 6, 0);
        int end = str.lastIndexOf("《");
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                jumpToWebView("隐私政策", H5Url.H5PrivatePolicy);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue));
                ds.setUnderlineText(false);
            }
        }, end, end + 6, 0);
        login_txt.setMovementMethod(LinkMovementMethod.getInstance());
        login_txt.setText(ssb, TextView.BufferType.SPANNABLE);
    }



}