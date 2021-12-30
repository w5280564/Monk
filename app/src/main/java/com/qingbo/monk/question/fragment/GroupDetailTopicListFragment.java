package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseOwnPublishBean;
import com.qingbo.monk.question.activity.PublisherGroupTopicActivity;
import com.qingbo.monk.question.adapter.GroupDetailTopicListAdapterAll;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import butterknife.OnClick;

/**
 * 社群详情话题列表
 */
public class GroupDetailTopicListFragment extends BaseRecyclerViewSplitFragment {
    private int type;//0全部 1我的发布
    private String id,requestUrl;

    public static GroupDetailTopicListFragment NewInstance(int type, String id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        GroupDetailTopicListFragment fragment = new GroupDetailTopicListFragment();
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
            if (type==0) {
                requestUrl = HttpUrl.groupDetailAllTab;
            }else{
                requestUrl = HttpUrl.getOwnPublishList;
            }
        }

        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if(event.type == FinishEvent.PUBLISH_TOPIC){
            page = 1;
            getQuestionList();
        }
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无话题",R.mipmap.zhuti, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailTopicListAdapterAll(type);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }


    @Override
    protected void onRefreshData() {
        page = 1;
        getQuestionList();
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getQuestionList();
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
        HttpSender sender = new HttpSender(requestUrl, "社群话题列表", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseOwnPublishBean obj = GsonUtil.getInstance().json2Bean(data, BaseOwnPublishBean.class);
                            handleSplitListData(obj, mAdapter, limit);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    @OnClick(R.id.iv_bianji)
    public void onClick() {
        PublisherGroupTopicActivity.actionStart(mActivity,id);
    }
}
