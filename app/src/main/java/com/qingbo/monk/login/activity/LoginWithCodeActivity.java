package com.qingbo.monk.login.activity;

import android.content.Intent;
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
//    private ActivityResultLauncher mActivityResultLauncher;



    @Override
    protected int getLayoutId() {
       return R.layout.activity_login_with_code;
    }


    @Override
    protected void initView() {
//        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result!=null) {
//                    int resultCode = result.getResultCode();
//                    if (resultCode==RESULT_OK) {
//                        getMineBankList();
//                    }
//                }
//            }
//        });
    }





    @OnClick({R.id.ll_area_code,R.id.tv_send_code,R.id.iv_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_area_code:
//                Intent intent = new Intent(mActivity, AreaCodeListActivity.class);
//                intent.putExtra("isFromRechargeOrCash",true);
//                mActivityResultLauncher.launch(intent);
                skipAnotherActivity(AreaCodeListActivity.class);
                break;
            case R.id.tv_send_code:
                skipAnotherActivity(GetPhoneCodeStepTwoActivity.class);
                break;
            case R.id.iv_wechat:

                break;

        }
    }



}