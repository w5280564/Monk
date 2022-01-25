package com.qingbo.monk.person.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.ArticleLikedListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.MyCommentList_Bean;
import com.qingbo.monk.home.adapter.ArticleZan_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.adapter.MyComment_Adapter;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的关注列表
 */
public class MyFollowActivity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar titleBar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_fllow;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("我关注的人");
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

    ArticleLikedListBean articleLikedListBean;

    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.User_Follow_List, "关注列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            articleLikedListBean = GsonUtil.getInstance().json2Bean(json_data, ArticleLikedListBean.class);
//                            myCommentList_bean = GsonUtil.getInstance().json2Bean(json_data, MyCommentList_Bean.class);
                            if (articleLikedListBean != null) {
                                handleSplitListData(articleLikedListBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new MyFollow_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
            if (item==null) {
                return;
            }
            String id = item.getId();
            MyAndOther_Card.actionStart(mActivity, id);
        });

        mAdapter.setOnItemChildClickListener(new MyFollow_Adapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String likeId = item.getId();
                        postFollowData(likeId, position);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, item.getId(), item.getNickname(), item.getAvatar());
                        break;
                }
            }
        });
    }

    private void postFollowData(String otherUserId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
                    TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
                    ((MyFollow_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}