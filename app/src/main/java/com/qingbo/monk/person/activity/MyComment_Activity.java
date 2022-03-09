package com.qingbo.monk.person.activity;

import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.MyCommentBean;
import com.qingbo.monk.bean.MyCommentList_Bean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.person.adapter.MyComment_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的评论
 */
public class MyComment_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar titleBar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.my_recyleview_refresh;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("我的评论");
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
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

    MyCommentList_Bean myCommentList_bean;

    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.User_Comment, "我的评论", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            myCommentList_bean = GsonUtil.getInstance().json2Bean(json_data, MyCommentList_Bean.class);
                            if (myCommentList_bean != null) {
                                handleSplitListData(myCommentList_bean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new MyComment_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyCommentBean item = (MyCommentBean) adapter.getItem(position);
            String articleId = item.getArticleId();
            if (!TextUtils.isEmpty(articleId)) {
                String type = item.getCommentType();
                ArticleDetail_Activity.startActivity(this, articleId, "0", type);

            }
        });
    }


}