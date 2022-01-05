package com.qingbo.monk.question.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.bean.GroupBean;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

/**
 * 更多社群问答
 */
public class AllGroupListActivity extends BaseRecyclerViewSplitActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_group;
    }


    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据",0,true);
    }

    @Override
    protected void initLocalData() {
        registerEventBus();
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if(event.type == FinishEvent.JOIN_GROUP||event.type == FinishEvent.EXIT_GROUP){
            page = 1;
            getAllGroup(true);
        }
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupBean mGroupBean = (GroupBean) adapter.getItem(position);
                if (mGroupBean==null) {
                    return;
                }
                CheckOtherGroupDetailActivity.actionStart(mActivity,mGroupBean.getId());
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void getServerData() {
        getAllGroup(true);
    }


    private void getAllGroup(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit+"");
        HttpSender sender = new HttpSender(HttpUrl.allGroup, "全部社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseGroupBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseGroupBean.class);
                            handleSplitListData(obj,mAdapter,limit);
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }



    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getAllGroup(false);
    }
}
