package com.qingbo.monk.login.fragment;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第4步
 */
public class StepFourFragmentLogin extends BaseFragment {
    @BindView(R.id.tv_description_toast)
    TextView tv_description_toast;
    @BindView(R.id.et_description)
    EditText et_description;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_four_fragment_login;
    }

    @Override
    protected void initEvent() {
        addEditTextListener();
    }

    /**
     * 给简介editext添加监听
     */
    private void addEditTextListener() {
        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_description.getText();
                int len = editable.length();
                //显示还可以输入多少字
                tv_description_toast.setText(len + "" + "/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.tv_back,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO));
                break;
            case R.id.tv_save:
                String description = StringUtil.getEditText(et_description);
                getEdit_Info(description);

                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_FOUR));
                break;

        }
    }

    @Override
    protected void initLocalData() {

    }

    private void getEdit_Info(String description) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("city","");
        requestMap.put("county","");
        requestMap.put("industry","");
        requestMap.put("work","");
        requestMap.put("get_resource","");
        requestMap.put("interested","");
        requestMap.put("nickname","");
        requestMap.put("description",description);
        HttpSender httpSender = new HttpSender(HttpUrl.interestedList, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {


            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

}
