package com.qingbo.monk.base;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;

/**
 * 带分页的基类
 */
public class BaseRecyclerViewFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
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

    protected void initRecyclerViewAndSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.animal_color));
        mSwipeRefreshLayout.setRefreshing(true);
    }


    @Override
    public void onLoadMoreRequested() {
        onLoadMoreRequested();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    public interface MySwipeRefreshLayoutAndLoadMoreListener{
        void onRefresh();
        void onLoadMoreRequested();
    }
}
