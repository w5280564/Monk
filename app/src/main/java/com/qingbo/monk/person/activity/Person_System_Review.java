package com.qingbo.monk.person.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.SystemReview_Bean;
import com.qingbo.monk.bean.SystemReview_List_Bean;
import com.qingbo.monk.person.adapter.MySystem_Review_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

public class Person_System_Review extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.card_Recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;

    @Override
    protected int getLayoutId() {
        return R.layout.person_system_liked;
    }


    @Override
    protected void initView() {
        title_bar.setTitle("系统审核");
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
        getExpertList(true);
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


    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.System_CheckList, "系统审核—审核列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            SystemReview_List_Bean systemReview_list_bean = GsonUtil.getInstance().json2Bean(json_data, SystemReview_List_Bean.class);
                            if (systemReview_list_bean != null) {
                                handleSplitListData(systemReview_list_bean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new MySystem_Review_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            SystemReview_Bean item = (SystemReview_Bean) adapter.getItem(position);
//            String likeType = item.getLikeType();
//            String articleId="";
//            if (TextUtils.equals(likeType ,"1")){
//                articleId = item.getContent().getId();
//            }else {
//                 articleId = item.getContent().getArticleId();
//            }
//            if (!TextUtils.isEmpty(articleId)) {
//                String type = "";
//                ArticleDetail_Activity.startActivity(this, articleId, "0", type);
//            }
        });
    }

}