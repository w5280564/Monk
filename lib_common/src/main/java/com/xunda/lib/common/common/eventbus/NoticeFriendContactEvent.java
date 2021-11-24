package com.xunda.lib.common.common.eventbus;

/**
 * 提醒通讯录好友列表刷新数据
 */
public class NoticeFriendContactEvent {

    public final static int FRESH_DATA = 1;//提醒通讯录好友列表刷新数据

    //操作所属的模块类型(业务线)
    public int type;


    public NoticeFriendContactEvent(int type) {
        this.type = type;
    }

}
