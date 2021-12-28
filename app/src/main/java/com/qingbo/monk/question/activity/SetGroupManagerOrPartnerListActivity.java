package com.qingbo.monk.question.activity;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerAdapter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加或删除社区管理员或合伙人
 */
public class SetGroupManagerOrPartnerListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private GroupManagerOrPartnerAdapter mGroupMemberListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_or_partner_list_set;
    }

    @Override
    protected void initLocalData() {

    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mGroupMemberListAdapter = new GroupManagerOrPartnerAdapter();
        mRecyclerView.setAdapter(mGroupMemberListAdapter);
        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mGroupMemberListAdapter.setNewData(mList);
    }




    @OnClick({R.id.et_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                break;

        }
    }

}
