package com.qingbo.monk.person.activity;

import androidx.annotation.RequiresApi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.InputFilter;
import android.widget.EditText;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 修改昵称
 */
public class Edit_ChangeName extends BaseActivity {
    @BindView(R.id.changeName_Edit)
    EditText changeName_Edit;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;

    private String nickname;

    public static void actionStart(Context context, String nickname) {
        Intent intent = new Intent(context, Edit_ChangeName.class);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_changename;
    }

    @Override
    protected void initLocalData() {
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        changeName_Edit.setFilters(new InputFilter[]{new ByteLengthFilter(14)});
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        edit_Info();
    }

    private void edit_Info() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", changeName_Edit.getText().toString());
        HttpSender httpSender = new HttpSender(HttpUrl.Edit_Info, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    UserBean obj = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
//                    saveUserInfo(obj);
                    finish();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 保存用户信息
     *
     * @param userObj 用户对象
     */
    private void saveUserInfo(UserBean userObj) {
        if (userObj != null) {
            PrefUtil.saveUser(userObj, "");
        }
    }





}


