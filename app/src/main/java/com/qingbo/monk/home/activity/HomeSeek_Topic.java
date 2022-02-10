package com.qingbo.monk.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeSeekTopic_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_Person_Adapter;
import com.qingbo.monk.home.adapter.HomeSeek_Topic_Adapter;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 搜索——资讯
 */
public class HomeSeek_Topic extends BaseRecyclerViewSplitFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String word;
    private SearchEditText query_edit;

    public static HomeSeek_Topic newInstance(String word) {
        Bundle args = new Bundle();
        args.putString("word",word);
        HomeSeek_Topic fragment = new HomeSeek_Topic();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homeseek_fragment;
    }

    @Override
    protected void initLocalData() {
         word = getArguments().getString("word");
    }

    @Override
    protected void initView(View mRootView) {
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void loadData() {
        word =  ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(word,false);
    }



    @Override
    protected void onRefreshData() {
        word =  ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        page = 1;
        getExpertList(word,false);
    }

    @Override
    protected void onLoadMoreData() {
        word =  ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        page++;
        getExpertList(word,false);
    }

    HomeSeekTopic_ListBean homeSeekTopic_listBean;
    public void getExpertList(String word,boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_Topic, "搜索资讯", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                             homeSeekTopic_listBean = GsonUtil.getInstance().json2Bean(json_data, HomeSeekTopic_ListBean.class);
                            if (homeSeekTopic_listBean != null) {
                                ((HomeSeek_Topic_Adapter)mAdapter).setFindStr(word);
                                handleSplitListData(homeSeekTopic_listBean, mAdapter, limit);
                            }
                        }
                    }
                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_Topic_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
//            if (item==null) {
//                return;
//            }
//            String id = item.getId();
//            MyAndOther_Card.actionStart(mActivity, id);
//        });

//        mAdapter.setOnItemChildClickListener(new MyFollow_Adapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
//                switch (view.getId()) {
//                    case R.id.follow_Tv:
//                        String likeId = item.getId();
//                        postFollowData(likeId, position);
//                        break;
//                    case R.id.send_Mes:
//                        ChatActivity.actionStart(mActivity, item.getId(), item.getNickname(), item.getAvatar());
//                        break;
//                }
//            }
//        });
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