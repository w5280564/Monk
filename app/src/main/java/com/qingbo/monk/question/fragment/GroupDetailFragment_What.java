package com.qingbo.monk.question.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.question.adapter.GroupDetailListAdapterAll;
import com.qingbo.monk.question.adapter.GroupDetailListAdapterWhat;

import java.util.ArrayList;
import java.util.List;

/**
 * 社群详情（去提问）
 */
public class GroupDetailFragment_What extends BaseRecyclerViewSplitFragment {




    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_detail_what;
    }



    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", false);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailListAdapterWhat();
        mRecyclerView.setAdapter(mAdapter);

        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mAdapter.setNewData(mList);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }


    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {

    }

}
