package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseWaitGroupAnswerBean;
import com.qingbo.monk.bean.WaitGroupAnswerBean;
import com.qingbo.monk.question.activity.PublisherAnswerQuestionToPeopleActivity;
import com.qingbo.monk.question.activity.PublisherAskQuestionToPeopleActivity;
import com.qingbo.monk.question.adapter.GroupDetailWaitAnswerAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;

/**
 * 社群详情等你回答列表
 */
public class GroupDetailWaitAnswerListFragment extends BaseRecyclerViewSplitFragment {
    private String id;

    public static GroupDetailWaitAnswerListFragment NewInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        GroupDetailWaitAnswerListFragment fragment = new GroupDetailWaitAnswerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.refresh_recyclerview_layout;
    }


    @Override
    protected void initLocalData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            id = mBundle.getString("id");
        }
        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if(event.type == FinishEvent.PUBLISH_ANSWER){
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
        initSwipeRefreshLayoutAndAdapter("暂无提问",R.mipmap.zhuti, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailWaitAnswerAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                WaitGroupAnswerBean mQuestionBean = (WaitGroupAnswerBean) adapter.getItem(position);

                if (mQuestionBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_answer:
                        PublisherAnswerQuestionToPeopleActivity.actionStart(mActivity,mQuestionBean.getNickname(),mQuestionBean.getId(),id);
                        break;
                }
            }
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
        map.put("page", page + "");
        map.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.waitAnswerList, "等待回答问题列表", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseWaitGroupAnswerBean obj = GsonUtil.getInstance().json2Bean(data, BaseWaitGroupAnswerBean.class);
                            handleSplitListData(obj, mAdapter, limit);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }

}
