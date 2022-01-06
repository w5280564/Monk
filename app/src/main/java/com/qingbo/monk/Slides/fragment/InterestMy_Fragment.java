package com.qingbo.monk.Slides.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.InterestDetail_Activity;
import com.qingbo.monk.Slides.adapter.Interest_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.InterestList_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

/**
 * 侧边栏-兴趣圈-我的兴趣圈
 */
public class InterestMy_Fragment extends BaseRecyclerViewSplitFragment {

    private String type;

    /**
     * @param
     * @return
     */
    public static InterestMy_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        InterestMy_Fragment fragment = new InterestMy_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        super.initLocalData();
        type = getArguments().getString("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_interest;
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, false);
    }

    @Override
    protected void loadData() {
        getListData(true);
    }


    InterestList_Bean interestList_bean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        if (TextUtils.isEmpty(PrefUtil.getUser().getId())) {

        }
//        String id = PrefUtil.getUser().getId();
//        requestMap.put("userid", id);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_My, "我的兴趣圈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                interestList_bean = new Gson().fromJson(json_data, InterestList_Bean.class);
                if (interestList_bean != null) {
                    handleSplitListData(interestList_bean, mAdapter, limit);
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false);
    }


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Interest_Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InterestBean item = (InterestBean) adapter.getItem(position);
                InterestDetail_Activity.startActivity(requireActivity(),"0",item.getId());
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.join_Tv:
                        InterestBean item = (InterestBean) adapter.getItem(position);
                        changeJoin(item.getJoinStatus(), position);
                        getJoin(item.getId());
                        break;
                }
            }
        });
//        onBackTop(dingTop_Img);

    }

    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣圈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 修改加入状态
     *
     * @param stateIndex 1已加入 其他都是未加入
     * @param position
     */
    private void changeJoin(String stateIndex, int position) {
        if (interestList_bean != null) {
            TextView join_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.join_Tv);
            if (TextUtils.equals(stateIndex, "1")) {
                stateIndex = "0";
            } else {
                stateIndex = "1";
            }
            interestList_bean.getList().get(position).setJoinStatus(stateIndex);
            ((Interest_Adapter) mAdapter).isJoin(stateIndex, join_Tv);
        }
    }


}
