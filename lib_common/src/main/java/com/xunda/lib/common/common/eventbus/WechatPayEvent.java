package com.xunda.lib.common.common.eventbus;


public class WechatPayEvent {

    public final static int WECHAT_PAY_RESULT = 1; //微信支付结果回调

    public int event_type;
    public int result_type;


    public WechatPayEvent(int event_type, int result_type) {
        this.event_type = event_type;
        this.result_type = result_type;
    }
}
