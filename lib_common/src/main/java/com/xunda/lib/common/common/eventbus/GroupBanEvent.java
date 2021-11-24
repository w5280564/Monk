package com.xunda.lib.common.common.eventbus;

public class GroupBanEvent {
    public final static int GROUP_BAN = 119; //群封禁
    //操作所属的模块类型(业务线)
    public int type;
    public String toast;

    public GroupBanEvent(int type,String toast) {
        this.toast = toast;
        this.type = type;
    }
}
