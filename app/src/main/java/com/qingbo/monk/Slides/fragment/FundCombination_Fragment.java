package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.FundCombination_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.CombinationListBean;
import com.qingbo.monk.bean.FundCombinationListBean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.adapter.Combination_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧边栏 基金-基金持股
 */
public class FundCombination_Fragment extends BaseRecyclerViewSplitFragment {

    private String news_digest;
    @BindView(R.id.dingTop_Img)
    ImageView dingTop_Img;

    public static FundCombination_Fragment newInstance(String news_digest) {
        Bundle args = new Bundle();
        args.putString("news_digest", news_digest);
        FundCombination_Fragment fragment = new FundCombination_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0,false);
    }

    @Override
    protected void initLocalData() {
        news_digest = getArguments().getString("news_digest");
    }


    @Override
    protected void loadData() {
        getListData(true);
    }

    FundCombinationListBean fundCombinationListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("news_digest", news_digest);
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Position, "侧边栏-基金-基金持股", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    fundCombinationListBean = GsonUtil.getInstance().json2Bean(json_data, FundCombinationListBean.class);
                    if (fundCombinationListBean != null) {
                        handleSplitListData(fundCombinationListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {

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
        mAdapter = new FundCombination_Adapter();
        mRecyclerView.setAdapter(mAdapter);
//        homeFollowAdapter.setOnItemClickListener((adapter, view, position) -> {
//
//        });

        onBackTop(dingTop_Img);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }



}
