package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 我的社交主页
 */
public class Edit_ChangePage extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.addPage_Tv)
    TextView addPage_Tv;
    @BindView(R.id.urlLabel_Lin)
    LinearLayout urlLabel_Lin;
    private String nickname;

    public static void actionStart(Context context, String nickname) {
        Intent intent = new Intent(context, Edit_ChangePage.class);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_changpage;
    }


    @Override
    protected void initLocalData() {
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initEvent() {
        addPage_Tv.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserData(false);
    }

    UserBean userBean;

    private void getUserData( boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", SharePref.user().getUserId());
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean.getColumn() != null) {
                        List<UserBean.ColumnDTO> column = userBean.getColumn();
                        urlLabelFlow(urlLabel_Lin, mActivity, column);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void del_Column(String columnID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", columnID);
        HttpSender httpSender = new HttpSender(HttpUrl.UserColumn_Del, "删除专栏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    getUserData(false);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



    /**
     * 我的专栏
     */
    public void urlLabelFlow(LinearLayout myFlow, Context mContext, List<UserBean.ColumnDTO> urlList) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (ListUtils.isEmpty(urlList)) {
            return;
        }
        int index = 0;
        for (UserBean.ColumnDTO columnDTO : urlList) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.user_page_edit, null);
            TextView name_Tv = view.findViewById(R.id.name_Tv);
            TextView contentUrl_Tv = view.findViewById(R.id.contentUrl_Tv);
            ImageView editClear_Img = view.findViewById(R.id.editClear_Img);
            name_Tv.setText(columnDTO.getColumnName());
            contentUrl_Tv.setText(columnDTO.getColumnUrl());
            editClear_Img.setTag(index);
            myFlow.addView(view);
            index++;
            editClear_Img.setOnClickListener(v -> {
                int tag = (int) v.getTag();
                String columnName = urlList.get(tag).getColumnName();
                String id = urlList.get(tag).getId();
                new TwoButtonDialogBlue(mActivity, "确定删除‘"+ columnName+"’？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
                    @Override
                    public void onClickRight() {
                        del_Column(id);
                    }
                    @Override
                    public void onClickLeft() {
                    }
                }).show();
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPage_Tv:
                Edit_ChangePage_Add.actionStart(mActivity, userBean);
                break;
        }
    }
}


