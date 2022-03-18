package com.qingbo.monk.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.home.activity.MainActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.bean.BaseUserBean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.SocketUnbindEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.CountDownTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换手机号
 */
public class ChangePhoneNumberActivity extends BaseActivity {
    @BindView(R.id.tv_number_before)
    TextView tv_number_before;
    @BindView(R.id.et_phoneNumber)
    EditText et_phoneNumber;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_send_code)
    CountDownTextView tv_send_code;
    private ActivityResultLauncher mActivityResultLauncher;
    private String area_code = "86";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_phone;
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChangePhoneNumberActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
    }

    @Override
    protected void initView() {
        tv_send_code.setResendString("s");
        tv_number_before.setText("+" + area_code);
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null) {
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_OK) {
                        area_code = result.getData().getStringExtra("area_code");
                        tv_number_before.setText("+" + area_code);
                    }
                }
            }
        });
    }


    @OnClick({R.id.ll_area_code, R.id.tv_send_code, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_area_code:
                Intent intent = new Intent(mActivity, AreaCodeListActivity.class);
                mActivityResultLauncher.launch(intent);
                break;
            case R.id.tv_send_code:
                String phoneNumberCode = StringUtil.getEditText(et_phoneNumber);

                if (StringUtil.isBlank(phoneNumberCode)) {
                    T.ss("请输入手机号");
                    return;
                }

                getSmsCode(area_code, phoneNumberCode, tv_send_code);
                break;
            case R.id.tv_submit:
                String phoneNumber = StringUtil.getEditText(et_phoneNumber);
                if (StringUtil.isBlank(phoneNumber)) {
                    T.ss("请输入手机号");
                    return;
                }
                String smsCode = StringUtil.getEditText(et_code);
                if (StringUtil.isBlank(smsCode)) {
                    T.ss("短信验证码");
                    return;
                }
                mobileBandLogin(phoneNumber, smsCode);
                break;
        }
    }


    /**
     * 修改手机号
     *
     * @param smsCode
     */
    private void mobileBandLogin(String phoneNumber, String smsCode) {
        String nickname = PrefUtil.getUser().getNickname();
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("nickname", nickname);
        baseMap.put("mobile", phoneNumber);
        baseMap.put("code", smsCode);
        HttpSender sender = new HttpSender(HttpUrl.Edit_Phone, "修改手机号", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("修改成功");
                            finish();
//                            getQuit();
//                            BaseUserBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseUserBean.class);
//                            saveUserInfo(obj);
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
        if (baseUserBean != null) {
            UserBean userObj = baseUserBean.getInfo();
            if (userObj == null) {
                return;
            }
            PrefUtil.saveUser(userObj, baseUserBean.getAccessToken());
            String interested = userObj.getInterested();
            if (StringUtil.isBlank(interested)) {//首次登陆
                skipAnotherActivity(WelcomeActivity.class);
            } else {
                skipAnotherActivity(MainActivity.class);
            }
        }

    }

    private void getQuit() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Login_Logout, "退出登录", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    EventBus.getDefault().post(new SocketUnbindEvent(SocketUnbindEvent.SocketUnbind));//解绑WebSocketService
                    PrefUtil.clearSharePrefInfo();
                    BaseApplication.getInstance().clearActivity();
                    skipAnotherActivity(LoginActivity.class);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

}