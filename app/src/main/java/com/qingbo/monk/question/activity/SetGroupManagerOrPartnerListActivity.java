package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMemberListBean;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加或删除社区管理员或合伙人
 */
public class SetGroupManagerOrPartnerListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String id,type;//3是管理员 4是合伙人
    private GroupManagerOrPartnerAdapter mGroupMemberListAdapter;

    public static void actionStart(Context context, String id, String type) {
        Intent intent = new Intent(context, SetGroupManagerOrPartnerListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_or_partner_list_set;
    }


    @Override
    protected void initView() {
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mGroupMemberListAdapter = new GroupManagerOrPartnerAdapter();
        mRecyclerView.setAdapter(mGroupMemberListAdapter);
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void getServerData() {
        groupUserList();
    }

    /**
     * 群成员列表
     */
    private void groupUserList() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", "2");
        HttpSender sender = new HttpSender(HttpUrl.groupUserList, "群成员列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            GroupMemberListBean obj = GsonUtil.getInstance().json2Bean(json_data, GroupMemberListBean.class);
                            if (obj==null) {
                                return;
                            }

                            handleData(obj);
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(GroupMemberListBean obj) {
        mGroupMemberListAdapter.setNewData(obj.getList());
        titleBar.setTitle(String.format("群成员（%s）",obj.getCount()));
    }



    @OnClick({R.id.et_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                break;

        }
    }

}
