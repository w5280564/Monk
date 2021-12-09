package com.qingbo.monk.login.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第4步
 */
public class StepFourFragmentLogin extends BaseFragment {
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.tv_nickName)
    TextView tv_nickName;
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


    @Override
    public void onResume() {
        super.onResume();
        String userHeadUrl = SharePref.user().getUserHead();
        String nickName = SharePref.server().getUserNickName();
        tv_nickName.setText(StringUtil.getStringValue(nickName));
        GlideUtils.loadCircleImage(mActivity,iv_header,userHeadUrl);
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
                if (StringUtil.isBlank(description)) {
                    T.ss("请填写个人介绍");
                    return;
                }

                if (description.length()<20) {
                    T.ss("最少20个字");
                    return;
                }

                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_FOUR,true,description));
                break;

        }
    }




}
