package com.qingbo.monk.question.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

/**
 * 更多社群问答
 */
public class AllGroupListActivity extends BaseRecyclerViewSplitActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_group;
    }


    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据");
    }

    @Override
    protected void initEvent() {
        super.initEvent();//一定要写
    }

    private void initRecyclerView() {
        mAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void getServerData() {
        getAllGroup(true);
    }


    private void getAllGroup(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit+"");
        HttpSender sender = new HttpSender(HttpUrl.allGroup, "全部社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page==1&&mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseGroupBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseGroupBean.class);
                            handleSplitListData(obj,mAdapter,limit);
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }



    @Override
    protected void onRefreshData() {
        page = 1;
        getAllGroup(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getAllGroup(false);
    }
}
