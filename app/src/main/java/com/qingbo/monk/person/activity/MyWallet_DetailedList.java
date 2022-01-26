package com.qingbo.monk.person.activity;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.WalletDetailed_Bean;
import com.qingbo.monk.bean.WalletDetailed_ListBean;
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
    @BindView(R.id.month_Tv)
    TextView month_Tv;
    @BindView(R.id.income_Tv)
    TextView income_Tv;
    @BindView(R.id.refund_Tv)
    TextView refund_Tv;
    @BindView(R.id.withdraw_Tv)
    TextView withdraw_Tv;

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

                                originalValue(walletDetailed_listBean.getMonth(), "", "", month_Tv);
                                originalValue(walletDetailed_listBean.getIncome(), "", "月收入 ￥", income_Tv);
                                originalValue(walletDetailed_listBean.getRefund(), "", "退款 ￥", refund_Tv);
                                originalValue(walletDetailed_listBean.getWithdraw(), "", "提现 ￥", withdraw_Tv);
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
            MyWallet_DetailedList_Detail.actionStart(mActivity, item.getOrderId());
        });

    }

    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + (CharSequence) value);
        }
    }

}