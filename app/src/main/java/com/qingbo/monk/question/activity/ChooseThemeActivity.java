package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseOwnPublishBean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.question.adapter.GroupDetailThemeListAdapterChoose;
import com.qingbo.monk.question.adapter.QuestionListAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择主题
 */
public class ChooseThemeActivity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.tv_choose)
    TextView tv_choose;
    private List<String> mChooseIsList = new ArrayList<>();
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
        initSwipeRefreshLayoutAndAdapter("暂无主题",R.mipmap.icon_no_date, true);
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
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OwnPublishBean obj = (OwnPublishBean) adapter.getItem(position);
                if (obj!=null) {
                    if (!obj.isCheck()) {
                        mChooseIsList.add(obj.getArticleId());
                        obj.setCheck(true);
                    }else{
                        mChooseIsList.remove(obj.getArticleId());
                        obj.setCheck(false);
                    }
                    tv_choose.setText(mChooseIsList.size()==0?"选择":String.format("已选择(%s)",mChooseIsList.size()));
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

        ((GroupDetailThemeListAdapterChoose) mAdapter).setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
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
        map.put("theme", "1");
        HttpSender sender = new HttpSender(HttpUrl.groupDetailAllTab, "查看主题", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mChooseIsList.clear();
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


    @OnClick({R.id.rl_back,R.id.tv_cancel,R.id.tv_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
            case R.id.tv_cancel:
                back();
                break;
            case R.id.tv_choose:
                if (ListUtils.isEmpty(mChooseIsList)) {
                    T.ss("请选择主题");
                    return;
                }
                editTheme();
                break;

        }
    }


    private void editTheme() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", StringUtil.listToString(mChooseIsList));
        map.put("status", "1");
        HttpSender sender = new HttpSender(HttpUrl.editTheme, "编辑主题", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.CHOOSE_THEME));
                            showToastDialog("操作成功！");
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }




}
