package com.xunda.lib.common.common.eventbus;

public class FinishEvent {

    public final static int CREATE_SHEQUN = 1; //创建完社群

    //操作所属的模块类型(业务线)
    public int type;

    public FinishEvent(int type) {
        this.type = type;
    }

}
