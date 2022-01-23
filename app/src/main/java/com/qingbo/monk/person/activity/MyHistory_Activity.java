package com.qingbo.monk.person.activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter;
import com.baozi.treerecyclerview.adpater.TreeRecyclerType;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.MyTestHis_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 我的最近浏览
 */
public class MyHistory_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar titleBar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private TreeRecyclerAdapter treeRecyclerAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.my_recyleview_refresh;
    }

    @Override
    protected void initView() {
        titleBar.setTitle("最近浏览");
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new mSwipeRefreshLayoutView());
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity,R.color.animal_color));
        initRecyclerView();
//        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
        getExpertList(false);
    }
    private class mSwipeRefreshLayoutView implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            page = 1;
            getExpertList(false);
        }
    }

    @Override
    protected void onRefreshData() {
//        page = 1;
//        getExpertList(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getExpertList(false);
    }


    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.User_History_All, "浏览记录", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            MyTestHis_Bean myTestHis_bean = new Gson().fromJson(json_root, MyTestHis_Bean.class);
                            if (myTestHis_bean != null){
                                List<TreeItem> items = ItemHelperFactory.createItems(myTestHis_bean.getData());
                                //添加到adapter
                                treeRecyclerAdapter.getItemManager().replaceAllItem(items);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mRecyclerView.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.hui));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //可折叠
        treeRecyclerAdapter = new TreeRecyclerAdapter(TreeRecyclerType.SHOW_ALL);
        mRecyclerView.setAdapter(treeRecyclerAdapter);
    }




}