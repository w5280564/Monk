package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.InterestDetail_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.InterestList_Bean;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.bean.MyGroupList_Bean;
import com.qingbo.monk.person.adapter.MyGroupAdapter;
import com.qingbo.monk.person.adapter.MyInterestAdapter;
import com.qingbo.monk.question.activity.GroupDetailActivity;
import com.qingbo.monk.question.adapter.QuestionGroupAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我/他的社群
 */
public class MyGroupList_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private String userID;

    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyGroupList_Activity.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.my_recyleview_refresh;
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getMyGroup();
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getMyGroup();
    }


    @Override
    protected void initLocalData() {
        userID = getIntent().getStringExtra("userID");
    }


    @Override
    protected void initView() {
        if (isMe()){
            title_bar.setTitle("我的社群");
        }else {
            title_bar.setTitle("他的社群");
        }
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }



    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyGroupAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyCardGroup_Bean obj = (MyCardGroup_Bean) adapter.getItem(position);
                GroupDetailActivity.actionStart(mActivity, obj.getId());
            }
        });
    }


    @Override
    protected void getServerData() {
        getMyGroup();
    }

    private void getMyGroup() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("userID", userID);
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            MyGroupList_Bean myGroupList_bean =  GsonUtil.getInstance().json2Bean(json_data, MyGroupList_Bean.class);
                            handleSplitListData(myGroupList_bean, mAdapter, limit);
                        }
                    }

                }, false);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    /**
     * 是否是我自己
     *
     * @return
     */
    private boolean isMe() {
        String id = PrefUtil.getUser().getId();
        if (TextUtils.equals(userID, id)) {
            return true;
        }
        return false;
    }





}
