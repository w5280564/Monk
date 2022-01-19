package com.xunda.lib.common.common.eventbus;


public class WechatLoginEvent {

    public final static int WECHAT_Login_RESULT = 1; //微信登录成功结果回调

    public int event_type;


    public WechatLoginEvent(int event_type) {
        this.event_type = event_type;
    }
}
