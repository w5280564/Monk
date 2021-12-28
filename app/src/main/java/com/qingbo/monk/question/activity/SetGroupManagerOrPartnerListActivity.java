package com.qingbo.monk.question.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.dialog.SetManagerDialog;
import com.qingbo.monk.question.adapter.GroupMemberListAdapter;

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

    private GroupMemberListAdapter mGroupMemberListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_or_partner_list_set;
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
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
        mGroupMemberListAdapter = new GroupMemberListAdapter();
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

    @Override
    protected void initEvent() {
        mGroupMemberListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showSetManagerDialog();
            }
        });
    }

    private void showSetManagerDialog() {
        SetManagerDialog mSetManagerDialog = new SetManagerDialog(this, new SetManagerDialog.ConfirmListener() {
            @Override
            public void onSet() {

            }
        });
        mSetManagerDialog.show();
    }


    @OnClick({R.id.title_bar_left, R.id.title_bar_center_txt, R.id.et_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                back();
                break;
            case R.id.title_bar_center_txt:
                break;
            case R.id.et_search:
                break;

        }
    }

}
