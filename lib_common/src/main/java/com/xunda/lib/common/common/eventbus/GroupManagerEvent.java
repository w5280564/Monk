package com.xunda.lib.common.common.eventbus;

public class GroupManagerEvent {
    public final static int UPDATE_FEE = 1; //修改入群方式
    public final static int UPDATE_THEME = 2; //修改主题


    //操作所属的模块类型(业务线)
    public int type;
    public String value;

    public GroupManagerEvent(int type,String value) {
        this.type = type;
        this.value = value;
    }
}
