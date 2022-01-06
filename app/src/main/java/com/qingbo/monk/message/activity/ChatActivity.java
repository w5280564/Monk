package com.qingbo.monk.message.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.SendMessageBean;
import com.qingbo.monk.bean.ServiceChatBean;
import com.qingbo.monk.bean.ServiceChatIndexBean;
import com.qingbo.monk.message.adapter.ChatAdapter;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天页
 */
public class ChatActivity extends BaseCameraAndGalleryActivity_Single {
    private static final String TAG = "websocket";
    @BindView(R.id.recycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    private WebSocketService webSocketService;
    private List<String> mSensitiveWordList = new ArrayList<>();//敏感词词汇列表
    private ChatAdapter mAdapter;
    private List<ServiceChatIndexBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }


    @Override
    protected void initLocalData() {
        List<String> objList = GsonUtil.getInstance().parseDataNoKey(SharePref.local().getSensitiveWordsListJson());
        if (!ListUtils.isEmpty(objList)) {
            mSensitiveWordList.clear();
            mSensitiveWordList.addAll(objList);
        }
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        initWebSocket();
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
                    mSendMessageBean.setTo("5");
                    mSendMessageBean.setMsgType("text");
                    mSendMessageBean.setFlag("msg");
                    webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));

                    addContentToList(forbidSensitiveWord(content), ServiceChatIndexBean.CHAT_TYPE_SEND);
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
        ServiceChatIndexBean obj = new ServiceChatIndexBean();

        List<ServiceChatBean> tempList = new ArrayList<>();
        ServiceChatBean littleObj = new ServiceChatBean();
        littleObj.setContent(content);
        tempList.add(littleObj);

        obj.setList(tempList);
        obj.setType(chatType);
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

