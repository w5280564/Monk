package com.xunda.lib.common.common.eventbus;

public class AddFriendEvent {

    public final static int ADD_FRIEND_PHONE = 1; //手机号添加好友事件

    //操作所属的模块类型(业务线)
    public int type;

    public AddFriendEvent(int type) {
        this.type = type;
    }
}
