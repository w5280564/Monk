package com.xunda.lib.common.common.eventbus;


public class WechatPayEvent {

    public final static int WECHAT_PAY_RESULT = 1; //微信支付结果回调

    public int event_type;
    public int errCode;


    public WechatPayEvent(int event_type, int errCode) {
        this.event_type = event_type;
        this.errCode = errCode;
    }
}
