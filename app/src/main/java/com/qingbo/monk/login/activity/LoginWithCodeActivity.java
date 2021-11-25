package com.qingbo.monk.login.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码登录
 */
public class LoginWithCodeActivity extends BaseActivity {
    @BindView(R.id.phoneCode_btn)
    Button nextBtn;
    @BindView(R.id.phoneCode_edit)
    EditText phoneEdit;
    private boolean fromExitAct;
    private String phoneNumber;

    private String type;//1注册2登录3修改密码4修改手机号5注销

    @Override
    protected void initEvent() {
        addEditTextListener(phoneEdit);
    }

    /**
     * 给editext添加监听
     */
    private void addEditTextListener(EditText et_text) {
        et_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                judgeButtonIsClickable();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                judgeButtonIsClickable();
            }
        });
    }

    /**
     * 判断按钮是否可以点击
     */
    public void judgeButtonIsClickable() {
        if (StringUtil.isBlank(StringUtil.getEditText(phoneEdit))) {
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
        nextBtn.setEnabled(false);
        fromExitAct = getIntent().getBooleanExtra("fromExitAct", false);
        type = getIntent().getStringExtra("type");
        registerEventBus();
    }

    @Override
    protected int getLayoutId() {
       return R.layout.activity_login_with_code;
    }

    @OnClick({R.id.phoneCode_btn})
    public void onViewClicked(View view) {
        phoneNumber = StringUtil.getEditText(phoneEdit);
        switch (view.getId()){
            case R.id.phoneCode_btn:
                if(type.equals("4")) {//修改手机号 校验新手机号
                    Intent intent = new Intent(mActivity, GetPhoneCodeStepTwoActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    intent.putExtra("type",type);
                    intent.putExtra("nowIndex","2");
                    startActivity(intent);
                }else if(type.equals("2")){//验证码登录
                    Intent intent = new Intent(mActivity, GetPhoneCodeStepTwoActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    intent.putExtra("type",type);
                    intent.putExtra("fromExitAct",fromExitAct);
                    startActivity(intent);
                }else if(type.equals("3")){
                    Intent intent = new Intent(mActivity, GetPhoneCodeStepTwoActivity.class);
                    intent.putExtra("phone",phoneNumber);
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
                break;

        }
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if (event.type == FinishEvent.ReSetPass_SUCCESS) {
            back();
        }else if(event.type == FinishEvent.ReSetNewPhone){
            back();
        }else if(event.type == FinishEvent.CODE_LOGIN_SUCCESS){
            back();
        }
    }

}