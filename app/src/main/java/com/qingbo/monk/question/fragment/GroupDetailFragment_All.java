package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.question.activity.PublisherGroupTopicActivity;
import com.qingbo.monk.question.adapter.GroupDetailListAdapterAll;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.OnClick;

/**
 * 社群详情全部
 */
public class GroupDetailFragment_All extends BaseRecyclerViewSplitFragment {
    private int type;
    private String id;

    public static GroupDetailFragment_All NewInstance(int type, String id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        GroupDetailFragment_All fragment = new GroupDetailFragment_All();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_detail_all_layout;
    }


    @Override
    protected void initLocalData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            type = mBundle.getInt("type", 0);
            id = mBundle.getString("id");
        }

    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailListAdapterAll();
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

    @Override
    protected void loadData() {
        getQuestionList();
    }

    private void getQuestionList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("action", "1");
        map.put("page", page + "");
        map.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.groupDetailAllTab, "社群全部", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {

                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData() {


    }

    @OnClick(R.id.iv_bianji)
    public void onClick() {
        PublisherGroupTopicActivity.actionStart(mActivity,id);
    }
}
