package com.qingbo.monk.base;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.xunda.lib.common.bean.BaseSplitIndexBean;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.CustomLoadMoreView;

/**
 * 带分页的基类Activity
 */
public abstract class BaseRecyclerViewSplitActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseQuickAdapter mAdapter;
    protected int page = 1;
    protected int limit = 12;


    @Override
    protected int getLayoutId() {
        return 0;
    }




    protected void initSwipeRefreshLayoutAndAdapter(String emptyToastText,int emptyViewImgResource,boolean isHaveRefresh) {
        mAdapter.setEmptyView(addEmptyView(emptyToastText, emptyViewImgResource));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        if (isHaveRefresh && mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.animal_color));
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }

    /**
     * 无数据 不展示空数据界面
     * @param isHaveRefresh
     */
    protected void initSwipeRefreshLayoutAndAdapter(boolean isHaveRefresh) {
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
        if (isHaveRefresh && mSwipeRefreshLayout!=null) {
            mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.animal_color));
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }



    /**
     * 全局处理分页的公共方法
     * @param obj  具体的分页对象  列表适配器
     * @param mAdapter
     */
    protected void handleSplitListData(BaseSplitIndexBean obj, BaseQuickAdapter mAdapter, int mPageSize) {
        if(obj!=null){
            int allCount = StringUtil.isBlank(obj.getCount())?0:Integer.parseInt(obj.getCount());
            int bigPage = 0;//最大页
            if(allCount%mPageSize!=0){
                bigPage=allCount/mPageSize+1;
            }else{
                bigPage=allCount/mPageSize;
            }
            if(page==bigPage){
                mAdapter.loadMoreEnd();//显示“没有更多数据”
            }

            boolean isRefresh = page==1?true:false;
            if(!ListUtils.isEmpty(obj.getList())){
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
            }else{

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
}
