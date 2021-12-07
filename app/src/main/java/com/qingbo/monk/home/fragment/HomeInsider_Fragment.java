package com.qingbo.monk.home.fragment;

import android.content.Context;
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
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.InsiderListBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.adapter.Follow_Adapter;
import com.qingbo.monk.home.adapter.Insider_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.view.CustomLoadMoreView;

import java.util.HashMap;
import java.util.List;

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
        getListData();
    }

    int page = 1;
    protected int pageSize = 10;
    InsiderListBean insiderListBean;

    private void getListData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", pageSize + "");
        requestMap.put("type", "1");
        HttpSender httpSender = new HttpSender(HttpUrl.Insider_List, "首页-内部人", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                     insiderListBean = GsonUtil.getInstance().json2Bean(json_data, InsiderListBean.class);
                    if (insiderListBean != null) {
                        handleSplitListData(insiderListBean, insider_adapter, pageSize);
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
//        homeFollowAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                if (homeFllowBean == null) {
//                    return;
//                }
//                switch (view.getId()) {
//                    case R.id.follow_Tv:
//                        String otherUserId = homeFllowBean.getList().get(position).getAuthorId();
//                        postFollowData(otherUserId, position);
//                        break;
//                    case R.id.follow_Img:
//                        String likeId = homeFllowBean.getList().get(position).getArticleId();
//                        postLikedData(likeId, position);
//                        break;
//                }
//            }
//        });


//        homeFollowAdapter.setOnItemImgClickLister(new Follow_Adapter.OnItemImgClickLister() {
//            @Override
//            public void OnItemImgClickLister(int position, List<String> strings) {
//                L.e("imgList>>>>" + GsonUtil.getInstance().toJson(strings));
//                jumpToPhotoShowActivity(position, strings);
//            }
//        });
    }


}
