package com.qingbo.monk.Slides.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.Expert_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

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
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0,true);
    }


    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(false);
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getExpertList(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getExpertList(false);
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
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
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
        mAdapter = new Expert_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(this, articleId, "0", type,true);
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.group_Img:
                        HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
                        startPerson(item);
                        break;
                }
            }
        });

        ((Expert_Adapter) mAdapter).setOnItemImgClickLister(new Expert_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    /**
     * 人物跳转
     * @param item
     */
    private void startPerson(HomeFllowBean item) {
        String data_source = item.getData_source();//1是虚拟人物,为0是注册用户
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getAuthorName();
            String id = item.getAuthorId();
            SideslipPersonDetail_Activity.startActivity(mActivity, nickname, id, "0");
        }else {
            String id = item.getAuthorId();
            MyAndOther_Card.actionStart(mActivity, id,true);
        }
    }



}