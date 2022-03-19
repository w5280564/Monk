package com.qingbo.monk.person.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.System_ExamineList_Bean;
import com.qingbo.monk.bean.System_Examine_Bean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.person.adapter.MySystem_Examine_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 系统通知-评论
 */
public class Person_System_examine extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    //    @BindView(R.id.refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.personsystem_examine;
    }


    @Override
    protected void initView() {
//        title_bar.setTitle("评论");
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
        postClear(false);
        getExpertList(true);
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getExpertList(true);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getExpertList(false);
    }


    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.System_ReplyList, "系统审核评论", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            System_ExamineList_Bean system_examineList_bean = GsonUtil.getInstance().json2Bean(json_data, System_ExamineList_Bean.class);
                            if (system_examineList_bean != null) {
                                handleSplitListData(system_examineList_bean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new MySystem_Examine_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            System_Examine_Bean item = (System_Examine_Bean) adapter.getItem(position);
            String articleId = item.getArticleId();
            if (!TextUtils.isEmpty(articleId)) {
                String type = item.getCommentType();
                ArticleDetail_Activity.startActivity(this, articleId, "0", type);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.reply_Tv_:
                        System_Examine_Bean item = (System_Examine_Bean) adapter.getItem(position);
                        String articleId = item.getArticleId();
                        if (!TextUtils.isEmpty(articleId)) {
                            String type = item.getCommentType();
                            ArticleDetail_Activity.startActivity(mActivity, articleId, "0", type);
                        }
                        break;
                }
            }
        });
    }

    private void postClear(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("type","comment");
        HttpSender sender = new HttpSender(HttpUrl.System_Mes_Clear, "系统消息数-清除提醒", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {

                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendPost();
    }



}