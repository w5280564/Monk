package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.AAndHKDetail_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.InsiderListBean;
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
    private String search,type;


    /**
     *
     * @param type 1是A股2是港股
     * @param search  查询股票code
     * @return
     */
    public static StockNitice_Fragment newInstance(String type,String search) {
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
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无公告",false);
    }

    @Override
    protected void loadData() {
        getListData(true);
    }

    InsiderListBean insiderListBean;

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

        onBackTop(dingTop_Img);

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
