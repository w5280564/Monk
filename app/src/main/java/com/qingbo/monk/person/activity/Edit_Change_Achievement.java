package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑最有成就感的事
 */
public class Edit_Change_Achievement extends BaseActivity {
    @BindView(R.id.change_Edit)
    EditText change_Edit;
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.tv_nickName)
    TextView tv_nickName;
    @BindView(R.id.changeCount_Tv)
    TextView changeCount_Tv;

    private String nickname;
    private UserBean userBean;

    @Override
    protected int getLayoutId() {
        return R.layout.edit_change_achievement;
    }

    /**
     * @param context
     * @param userBean
     */
    public static void actionStart(Context context, UserBean userBean) {
        Intent intent = new Intent(context, Edit_Change_Achievement.class);
        intent.putExtra("userBean", userBean);
        context.startActivity(intent);
    }


    @Override
    protected void initLocalData() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
    }

    @Override
    protected void initView() {
        if (userBean != null) {
            nickname = userBean.getNickname();
            String achievement = userBean.getAchievement();
            change_Edit.setText(achievement);
            String avatar = userBean.getAvatar();
            GlideUtils.loadCircleImage(mActivity,iv_header,avatar);
            String nickname = userBean.getNickname();
            tv_nickName.setText(nickname);
        }
    }

    @Override
    protected void initEvent() {
        addEditTextListener();
    }

    /**
     * 给简介editext添加监听
     */
    private void addEditTextListener() {
        change_Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = change_Edit.getText();
                int len = editable.length();
                //显示还可以输入多少字
                changeCount_Tv.setText(len + "/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
//                int length = change_Edit.getText().length();
                if (change_Edit.length() < 20) {
                    T.ss("最少20个字");
                    return;
                }
                edit_Info();
                break;

        }
    }


    private void edit_Info() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        requestMap.put("achievement", change_Edit.getText().toString());
        HttpSender httpSender = new HttpSender(HttpUrl.Edit_Info, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    UserBean obj = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    finish();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}