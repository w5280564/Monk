package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.Topic_Bean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑最有成就感的事
 */
public class Edit_Change_Achievement extends BaseActivity {
    private String nickname, achievement;

    @Override
    protected int getLayoutId() {
        return R.layout.edit_change_achievement;
    }

    /**
     * @param context
     * @param nickname   修改资料必须携带
     * @param achievement 最有成就感的事
     */
    public static void actionStart(Context context, String nickname, String achievement) {
        Intent intent = new Intent(context, Edit_Change_Achievement.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("achievement", achievement);
        context.startActivity(intent);
    }



    @Override
    protected void initLocalData() {
        nickname = getIntent().getStringExtra("nickname");
        achievement = getIntent().getStringExtra("achievement");
    }

    @OnClick({R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
//                if (description.length()<20) {
//                    T.ss("最少20个字");
//                    return;
//                }

//                if (choice_LableMap == null || choice_LableMap.size() < 3) {
//                    T.s("至少选择三个话题", 3000);
//                    return;
//                }
//                StringBuilder stringBuilder = new StringBuilder();
//                for (Iterator i = choice_LableMap.keySet().iterator(); i.hasNext(); ) {
//                    Object obj = i.next();
//                    stringBuilder.append(choice_LableMap.get(obj) + ",");
//                }
//                edit_Info(stringBuilder.toString());
                break;

        }
    }




    private void edit_Info(String interest) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        requestMap.put("achievement", achievement);
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