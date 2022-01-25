package com.qingbo.monk.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeSeekUser_Bean;
import com.qingbo.monk.bean.HomeSeekUser_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_User_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 搜索——用户
 */
public class HomeSeek_User extends BaseRecyclerViewSplitFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String word;
    private SearchEditText query_edit;

    public static HomeSeek_User newInstance(String word) {
        Bundle args = new Bundle();
        args.putString("word",word);
        HomeSeek_User fragment = new HomeSeek_User();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homeseek_fragment;
    }

    @Override
    protected void initLocalData() {
//         word = getArguments().getString("word");
    }

    @Override
    protected void initView(View mRootView) {
        query_edit = ((HomeSeek_Activity) requireActivity()).query_Edit;
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);

    }

    @Override
    protected void loadData() {
        word = query_edit.toString();
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(word,false);
    }


    @Override
    protected void onRefreshData() {
        word = query_edit.toString();
        page = 1;
        getExpertList(word,false);
    }

    @Override
    protected void onLoadMoreData() {
        word = query_edit.toString();
        page++;
        getExpertList(word,false);
    }

    HomeSeekUser_ListBean homeSeekUser_listBean;
    public void getExpertList(String word, boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", 1 + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_User, "搜索用户", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                             homeSeekUser_listBean = GsonUtil.getInstance().json2Bean(json_data, HomeSeekUser_ListBean.class);
                            if (homeSeekUser_listBean != null) {
                                handleSplitListData(homeSeekUser_listBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_User_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            HomeSeekUser_Bean item = (HomeSeekUser_Bean) adapter.getItem(position);
//            if (item==null) {
//                return;
//            }
//            String id = item.getId();
//            MyAndOther_Card.actionStart(mActivity, id);
        });

        mAdapter.setOnItemChildClickListener(new MyFollow_Adapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeSeekUser_Bean item = (HomeSeekUser_Bean) adapter.getItem(position);
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
                    ((HomeSeek_User_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



}