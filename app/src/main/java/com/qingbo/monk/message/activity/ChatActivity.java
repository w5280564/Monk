package com.qingbo.monk.message.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.BaseReceiveMessageBean;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.qingbo.monk.bean.SendMessageBean;
import com.qingbo.monk.message.adapter.ChatAdapter;
import com.xunda.lib.common.bean.BaseSplitIndexBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.CustomLoadMoreView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天页
 */
public class ChatActivity extends BaseCameraAndGalleryActivity_Single implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private static final String TAG = "websocket";
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.et_content)
    EditText etContent;
    private WebSocketService webSocketService;
    private List<String> mSensitiveWordList = new ArrayList<>();//敏感词词汇列表
    private ChatAdapter mAdapter;
    private List<ReceiveMessageBean> mList = new ArrayList<>();
    private String id,header,name;
    private int page = 1;
    private int limit = 20;
    private String selfUserId;


    public static void actionStart(Context context, String id,String name,String header) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("header",header);
        context.startActivity(intent);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }


    @Override
    protected void initLocalData() {
        selfUserId = SharePref.user().getUserId();
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        header = getIntent().getStringExtra("header");
        title = name;

        List<String> objList = GsonUtil.getInstance().parseDataNoKey(SharePref.local().getSensitiveWordsListJson());
        if (!ListUtils.isEmpty(objList)) {
            mSensitiveWordList.clear();
            mSensitiveWordList.addAll(objList);
        }
    }

    /**
     * 屏蔽敏感词汇
     * @param sendBeforeMessage
     */
    private String forbidSensitiveWord(String sendBeforeMessage) {
        if (!ListUtils.isEmpty(mSensitiveWordList)) {
            for (int i = 0; i < mSensitiveWordList.size(); i++) {
                if (sendBeforeMessage.contains(mSensitiveWordList.get(i))) {
                    sendBeforeMessage = sendBeforeMessage.replaceAll(mSensitiveWordList.get(i), "***");
                }
            }
            return sendBeforeMessage;
        }
        return "";
    }


    @Override
    protected void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter(mList);
        mAdapter.setEmptyView(addEmptyView("暂无消息", 0));
        mRecyclerView.setAdapter(mAdapter);
        initWebSocket();
    }

    @Override
    protected void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this,mRecyclerView);
    }

    private void initWebSocket() {
        bindService(new Intent(this, WebSocketService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void getServerData() {
        getMessageList(true);
    }



    /**
     * 聊天记录
     */
    private void getMessageList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userid", id);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.messageList, "聊天记录", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseReceiveMessageBean mObj = GsonUtil.getInstance().json2Bean(json_data, BaseReceiveMessageBean.class);
                            handleSplitListData(mObj);
                        }
                    }

                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }




    /**
     * 分页
     */
    private void handleSplitListData(BaseReceiveMessageBean obj) {
        if (obj != null) {
            List<ReceiveMessageBean> mTempList = obj.getList();
            if (!ListUtils.isEmpty(mTempList)) {
                for (ReceiveMessageBean mObj:mTempList) {
                    if (!StringUtil.isBlank(mObj.getFrom())) {
                        if (mObj.getFrom().equals(selfUserId)) {
                            mObj.setType(ReceiveMessageBean.CHAT_TYPE_SEND);
                        }else{
                            mObj.setType(ReceiveMessageBean.CHAT_TYPE_RECEIVER);
                            mObj.setFromHeader(header);
                        }
                    }
                }
                if (ListUtils.isEmpty(mAdapter.getData())) {
                    mAdapter.setNewData(mTempList);
                }else{
                    mAdapter.addData(mTempList);
                }
            }
        }
    }


    @OnClick({R.id.ll_image,R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_image:
                checkGalleryPermission();
                break;
            case R.id.tv_send:
                String content = StringUtil.getEditText(etContent);
                if (StringUtil.isBlank(content)) {
                    T.ss("请输入聊天内容");
                    return;
                }

                if (webSocketService != null) {
                    SendMessageBean mSendMessageBean = new SendMessageBean();
                    mSendMessageBean.setMessage(etContent.getText().toString());
                    mSendMessageBean.setFrom(SharePref.user().getUserId());
                    mSendMessageBean.setTo(id);
                    mSendMessageBean.setMsgType("text");
                    mSendMessageBean.setFlag("msg");
                    webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));

                    addContentToList(forbidSensitiveWord(content), ReceiveMessageBean.CHAT_TYPE_SEND);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }




    /**
     * 往消息体里增加数据
     * @param content 消息内容
     * @param chatType 消息类型
     */
    private void addContentToList(String content, int chatType) {
        ReceiveMessageBean obj = new ReceiveMessageBean();
        obj.setMessage(content);
        obj.setMsgType("text");
        obj.setFrom(id);
        obj.setType(chatType);
        obj.setFromName(name);
        mList.add(obj);
    }



    @Override
    public void onRightClick() {
        back();
    }

    @Override
    protected void onUploadSuccess(String imageString) {

    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getMessageList(false);
    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        getMessageList(false);
    }


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>聊天相关>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            webSocketService = ((WebSocketService.LocalBinder) service).getService();
            webSocketService.setWebSocketCallback(webSocketCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            webSocketService = null;
        }
    };

    private WebSocketService.WebSocketCallback webSocketCallback = new WebSocketService.WebSocketCallback() {
        @Override
        public void onMessage(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    L.e(TAG,"接收到消息>>>"+text);
                    if (text==null) {
                        return;
                    }

                    ReceiveMessageBean receiveObj = GsonUtil.getInstance().json2Bean(text,ReceiveMessageBean.class);
                    if (receiveObj==null) {
                        return;
                    }
                    if (!"-1".equals(receiveObj.getFrom())) {
                        mList.add(receiveObj);
                    }

                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onOpen() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SendMessageBean mSendMessageBean = new SendMessageBean();
                    mSendMessageBean.setFrom(SharePref.user().getUserId());
                    mSendMessageBean.setFlag("init");
                    webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));
                    L.e(TAG,"onOpen>>初始化");
                }
            });
        }

        @Override
        public void onClosed() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    L.e(TAG,"onClosed");
                }
            });
        }
    };


}

