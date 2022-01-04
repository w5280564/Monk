package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseOwnPublishBean;
import com.qingbo.monk.question.adapter.GroupDetailThemeListAdapterChoose;
import com.qingbo.monk.question.adapter.GroupDetailTopicListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

/**
 * 选择主题
 */
public class ChooseThemeActivity extends BaseRecyclerViewSplitActivity {


    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_theme;
    }


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, ChooseThemeActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无主题",R.mipmap.zhuti, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailThemeListAdapterChoose();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initLocalData() {
       id = getIntent().getStringExtra("id");
    }

    @Override
    protected void getServerData() {
        getThemeList();
    }



    @Override
    protected void onRefreshData() {
        page = 1;
        getThemeList();
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getThemeList();
    }



    private void getThemeList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("action", "1");
        map.put("page", page + "");
        map.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.groupDetailAllTab, "查看主题", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseOwnPublishBean obj = GsonUtil.getInstance().json2Bean(data, BaseOwnPublishBean.class);
                            handleSplitListData(obj, mAdapter, limit);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }
}
