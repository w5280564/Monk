package com.qingbo.monk.person.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.MyDraftsList_Bean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.person.activity.MyCrateArticle_Avtivity;
import com.qingbo.monk.person.adapter.MyDraftsAdapter;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 草稿箱--创作中心
 */
public class MyDrafts_Crate_Fragment extends BaseRecyclerViewSplitFragment {
    @BindView(R.id.dingTop_Img)
    ImageView dingTop_Img;
    private String type;

    /**
     *
     * @param type
     * @return
     */
    public static MyDrafts_Crate_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        MyDrafts_Crate_Fragment fragment = new MyDrafts_Crate_Fragment();
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
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据",0,true);
    }


    @Override
    protected void loadData() {
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



    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("type", type);
        HttpSender sender = new HttpSender(HttpUrl.User_Drafts, "查看草稿箱", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            MyDraftsList_Bean myDraftsList_bean = GsonUtil.getInstance().json2Bean(json_data, MyDraftsList_Bean.class);
                            if (myDraftsList_bean != null) {
                                handleSplitListData(myDraftsList_bean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }




    private void initRecyclerView() {
        mAdapter = new MyDraftsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OwnPublishBean item = (OwnPublishBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.draft_Fa:
                        MyCrateArticle_Avtivity.actionStart(mActivity, item, true);
                        break;
                    case R.id.draft_Del:
                        showDeleteDialog(item.getId(), position);
                        break;

                }
            }
        });
    }

    private void showDeleteDialog(String mQuestionId, int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity, "确定删除此问答？", "取消", "确定",
                new TwoButtonDialogBlue.ConfirmListener() {

                    @Override
                    public void onClickRight() {
                        deleteQuestion(mQuestionId, position);
                    }

                    @Override
                    public void onClickLeft() {

                    }
                });

        mDialog.show();
    }

    /**
     * 删除话题
     * @param mQuestionId
     */
    private void deleteQuestion(String mQuestionId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", mQuestionId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.deleteTopic, "删除话题", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    mAdapter.remove(position);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



}
