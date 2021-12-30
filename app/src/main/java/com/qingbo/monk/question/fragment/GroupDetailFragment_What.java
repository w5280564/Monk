package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.bean.AskQuestionBean;
import com.qingbo.monk.question.activity.AskQuestionToPeopleActivity;
import com.qingbo.monk.question.adapter.GroupDetailListAdapterWhat;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import java.util.HashMap;
import butterknife.BindView;

/**
 * 社群详情（去提问）
 */
public class GroupDetailFragment_What extends BaseLazyFragment {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String id;
    private GroupDetailListAdapterWhat mAdapter;

    public static GroupDetailFragment_What NewInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        GroupDetailFragment_What fragment = new GroupDetailFragment_What();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_detail_what;
    }


    @Override
    protected void initLocalData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            id = mBundle.getString("id");
        }
    }

    @Override
    protected void initView(View mView) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailListAdapterWhat();
        mAdapter.setEmptyView(addEmptyView("暂无提问", R.mipmap.zhuti));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AskQuestionBean mAskQuestionBean = (AskQuestionBean) adapter.getItem(position);
                if (mAskQuestionBean==null) {
                    return;
                }

                AskQuestionToPeopleActivity.actionStart(mActivity,mAskQuestionBean.getNickname(),mAskQuestionBean.getId(),id);
            }
        });
    }


    @Override
    protected void loadData() {
        getShequnToQuestionList();
    }

    /**
     * 去提问列表
     */
    private void getShequnToQuestionList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("shequn_id", id);
        HttpSender sender = new HttpSender(HttpUrl.getGroupToQuestionList, "去提问列表", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleData(json);
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData(String json) {
        HttpBaseList<AskQuestionBean> objList = GsonUtil

                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<AskQuestionBean>>() {
                        }.getType());
        mAdapter.setNewData(objList.getData());
    }


}
