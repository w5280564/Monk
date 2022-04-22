package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.qingbo.monk.bean.MyGroupList_Bean;
import com.qingbo.monk.person.adapter.MyGroupAdapter;
import com.qingbo.monk.question.activity.CreateGroupStepOneActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 转发到 ——我/他的社群
 */
public class ForWardGroup_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private String userID;
    String type;
    private TextView myCrate_Tv, group_Tv;
    private String articleId;
    private String op_type;

    /**
     *
     * @param context
     * @param userID
     * @param biz_id 文章id或者仓位组合ID
     * @param op_type 0：文章类【默认】 1：仓位组合
     */
    public static void actionStart(Context context, String userID, String biz_id, String op_type) {
        Intent intent = new Intent(context, ForWardGroup_Activity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("biz_id", biz_id);
        intent.putExtra("op_type", op_type);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.group_list;
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getMyGroup(type);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getMyGroup(type);
    }


    @Override
    protected void initLocalData() {
        userID = getIntent().getStringExtra("userID");
        articleId = getIntent().getStringExtra("biz_id");
        op_type = getIntent().getStringExtra("op_type");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMe()) {
//            getMyGroupHead("1");
            type = "2";
        } else {
            type = "3";
        }
        getMyGroup(type);
    }

    @Override
    public void onRightClick() {
        skipAnotherActivity(CreateGroupStepOneActivity.class);
    }

    @Override
    protected void initView() {
        if (isMe()) {
            title_bar.setTitle("我的问答社群");
        } else {
            title_bar.setTitle("他的问答社群");
        }
        title_bar.hideRight();
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
        mAdapter = new MyGroupAdapter(isMe());
        mRecyclerView.setAdapter(mAdapter);
        addHeadView();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyCardGroup_Bean mGroupBean = (MyCardGroup_Bean) adapter.getItem(position);
            if (mGroupBean == null) {
                return;
            }
            String id = mGroupBean.getId();
            startForWard(id);
        });
    }

    MyGroupAdapter myGroupAdapter;

    /**
     * 列表第一列改为我创建的群
     */
    private void addHeadView() {
        View myView = LayoutInflater.from(this).inflate(R.layout.mygrouplist_top, null);
        ConstraintLayout top_Con = myView.findViewById(R.id.top_Con);
        myCrate_Tv = myView.findViewById(R.id.myCrate_Tv);
        group_Tv = myView.findViewById(R.id.group_Tv);

        if (isMe()) {
            top_Con.setVisibility(View.VISIBLE);
            group_Tv.setText("我加入的社群");

        } else {
            group_Tv.setText("他创建的社群");
        }


        RecyclerView top_mRecyclerView = myView.findViewById(R.id.top_mRecyclerView);
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        top_mRecyclerView.setLayoutManager(mMangaer);
        myGroupAdapter = new MyGroupAdapter(isMe());
        top_mRecyclerView.setAdapter(myGroupAdapter);
        mAdapter.addHeaderView(myView);

        myGroupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isMe()) {
                    MyCardGroup_Bean obj = (MyCardGroup_Bean) adapter.getItem(position);
                    String id = obj.getId();
                    startForWard(id);
                }
            }
        });
    }

    /**
     * 转发到社群 弹窗
     *
     * @param id
     */
    private void startForWard(String id) {
        new TwoButtonDialogBlue(mActivity, "确定转发到该社群？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                postForwardingData(articleId, id);
            }

            @Override
            public void onClickLeft() {

            }
        }).show();
    }


    //我创建的群
    MyGroupList_Bean myGroupList_bean;
    private void getMyGroupHead(String type) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("userid", userID);
        requestMap.put("type", type);
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我创建的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            myGroupList_bean = GsonUtil.getInstance().json2Bean(json_data, MyGroupList_Bean.class);
                            myGroupAdapter.setNewData(myGroupList_bean.getList());
//                            handleSplitListData(myGroupList_bean, myGroupAdapter, limit);
                            String format = String.format("我创建的社群(%1$s/8)", myGroupList_bean.getList().size());
                            myCrate_Tv.setText(format);
                        }
                    }

                }, false);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void getMyGroup(String type) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("userid", userID);
        requestMap.put("type", type);
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            MyGroupList_Bean myGroupList_bean = GsonUtil.getInstance().json2Bean(json_data, MyGroupList_Bean.class);
                            handleSplitListData(myGroupList_bean, mAdapter, limit);
                            if (isMe()) {
                                group_Tv.setText("我加入的社群");
                            } else {
                                String format = String.format("他创建的社群(%1$s/8)", myGroupList_bean.getList().size());
                                group_Tv.setText(format);
                            }
                            if (page == 1 && isMe()) {
                                getMyGroupHead("1");
                            }
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


    /**
     * 转发 到社群
     *
     * @param articleId
     * @param //type 1社群 2兴趣组
     */
    private void postForwardingData(String articleId, String shequn_id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("biz_id", articleId);
        requestMap.put("shequn_id", shequn_id);
        requestMap.put("type", "1");
        requestMap.put("op_type", op_type);
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
