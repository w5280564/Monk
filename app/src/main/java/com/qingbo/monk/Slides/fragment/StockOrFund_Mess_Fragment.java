package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.StockFundMes_Bean;
import com.qingbo.monk.bean.StockFundMes_ListBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.StockFund_Mess_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 侧边栏 个股/基金--资讯
 */
public class StockOrFund_Mess_Fragment extends BaseRecyclerViewSplitFragment {


    private String name, code;

    public static StockOrFund_Mess_Fragment newInstance(String name, String code) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("code", code);
        StockOrFund_Mess_Fragment fragment = new StockOrFund_Mess_Fragment();
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
        initSwipeRefreshLayoutAndAdapter("暂无资讯", false);
    }

    @Override
    protected void initLocalData() {
        name = getArguments().getString("name");
        code = getArguments().getString("code");
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getListData(true);
    }

    StockFundMes_ListBean stockFundMes_listBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("name", name + "");
        requestMap.put("code", code + "");
        HttpSender httpSender = new HttpSender(HttpUrl.StockOrFund_Message, "个股/基金--资讯", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    stockFundMes_listBean = GsonUtil.getInstance().json2Bean(json_data, StockFundMes_ListBean.class);
                    if (stockFundMes_listBean != null) {
                        handleSplitListData(stockFundMes_listBean, mAdapter, limit);
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
        mAdapter = new StockFund_Mess_Adapter();
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (stockFundMes_listBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Img:
                        String likeId = stockFundMes_listBean.getList().get(position).getId();
                        postLikedData(likeId, position);
                        break;
                }
            }
        });

        ((StockFund_Mess_Adapter) mAdapter).setOnItemImgClickLister(new StockFund_Mess_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }




    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.FundStock_MesLike, "资讯点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Img);
                    TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Count);
                    if (likedStateBena != null) {
                        //0取消点赞成功，1点赞成功
                        int nowLike;
                        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
                        if (likedStateBena.getLiked_status() == 0) {
                            nowLike -= 1;
                            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
                        } else if (likedStateBena.getLiked_status() == 1) {
                            follow_Img.setBackgroundResource(R.mipmap.dianzan);
                            nowLike += 1;
                        }
                        follow_Count.setText(nowLike + "");
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

}
