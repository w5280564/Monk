package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.AAndHKDetail_Activity;
import com.qingbo.monk.Slides.adapter.InsiderHK_Adapter;
import com.qingbo.monk.Slides.adapter.StockThighHK_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.HomeInsiderHKBean;
import com.qingbo.monk.bean.InsiderHKListBean;
import com.qingbo.monk.bean.InsiderListBean;
import com.qingbo.monk.bean.StockThighHK_ListBean;
import com.qingbo.monk.home.adapter.Insider_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧边栏 个股-A股/港股--公告
 */
public class StockNitice_Fragment extends BaseRecyclerViewSplitFragment {

    @BindView(R.id.dingTop_Img)
    ImageView dingTop_Img;
    private String search, type;


    /**
     * @param type   1是A股2是港股
     * @param search 查询股票code 改为name
     * @return
     */
    public static StockNitice_Fragment newInstance(String type, String search) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("search", search);
        StockNitice_Fragment fragment = new StockNitice_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        super.initLocalData();
        type = getArguments().getString("type");
        search = getArguments().getString("search");
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
        initSwipeRefreshLayoutAndAdapter("暂无内容", 0, true);
    }

    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }


    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("type", type);
        requestMap.put("search", search);
        HttpSender httpSender = new HttpSender(HttpUrl.Insider_List, "个股/基金--公告", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    if (TextUtils.equals(type, "2")) {
                        InsiderHKListBean hkListBean = GsonUtil.getInstance().json2Bean(json_data, InsiderHKListBean.class);
                        if (hkListBean != null) {
                            handleSplitListData(hkListBean, mAdapter, limit);
                        }
                    } else {
                        InsiderListBean insiderListBean = GsonUtil.getInstance().json2Bean(json_data, InsiderListBean.class);
                        if (insiderListBean != null) {
                            handleSplitListData(insiderListBean, mAdapter, limit);
                        }
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
        if (TextUtils.equals(type, "2")) {
            mAdapter = new InsiderHK_Adapter();
        } else {
            mAdapter = new Insider_Adapter();
        }
        mRecyclerView.setAdapter(mAdapter);

        onBackTop(dingTop_Img);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.equals(type, "2")) {
                    HomeInsiderHKBean item = (HomeInsiderHKBean) adapter.getItem(position);
                    String newsUuid = item.getNewsUuid();
                    AAndHKDetail_Activity.startActivity(requireActivity(),newsUuid,"0","0");
                }else {
                    HomeInsiderBean item = (HomeInsiderBean) adapter.getItem(position);
                    jumpToWebView(item.getNewsTitle(),item.getFile_url());
                }
            }
        });
    }


}
