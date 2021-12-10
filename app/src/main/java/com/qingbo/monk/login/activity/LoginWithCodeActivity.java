package com.qingbo.monk.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
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

                GetPhoneCodeStepTwoActivity.actionStart(mActivity,area_code,phoneNumber);
                break;
            case R.id.iv_wechat:
                wechatThirdLogin();
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


}