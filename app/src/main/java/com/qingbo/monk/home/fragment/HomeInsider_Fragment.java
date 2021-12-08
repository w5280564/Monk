package com.qingbo.monk.home.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.InsiderListBean;
import com.qingbo.monk.home.adapter.Insider_Adapter;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.CustomLoadMoreView;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 首页滑动tab页--内部人
 */
public class HomeInsider_Fragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.card_Recycler)
    RecyclerView card_Recycler;


    public static HomeInsider_Fragment newInstance(String type, String status, String isVip) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("status", status);
        args.putString("isVip", isVip);
        HomeInsider_Fragment fragment = new HomeInsider_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView() {
        super.initView();
        initlist(mContext);
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
        requestMap.put("type", "1");
        HttpSender httpSender = new HttpSender(HttpUrl.Insider_List, "首页-内部人", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                     insiderListBean = GsonUtil.getInstance().json2Bean(json_data, InsiderListBean.class);
                    if (insiderListBean != null) {
                        handleSplitListData(insiderListBean, insider_adapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getListData(false);
    }


    Insider_Adapter insider_adapter;

    public void initlist(final Context context) {
        LinearLayoutManager mMangaer = new LinearLayoutManager(context);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        card_Recycler.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        card_Recycler.setHasFixedSize(true);
        insider_adapter = new Insider_Adapter();
        insider_adapter.setEmptyView(addEmptyView("暂无数据", 0));
        insider_adapter.setLoadMoreView(new CustomLoadMoreView());
        card_Recycler.setAdapter(insider_adapter);
//        homeFollowAdapter.setOnItemClickListener((adapter, view, position) -> {
//
//        });

    }


    @Override
    protected void initEvent() {
        insider_adapter.setOnLoadMoreListener(this,card_Recycler);
    }


}
