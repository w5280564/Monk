package com.qingbo.monk.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.message.activity.ContactListActivity;
import com.qingbo.monk.message.adapter.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private MessageListAdapter mMessageListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mMessageListAdapter = new MessageListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMessageListAdapter);
        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mMessageListAdapter.setNewData(mList);
    }

    @OnClick({R.id.seek_Tv, R.id.ll_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seek_Tv:
                break;
            case R.id.ll_contact:
                skipAnotherActivity(ContactListActivity.class);
                break;
        }
    }
}
