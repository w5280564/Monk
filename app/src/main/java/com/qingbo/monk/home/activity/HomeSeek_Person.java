package com.qingbo.monk.home.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipPersonAndFund_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeSeekPerson_Bean;
import com.qingbo.monk.bean.HomeSeekPerson_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_Person_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 搜索——人物
 */
public class HomeSeek_Person extends BaseRecyclerViewSplitFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String word;
    private SearchEditText query_edit;

    public static HomeSeek_Person newInstance(String word) {
        Bundle args = new Bundle();
        args.putString("word",word);
        HomeSeek_Person fragment = new HomeSeek_Person();
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

    HomeSeekPerson_ListBean homeSeekPerson_listBean;
    public void getExpertList(String word,boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_Person, "搜索人物", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                             homeSeekPerson_listBean = GsonUtil.getInstance().json2Bean(json_data, HomeSeekPerson_ListBean.class);
                            if (homeSeekPerson_listBean != null) {
                                ((HomeSeek_Person_Adapter)mAdapter).setFindStr(word);
                                handleSplitListData(homeSeekPerson_listBean, mAdapter, limit);
                            }
                        }
                    }
                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_Person_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeSeekPerson_Bean item = (HomeSeekPerson_Bean) adapter.getItem(position);
            String nickname = item.getNickname();
            String id = item.getId();
            SideslipPersonAndFund_Activity.startActivity(mActivity, nickname, id, "0");
        });

    }




}