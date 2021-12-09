package com.qingbo.monk.base;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.xunda.lib.common.view.CustomLoadMoreView;

/**
 * 带分页的基类Fragment
 */
public abstract class BaseRecyclerViewFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return 0;
    }



    @Override
    protected void initEvent() {
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    protected void initSwipeRefreshLayoutAndAdapter(String emptyViewToast) {
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.animal_color));
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter.setEmptyView(addEmptyView(emptyViewToast, 0));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
    }


    @Override
    public void onLoadMoreRequested() {
        onLoadMoreData();
    }

    @Override
    public void onRefresh() {
        onRefreshData();
    }


    /**
     * 下拉刷新
     */
    protected abstract void onRefreshData();


    /**
     * 上拉分页
     */
    protected abstract void onLoadMoreData();
}
