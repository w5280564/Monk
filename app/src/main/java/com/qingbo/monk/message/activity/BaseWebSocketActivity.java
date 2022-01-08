package com.qingbo.monk.message.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.qingbo.monk.bean.SendMessageBean;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;


/**
 * WebSocketA基类
 */
public abstract class BaseWebSocketActivity extends BaseActivity {
    private static final String TAG = "websocket";

    private WebSocketService webSocketService;

    private void initWebSocket() {
        bindService(new Intent(this, WebSocketService.class), serviceConnection, BIND_AUTO_CREATE);
    }
    @Override
    protected void initView() {
        initWebSocket();
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
                    if (text==null) {
                        return;
                    }

                    onCommonResponse(text);
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
                    mSendMessageBean.setFlag(ReceiveMessageBean.MESSAGE_FLAG_INIT);
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


    //接收消息
    protected abstract void onCommonResponse(String text);



    /**
     * 发送消息
     */
    protected void sendText(String content,String toId) {
        if (webSocketService != null) {
            SendMessageBean mSendMessageBean = new SendMessageBean();
            mSendMessageBean.setMessage(content);
            mSendMessageBean.setFrom(SharePref.user().getUserId());
            mSendMessageBean.setTo(toId);
            mSendMessageBean.setMsgType(ReceiveMessageBean.MESSAGE_TYPE_TEXT);
            mSendMessageBean.setFlag(ReceiveMessageBean.MESSAGE_FLAG_MSG);
            webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));
        }
    }
}