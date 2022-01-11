package com.qingbo.monk.home.fragment;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.WebSocketHelper;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseMessageRecordBean;
import com.qingbo.monk.bean.MessageRecordBean;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.message.activity.ContactListActivity;
import com.qingbo.monk.message.adapter.MessageListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;

import java.util.HashMap;
import butterknife.OnClick;

/**
 * 会话列表
 */
public class MessageFragment extends BaseRecyclerViewSplitFragment implements BaseQuickAdapter.OnItemClickListener, WebSocketHelper.OnReceiveMessageListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }


    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无消息", 0,true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MessageListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
        WebSocketHelper.getInstance().setReceiveMessageListener(this);
    }

    @OnClick({R.id.seek_Tv, R.id.ll_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seek_Tv:
                break;
            case R.id.ll_contact:
                skipAnotherActivity(ContactListActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MessageRecordBean mMessageBean = (MessageRecordBean) adapter.getItem(position);
        if (mMessageBean == null) {
            return;
        }

        ChatActivity.actionStart(mActivity,mMessageBean.getId(),mMessageBean.getNickname(),mMessageBean.getAvatar());
    }


    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        conversationList(false);
    }

    @Override
    protected void loadData() {
        conversationList(true);
    }


    /**
     * 会话列表
     */
    private void conversationList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", 40 + "");
        HttpSender sender = new HttpSender(HttpUrl.conversationList, "会话列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseMessageRecordBean mObj = GsonUtil.getInstance().json2Bean(json_data, BaseMessageRecordBean.class);
                            handleSplitListData(mObj, mAdapter, 40);
                        }
                    }

                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }



    @Override
    protected void onRefreshData() {
        page = 1;
        conversationList(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        conversationList(false);
    }

    @Override
    public void onReceiveMessage(ReceiveMessageBean receiveObj) {
        L.e("websocket","会话列表页接收消息");
        page = 1;
        conversationList(false);
    }
}
