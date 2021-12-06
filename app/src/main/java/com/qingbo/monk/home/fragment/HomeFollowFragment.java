package com.qingbo.monk.home.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.adapter.Follow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class HomeFollowFragment extends BaseFragment {
    //    String type;
//    String status, isVip;
    @BindView(R.id.card_Recycler)
    RecyclerView card_Recycler;


    public static HomeFollowFragment newInstance(String type, String status, String isVip) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("status", status);
        args.putString("isVip", isVip);
        HomeFollowFragment fragment = new HomeFollowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }


    @Override
    protected void getServerData() {
        super.getServerData();
        getListData();
    }

    int page = 1;

    private void getListData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", "10");
        HttpSender httpSender = new HttpSender(HttpUrl.Recommend_List, "首页-推荐", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeFllowBean homeFllowBean = new Gson().fromJson(json_root, HomeFllowBean.class);
                    if (homeFllowBean != null) {
                        initlist(mContext, homeFllowBean.getData());
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    public void initlist(final Context context, HomeFllowBean.DataDTO homeFllowBean) {
        LinearLayoutManager mMangaer = new LinearLayoutManager(context);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        card_Recycler.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        card_Recycler.setHasFixedSize(true);
//        Follow_Adapter mAdapter = new Follow_Adapter(context, homeFllowBean);
        Follow_Adapter homeFollowAdapter = new Follow_Adapter();
        List<HomeFllowBean.DataDTO.ListDTO> list = homeFllowBean.getList();
        homeFollowAdapter.setNewData(list);
        card_Recycler.setAdapter(homeFollowAdapter);
    }


}
