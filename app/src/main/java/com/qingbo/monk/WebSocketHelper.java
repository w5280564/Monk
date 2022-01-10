package com.qingbo.monk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.gson.Gson;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.qingbo.monk.bean.SendMessageBean;
import com.qingbo.monk.message.activity.WebSocketService;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;

/**
 * WebSocket封装类
 */
public class WebSocketHelper {
    private OnReceiveMessageListener mOnReceiveMessageListener;
    private static final String TAG = "websocket";
    static private WebSocketHelper instance = null;
    private WebSocketService webSocketService;

    private WebSocketHelper() {
    }

    public static WebSocketHelper getInstance() {
        if(instance == null){
            synchronized (WebSocketHelper.class) {
                if(instance == null) {
                    instance = new WebSocketHelper();
                }
            }
        }
        return instance;
    }


    /**
     * 初始化和绑定WebSocket
     * @param mContext
     * @param BIND_AUTO_CREATE
     */
    public void initWebSocketService(Context mContext, int BIND_AUTO_CREATE) {
        mContext.bindService(new Intent(mContext, WebSocketService.class), serviceConnection, BIND_AUTO_CREATE);
    }


    /**
     * 解绑WebSocket
     * @param mContext
     */
    public void unbindWebSocketService(Context mContext) {
        mContext.unbindService(serviceConnection);
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
            L.e(TAG,"接收到消息>>>"+text);
            if (text==null) {
                return;
            }

            ReceiveMessageBean receiveObj = GsonUtil.getInstance().json2Bean(text,ReceiveMessageBean.class);
            if (receiveObj==null) {
                return;
            }


            if ("-1".equals(receiveObj.getFrom())) {
                return;
            }

            if (mOnReceiveMessageListener==null) {
                return;
            }
            mOnReceiveMessageListener.onReceiveMessage(receiveObj);
        }

        @Override
        public void onOpen() {

            L.e(TAG,"onOpen>>初始化");
            if (PrefUtil.isLogin()) {
                initSocketService(SharePref.user().getUserId());
            }
        }

        @Override
        public void onClosed() {
            L.e(TAG,"onClosed");
        }
    };

    /**
     * 发送消息
     */
    public void initSocketService(String selfUserId) {
        if (webSocketService != null) {
            SendMessageBean mSendMessageBean = new SendMessageBean();
            mSendMessageBean.setFrom(selfUserId);
            mSendMessageBean.setFlag(ReceiveMessageBean.MESSAGE_FLAG_INIT);
            webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));
        }
    }



    /**
     * 发送消息
     */
    public void sendMessage(String content,String msgType, String toId) {
        if (webSocketService != null) {
            SendMessageBean mSendMessageBean = new SendMessageBean();
            mSendMessageBean.setMessage(content);
            mSendMessageBean.setFrom(SharePref.user().getUserId());
            mSendMessageBean.setTo(toId);
            mSendMessageBean.setMsgType(msgType);
            mSendMessageBean.setFlag(ReceiveMessageBean.MESSAGE_FLAG_MSG);
            webSocketService.send(GsonUtil.getInstance().toJson(mSendMessageBean));
        }
    }

    public interface OnReceiveMessageListener{
        //接收消息
        void onReceiveMessage(ReceiveMessageBean receiveObj);
    }


    public void setReceiveMessageListener(OnReceiveMessageListener mOnReceiveMessageListener){
        this.mOnReceiveMessageListener = mOnReceiveMessageListener;
    }
}
