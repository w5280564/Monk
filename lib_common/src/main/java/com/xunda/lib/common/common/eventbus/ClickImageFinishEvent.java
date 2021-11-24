package com.xunda.lib.common.common.eventbus;

public class ClickImageFinishEvent {
    public final static int CLICK_IMAGE = 1; //点击图片返回


    //操作所属的模块类型(业务线)
    public int type;

    public ClickImageFinishEvent(int type) {
        this.type = type;
    }
}
