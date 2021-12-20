package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.AAndHKDetail_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.InsiderListBean;
import com.qingbo.monk.home.adapter.Insider_Adapter;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import java.util.HashMap;

/**
 * 侧边栏 基金--公告
 */
public class FundNitice_Fragment extends BaseRecyclerViewSplitFragment {


    private String news_digest;

    /**
     *
     * @param
     * @return
     */
    public static FundNitice_Fragment newInstance(String news_digest) {
        Bundle args = new Bundle();
        args.putString("news_digest", news_digest);
        FundNitice_Fragment fragment = new FundNitice_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        super.initLocalData();
        news_digest = getArguments().getString("news_digest");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据",false);
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getListData(true);
    }

    InsiderListBean insiderListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("news_digest", news_digest);
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Notice, "基金公告", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                     insiderListBean = GsonUtil.getInstance().json2Bean(json_data, InsiderListBean.class);
                    if (insiderListBean != null) {
                        handleSplitListData(insiderListBean, mAdapter, limit);
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
        mAdapter = new Insider_Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeInsiderBean item = (HomeInsiderBean) adapter.getItem(position);
                String newsUuid = item.getNewsUuid();
                AAndHKDetail_Activity.startActivity(requireActivity(),newsUuid,"0","0");
            }
        });
    }



}
