package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.BaseHaveBean;
import com.qingbo.monk.bean.HaveBean;
import com.qingbo.monk.person.adapter.HarvestAdapter;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑收获
 */
public class Edit_Change_Harvest extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private String nickname, getResource;
    private HarvestAdapter mHaveAdapter;

    public List<String> mChooseGetResourceList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.edit_change_harvest;
    }

    /**
     * @param context
     * @param nickname    修改资料必须携带
     * @param getResource 收获
     */
    public static void actionStart(Context context, String nickname, String getResource) {
        Intent intent = new Intent(context, Edit_Change_Harvest.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("getResource", getResource);
        context.startActivity(intent);
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getHaveList();
    }

    @Override
    protected void initLocalData() {
        nickname = getIntent().getStringExtra("nickname");
        getResource = getIntent().getStringExtra("getResource");

        if (!TextUtils.isEmpty(getResource)) {
            mChooseGetResourceList = StringUtil.stringToList(getResource);
        }
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mHaveAdapter = new HarvestAdapter(mChooseGetResourceList);
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(DisplayUtil.dip2px(mActivity, 23), getResources().getColor(R.color.app_background)));
        mRecyclerView.setAdapter(mHaveAdapter);

        mHaveAdapter.setOnItemClickListener((adapter, view, position) -> {
            HaveBean obj = (HaveBean) adapter.getItem(position);
            if (obj != null) {
                if (!obj.isCheck()) {
                    mChooseGetResourceList.add(obj.getName());
                    obj.setCheck(true);
                } else {
                    mChooseGetResourceList.remove(obj.getName());
                    obj.setCheck(false);
                }
                mHaveAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                if (mChooseGetResourceList.size() < 3) {
                    T.ss("至少选择三个方向");
                    return;
                }
                String s = StringUtil.listToString(mChooseGetResourceList);
                edit_Info(s);
                break;

        }
    }


    /**
     * 获取收获列表
     */
    private void getHaveList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.harvestList, "获取收获列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseHaveBean mBaseHaveBean = GsonUtil.getInstance().json2Bean(json_data, BaseHaveBean.class);
                            if (mBaseHaveBean != null) {
                                handleData(mBaseHaveBean);
                            }
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(BaseHaveBean mBaseHaveBean) {
        mHaveAdapter.setNewData(mBaseHaveBean.getList());
    }

    private void edit_Info(String get_resource) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        requestMap.put("get_resource", get_resource);
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