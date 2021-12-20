package com.qingbo.monk.Slides.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

/**
 * 侧滑专家
 */
public class SideslipExpert_Activity extends BaseRecyclerViewSplitActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_expert;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", true);
    }


    @Override
    protected void getServerData() {
        getExpertList(true);
    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
    }

    FollowListBean homeFllowBean;
    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Expert_List, "侧滑—专家", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {

                            homeFllowBean = GsonUtil.getInstance().json2Bean(json_data, FollowListBean.class);
                            if (homeFllowBean != null) {
                                handleSplitListData(homeFllowBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Focus_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(this, articleId, "0", type);
        });

    }


}