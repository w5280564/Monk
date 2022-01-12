package com.xunda.lib.common.common.eventbus;

import com.xunda.lib.common.bean.ReceiveMessageBean;

public class ReceiveSocketMessageEvent {

    public final static int RECEIVE_MESSAGE = 1; //收到消息

    //操作所属的模块类型(业务线)
    public int type;
    public ReceiveMessageBean receiveObj;



    public ReceiveSocketMessageEvent(int type, ReceiveMessageBean receiveObj) {
        this.type = type;
        this.receiveObj = receiveObj;
    }

}
