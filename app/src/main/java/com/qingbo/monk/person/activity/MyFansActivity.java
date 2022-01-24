package com.qingbo.monk.person.activity;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.ArticleLikedListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的粉丝列表
 */
public class MyFansActivity extends BaseRecyclerViewSplitActivity {
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
        titleBar.setTitle("我的粉丝");
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(false);
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getExpertList(false);
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
        HttpSender sender = new HttpSender(HttpUrl.fans_Like_List, "粉丝列表", requestMap,
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
//            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
//            String articleId = item.getArticleId();
//            String type = item.getType();
//            ArticleDetail_Activity.startActivity(this, articleId, "0", type);
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
                    case R.id.head_Img:
                        String id = item.getId();
                        MyAndOther_Card.actionStart(mActivity, id);
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