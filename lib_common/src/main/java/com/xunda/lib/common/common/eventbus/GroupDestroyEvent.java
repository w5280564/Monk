package com.xunda.lib.common.common.eventbus;

public class GroupDestroyEvent {
    public final static int GROUP_DESTROY = 120; //群解散
    //操作所属的模块类型(业务线)
    public int type;
    public String toast;

    public GroupDestroyEvent(int type, String toast) {
        this.toast = toast;
        this.type = type;
    }
}
