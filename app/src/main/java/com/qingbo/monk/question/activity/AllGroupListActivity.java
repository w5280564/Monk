package com.qingbo.monk.question.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewActivity;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.CustomLoadMoreView;
import java.util.HashMap;
import butterknife.BindView;

/**
 * 更多社群问答
 */
public class AllGroupListActivity extends BaseRecyclerViewActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private QuestionGroupAdapter mQuestionGroupAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_group;
    }


    @Override
    protected void initView() {
        initRecyclerView();
    }

    @Override
    protected void initEvent() {
        mQuestionGroupAdapter.setOnLoadMoreListener(this,mRecyclerView);
    }

    private void initRecyclerView() {
        mQuestionGroupAdapter = new QuestionGroupAdapter();
        mQuestionGroupAdapter.setEmptyView(addEmptyView("暂无数据", 0));
        mQuestionGroupAdapter.setLoadMoreView(new CustomLoadMoreView());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mQuestionGroupAdapter);
    }


    @Override
    protected void getServerData() {
        getAllGroup(true);
    }


    private void getAllGroup(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", pageSize+"");
        HttpSender sender = new HttpSender(HttpUrl.allGroup, "全部社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page==1&&mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseGroupBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseGroupBean.class);
                            handleSplitListData(obj,mQuestionGroupAdapter,pageSize);
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }



    @Override
    protected void onRefreshData() {
        page = 1;
        getAllGroup(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getAllGroup(false);
    }
}
