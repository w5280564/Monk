package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 修改个人说明
 */
public class Edit_ChangeExplain extends BaseActivity {
    @BindView(R.id.change_Edit)
    EditText change_Edit;
    @BindView(R.id.changeCount_Tv)
    TextView changeCount_Tv;

    private String nickname;

    public static void actionStart(Context context, String nickname) {
        Intent intent = new Intent(context, Edit_ChangeExplain.class);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_changeexplain;
    }

    @Override
    protected void initLocalData() {
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initEvent() {
        addEditTextListener();
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        int length = change_Edit.getText().length();
        if (length < 20){
            T.s("内容不能少于20字",3000);
            return;
        }
        edit_Info();
    }

    private void edit_Info() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        requestMap.put("description", change_Edit.getText().toString());
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
                changeCount_Tv.setText(len + "/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}


