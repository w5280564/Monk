package com.qingbo.monk.home.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipStock_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeSeekFund_All;
import com.qingbo.monk.bean.HomeSeekFund_Bean;
import com.qingbo.monk.bean.HomeSeekFund_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_Fund_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 搜索——股票
 */
public class HomeSeek_Fund extends BaseRecyclerViewSplitFragment {
    private String word = "";
//    private TextView query_Edit;

    public static HomeSeek_Fund newInstance(String word) {
        Bundle args = new Bundle();
        args.putString("word", word);
        HomeSeek_Fund fragment = new HomeSeek_Fund();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homeseek_fund_fragment;
    }

    @Override
    protected void initLocalData() {
//        word = getArguments().getString("word");
    }

    @Override
    protected void initView(View mRootView) {
//        query_Edit = requireActivity().findViewById(R.id.query_Edit);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        mRecyclerView = mRootView.findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void loadData() {
        word = ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(word, false);
    }

    @Override
    public void onRefreshData() {
        word = ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        page = 1;
        getExpertList(word, false);
    }

    @Override
    protected void onLoadMoreData() {
        word = ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        page++;
        getExpertList(word, false);
    }

    public void getExpertList(String word, boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_Fund, "搜索股票", requestMap, new MyOnHttpResListener() {
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeSeekFund_All homeSeekFund_all = GsonUtil.getInstance().json2Bean(json_root, HomeSeekFund_All.class);
                    if (homeSeekFund_all != null) {
                        List<HomeSeekFund_Bean> agu = homeSeekFund_all.getData().getList().getAgu();
                        List<HomeSeekFund_Bean> ganggu = homeSeekFund_all.getData().getList().getGanggu();
                        HomeSeekFund_ListBean homeSeekFund_listBean = new HomeSeekFund_ListBean();
                        List<HomeSeekFund_Bean> Fund_beans = new ArrayList<>();
                       if (page == 1 && !ListUtils.isEmpty(Fund_beans)){
                           Fund_beans.clear();
                       }

                        if (!ListUtils.isEmpty(agu)) {
                            Fund_beans.addAll(agu);
                        }
                        if (!ListUtils.isEmpty(ganggu)) {
                            Fund_beans.addAll(ganggu);
                        }
                        homeSeekFund_listBean.setList(Fund_beans);
                        ((HomeSeek_Fund_Adapter) mAdapter).setFindStr(word);
                        handleSplitListData(homeSeekFund_listBean, mAdapter, limit);
                    }
                }
            }
        }, isShowAnimal);
        sender.setContext(requireActivity());
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_Fund_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeSeekFund_Bean item = (HomeSeekFund_Bean) adapter.getItem(position);
                SideslipStock_Activity.startActivity(requireActivity(), item.getName(), item.getNumber());
            }
        });
    }


}