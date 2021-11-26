package com.qingbo.monk.login.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码登录
 */
public class LoginWithCodeActivity extends BaseActivity {
    @BindView(R.id.tv_number_before)
    TextView tv_number_before;
    @BindView(R.id.et_phoneCode)
    EditText et_phoneCode;
    private boolean fromExitAct;




    @Override
    protected int getLayoutId() {
       return R.layout.activity_login_with_code;
    }

    @OnClick({R.id.tv_number_before,R.id.tv_send_code,R.id.iv_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_number_before:

                break;
            case R.id.tv_send_code:
                skipAnotherActivity(GetPhoneCodeStepTwoActivity.class);
                break;
            case R.id.iv_wechat:

                break;

        }
    }


}