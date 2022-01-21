package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.AAndHKDetail_Activity;
import com.qingbo.monk.Slides.adapter.InsiderHK_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeInsiderHKBean;
import com.qingbo.monk.bean.InsiderHKListBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

/**
 * 侧边栏-内部人-港股
 */
public class HomeInsiderHK_Fragment extends BaseRecyclerViewSplitFragment {


    private String type;

    public static HomeInsiderHK_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        HomeInsiderHK_Fragment fragment = new HomeInsiderHK_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        super.initLocalData();
         type = getArguments().getString("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据",0,true);
    }

    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }

    InsiderHKListBean insiderHKListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Insider_List, "内部人-港股", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                     insiderHKListBean =  GsonUtil.getInstance().json2Bean(json_data, InsiderHKListBean.class);
                    if (insiderHKListBean != null) {
                        handleSplitListData(insiderHKListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {
        page = 1;
        getListData(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false);
    }


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new InsiderHK_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeInsiderHKBean item = (HomeInsiderHKBean) adapter.getItem(position);
                String newsUuid = item.getNewsUuid();
                AAndHKDetail_Activity.startActivity(requireActivity(),newsUuid,"0","0");
            }
        });

    }



}
