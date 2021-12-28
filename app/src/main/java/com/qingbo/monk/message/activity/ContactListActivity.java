package com.qingbo.monk.message.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.message.adapter.ContactListAdapter;
import com.xunda.lib.common.view.SideBar;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 通讯录列表
 */
public class ContactListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.sidrbar)
    SideBar mSideBar;
    private ContactListAdapter mContactListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_list;
    }

    @Override
    protected void initView() {
        mContactListAdapter = new ContactListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mContactListAdapter);
        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mContactListAdapter.setNewData(mList);
    }

    @Override
    protected void initEvent() {
//        //设置右侧触摸监听
//        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                //该字母首次出现的位置
//                int position = mSideBar.getPositionForSection(s.charAt(0));
//                if (position != -1) {
//                    mRecyclerView.scrollToPosition(position);
//                }
//
//            }
//        });
    }
}
