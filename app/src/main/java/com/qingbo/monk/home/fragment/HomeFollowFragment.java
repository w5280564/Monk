package com.qingbo.monk.home.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.adapter.Follow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.view.CustomLoadMoreView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class HomeFollowFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
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
    protected void initView() {
        super.initView();
        initlist(mContext);
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getListData();
    }

    int page = 1;
    protected int pageSize = 10;
    FollowListBean homeFllowBean;

    private void getListData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", pageSize + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Recommend_List, "首页-推荐", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFllowBean = new Gson().fromJson(json_data, FollowListBean.class);
                    if (homeFllowBean != null) {
                        handleSplitListData(homeFllowBean, homeFollowAdapter, pageSize);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getListData();
    }


    Follow_Adapter homeFollowAdapter;

    public void initlist(final Context context) {
        LinearLayoutManager mMangaer = new LinearLayoutManager(context);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        card_Recycler.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        card_Recycler.setHasFixedSize(true);
        homeFollowAdapter = new Follow_Adapter();
        homeFollowAdapter.setEmptyView(addEmptyView("暂无数据", 0));
        homeFollowAdapter.setLoadMoreView(new CustomLoadMoreView());
        card_Recycler.setAdapter(homeFollowAdapter);
//        homeFollowAdapter.setOnItemClickListener((adapter, view, position) -> {
//
//        });

    }


    @Override
    protected void initEvent() {
        homeFollowAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (homeFllowBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = homeFllowBean.getList().get(position).getAuthorId();
                        postFollowData(otherUserId,position);
                        break;
                    case R.id.follow_Img:
                        String commentId = homeFllowBean.getList().get(position).getArticleId();
                        postLikedData(commentId,position);
                        break;
                }
            }
        });


        homeFollowAdapter.setOnItemImgClickLister(new Follow_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                L.e("imgList>>>>"+ GsonUtil.getInstance().toJson(strings));
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }


    private void postFollowData(String otherUserId,int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView follow_Tv = (TextView) homeFollowAdapter.getViewByPosition(card_Recycler,position, R.id.follow_Tv);
                    TextView send_Mes = (TextView) homeFollowAdapter.getViewByPosition(card_Recycler,position, R.id.send_Mes);
                    homeFollowAdapter.isFollow(followStateBena.getFollowStatus(),follow_Tv,send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }
    private void postLikedData(String commentId,int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", commentId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Liked_Comment, "点赞/取消点赞评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView follow_Tv = (TextView) homeFollowAdapter.getViewByPosition(card_Recycler,position, R.id.follow_Img);
                    TextView send_Mes = (TextView) homeFollowAdapter.getViewByPosition(card_Recycler,position, R.id.send_Mes);
                    homeFollowAdapter.isFollow(followStateBena.getFollowStatus(),follow_Tv,send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

}
