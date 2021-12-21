package com.qingbo.monk.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.xunda.lib.common.bean.BaseSplitIndexBean;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.CustomLoadMoreView;

/**
 * 带分页的基类Fragment
 */
public abstract class BaseRecyclerViewSplitFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;
    protected int page = 1;
    protected int limit = 12;


    @Override
    protected int getLayoutId() {
        return 0;
    }


    protected void initSwipeRefreshLayoutAndAdapter(String emptyViewToast, boolean isHaveRefresh) {
        mAdapter.setEmptyView(addEmptyView(emptyViewToast, 0));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        if (isHaveRefresh && mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }


    /**
     * 全局处理分页的公共方法
     *
     * @param obj      具体的分页对象  列表适配器
     * @param mAdapter
     */
    protected void handleSplitListData(BaseSplitIndexBean obj, BaseQuickAdapter mAdapter, int mPageSize) {
        if (obj != null) {
            int allCount = StringUtil.isBlank(obj.getCount()) ? 0 : Integer.parseInt(obj.getCount());
            int bigPage = 0;//最大页
            if (allCount % mPageSize != 0) {
                bigPage = allCount / mPageSize + 1;
            } else {
                bigPage = allCount / mPageSize;
            }
            if (page == bigPage) {
                mAdapter.loadMoreEnd();//显示“没有更多数据”
            }

            boolean isRefresh = page == 1 ? true : false;
            if (!ListUtils.isEmpty(obj.getList())) {
                int size = obj.getList().size();

                if (isRefresh) {
                    mAdapter.setNewData(obj.getList());
                } else {
                    mAdapter.addData(obj.getList());
                }


                if (size < mPageSize) {
                    mAdapter.loadMoreEnd(isRefresh);//第一页的话隐藏“没有更多数据”，否则显示“没有更多数据”
                } else {
                    mAdapter.loadMoreComplete();
                }
            } else {

                if (isRefresh) {
                    mAdapter.setNewData(null);//刷新列表
                } else {
                    mAdapter.loadMoreEnd(false);//显示“没有更多数据”
                }
            }
        }
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

    /**
     * 按钮点击返回顶部 显示与隐藏
     */
    public void onBackTop(View dingTopView) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert manager != null;
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                //停止滑动
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {  //没有超过一屏幕
                        //这里隐藏是为了滑动之后显示了,在手动滑动到首页了,就隐藏
                        dingTopView.setVisibility(View.GONE);
                    } else {
                        dingTopView.setVisibility(View.VISIBLE);
                        dingTopView.setOnClickListener(v -> {
                            recyclerView.smoothScrollToPosition(0);
                            dingTopView.setVisibility(View.GONE);
                        });
                    }
                }
            }
        });
    }

}
