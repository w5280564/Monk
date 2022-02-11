package com.qingbo.monk.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeSeekGroup_All;
import com.qingbo.monk.bean.HomeSeekGroup_Bean;
import com.qingbo.monk.bean.HomeSeekGroup_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_Group_Adapter;
import com.qingbo.monk.home.adapter.HomeSeek_Person_Adapter;
import com.qingbo.monk.person.activity.MyGroupList_Activity;
import com.qingbo.monk.question.activity.GroupDetailActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索——圈子
 */
public class HomeSeek_Group extends BaseRecyclerViewSplitFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String word;
    private SearchEditText query_edit;

    public static HomeSeek_Group newInstance(String word) {
        Bundle args = new Bundle();
        args.putString("word",word);
        HomeSeek_Group fragment = new HomeSeek_Group();
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

    HomeSeekGroup_ListBean homeSeekGroup_listBean;
    public void getExpertList(String word,boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_Group, "搜索圈子", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            HomeSeekGroup_All homeSeekGroup_all = GsonUtil.getInstance().json2Bean(json_root, HomeSeekGroup_All.class);
                            List<HomeSeekGroup_Bean> data = homeSeekGroup_all.getData();

                             homeSeekGroup_listBean = new HomeSeekGroup_ListBean();
                            homeSeekGroup_listBean.setList(data);
                            if (homeSeekGroup_listBean != null) {
                                ((HomeSeek_Group_Adapter)mAdapter).setFindStr(word);
                                handleSplitListData(homeSeekGroup_listBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_Group_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeSeekGroup_Bean item = (HomeSeekGroup_Bean) adapter.getItem(position);
            String id = item.getId();
            GroupDetailActivity.actionStart(mActivity, id);
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            HomeSeekGroup_Bean item = (HomeSeekGroup_Bean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.join_Tv:
                    changeJoin(item.getJoinStatus(), position);
                    getJoin(item.getId());
                    break;
            }
        });


    }

    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 修改加入状态
     *
     * @param stateIndex 1已加入 其他都是未加入
     * @param position
     */
    private void changeJoin(String stateIndex, int position) {
        if (homeSeekGroup_listBean != null) {
            TextView join_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.join_Tv);
            if (TextUtils.equals(stateIndex, "1")) {
                stateIndex = "0";
            } else {
                stateIndex = "1";
            }
            homeSeekGroup_listBean.getList().get(position).setJoinStatus(stateIndex);
            ((HomeSeek_Group_Adapter) mAdapter).isJoin(stateIndex, join_Tv);
        }
    }


}