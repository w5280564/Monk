package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.StockCombination_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.StockCombinationListBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧边栏 股票—十大股东/十大流通股东/基金持股
 */
public class StockThigh_Fragment extends BaseRecyclerViewSplitFragment {

    private String news_digest, type;
    @BindView(R.id.dingTop_Img)
    ImageView dingTop_Img;

    /**
     * @param news_digest 股票代码
     * @param type        1-十大股东 2-十大流通股东 3-基金持股 type 3的时候数据模型不一样
     * @return
     */
    public static StockThigh_Fragment newInstance(String news_digest, String type) {
        Bundle args = new Bundle();
        args.putString("news_digest", news_digest);
        args.putString("type", type);
        StockThigh_Fragment fragment = new StockThigh_Fragment();
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
        type = getArguments().getString("type");
    }

    @Override
    protected void loadData() {
        getListData(true);
    }


    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("news_digest", news_digest);
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Thigh, "股票—十大股东/十大流通股东/基金持股", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    StockCombinationListBean stockCombinationListBean = GsonUtil.getInstance().json2Bean(json_data, StockCombinationListBean.class);
                    if (stockCombinationListBean != null) {
                        handleSplitListData(stockCombinationListBean, mAdapter, limit);
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
        mAdapter = new StockCombination_Adapter();
        mRecyclerView.setAdapter(mAdapter);


        onBackTop(dingTop_Img);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }


}
