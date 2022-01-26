package com.xunda.lib.common.common.eventbus;

public class SocketUnbindEvent {

    public final static int SocketUnbind = 1;

    //操作所属的模块类型(业务线)
    public int type;
    public String value;

    public SocketUnbindEvent(int type) {
        this.type = type;
    }



}
