package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.CharacterDetail_Bean;
import com.qingbo.monk.bean.Page_Column_Bean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.PickStringDialog;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 添加社交主页
 */
public class Edit_ChangePage_Add extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.name_Tv)
    TextView name_Tv;
    @BindView(R.id.pageUrl_edit)
    TextView pageUrl_edit;

    private UserBean userBean;

    public static void actionStart(Context context, UserBean userBean) {
        Intent intent = new Intent(context, Edit_ChangePage_Add.class);
        intent.putExtra("userBean", userBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_changadd;
    }


    @Override
    protected void initLocalData() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
    }

    @Override
    protected void initEvent() {
        name_Tv.setOnClickListener(this);
    }


    @Override
    public void onRightClick() {
        super.onRightClick();
        if (ListUtils.isEmpty(firList)){
            T.s("添加社交主页已达上限",2000);
            return;
        }
        edit_Info();
    }

    @Override
    protected void getServerData() {
        columnNameList_Data();
    }

    private void edit_Info() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("columnName", name_Tv.getText().toString());
        requestMap.put("columnUrl", pageUrl_edit.getText().toString());
        HttpSender httpSender = new HttpSender(HttpUrl.UserColumn_AddOrUpdate, "添加/更新专栏", requestMap, new MyOnHttpResListener() {
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

    List<String> firList;
    private void columnNameList_Data() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.UserColumn_List, "专栏名称列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    Page_Column_Bean page_column_bean = GsonUtil.getInstance().json2Bean(json_root, Page_Column_Bean.class);
                    firList = new ArrayList<>();
                    List<String> twoList = new ArrayList<>();
                    for (Page_Column_Bean.DataDTO dataDTO : page_column_bean.getData()) {
                        firList.add(dataDTO.getName());
                    }
                    for (UserBean.ColumnDTO columnDTO : userBean.getColumn()) {
                        twoList.add(columnDTO.getColumnName());
                    }
                    firList.removeAll(twoList);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name_Tv:
                setPage(firList);
                break;
        }
    }




    //选中 只设置一组数据就好
    private void setPage(List<String> pageNameList) {
        if (ListUtils.isEmpty(pageNameList)) {
            return;
        }
        final List<String> mOptionsItems = new ArrayList<>();
        for (String name : pageNameList) {
            mOptionsItems.add(name);
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mActivity, (options1, option2, options3, v) -> {
            name_Tv.setText(mOptionsItems.get(options1));

        }).build();
        pvOptions.setPicker(mOptionsItems);
        pvOptions.show();
    }



}


