package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.InterestList_Bean;
import com.qingbo.monk.person.adapter.MyInterestAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 转发到 ——我/他的兴趣组
 */
public class ForWardInterest_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private String userID;
    private String articleId;

    public static void actionStart(Context context, String userID, String biz_id) {
        Intent intent = new Intent(context, ForWardInterest_Activity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("biz_id", biz_id);
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
        articleId = getIntent().getStringExtra("biz_id");
    }


    @Override
    protected void initView() {
        if (isMe()) {
            title_bar.setTitle("我的兴趣组");
        } else {
            title_bar.setTitle("他的兴趣组");
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
        mAdapter = new MyInterestAdapter(isMe());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            InterestBean item = (InterestBean) adapter.getItem(position);
//            InterestDetail_Activity.startActivity(mActivity, "0", item.getId());
            String id = item.getId();
            startForWard(id);
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
        requestMap.put("userid", userID);
        HttpSender sender = new HttpSender(HttpUrl.Interest_My, "我的兴趣组", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            InterestList_Bean interestList_bean = new Gson().fromJson(json_data, InterestList_Bean.class);
                            handleSplitListData(interestList_bean, mAdapter, limit);
                        }
                    }

                }, true);

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

    /**
     * 转发到兴趣组 弹窗
     *
     * @param id
     */
    private void startForWard(String id) {
        new TwoButtonDialogBlue(mActivity, "确定转发到该兴趣组？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                postForwardingData(articleId, id);
            }

            @Override
            public void onClickLeft() {

            }
        }).show();
    }

    /**
     * 转发 到兴趣组
     *
     * @param articleId
     * @param //type 1社群 2兴趣组
     */
    private void postForwardingData(String articleId, String shequn_id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("biz_id", articleId);
        requestMap.put("shequn_id", shequn_id);
        requestMap.put("type", "2");
        HttpSender httpSender = new HttpSender(HttpUrl.ForWard_Group, "转发动态_社群/兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    finish();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}
