package com.qingbo.monk.person.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.ArticleLikedListBean;
import com.qingbo.monk.bean.WalletDetailed_Bean;
import com.qingbo.monk.bean.WalletDetailed_ListBean;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.qingbo.monk.person.adapter.MyWallet_Detailed_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

public class MyWallet_DetailedList extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.fragment_background)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.mywallet_moneylist;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getExpertList(true);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getExpertList(false);
    }

    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(false);
    }



    WalletDetailed_ListBean walletDetailed_listBean;
    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Wallet_List, "交易记录", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                             walletDetailed_listBean = GsonUtil.getInstance().json2Bean(json_data, WalletDetailed_ListBean.class);
                            if (walletDetailed_listBean != null) {
                                handleSplitListData(walletDetailed_listBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void initRecyclerView() {
        mAdapter = new MyWallet_Detailed_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            WalletDetailed_Bean item = (WalletDetailed_Bean) adapter.getItem(position);
            MyWallet_DetailedList_Detail.actionStart(mActivity,item.getOrder_id());
        });

    }

}