package com.xunda.lib.common.common.eventbus;

/**
 * 刷新首页初始化环信操作
 */
public class LoginSuccessEvent {

    public final static int FRESH_MAIN_ACTIVITY_HX = 1;//刷新首页初始化环信操作

    //操作所属的模块类型(业务线)
    public int type;


    public LoginSuccessEvent(int type) {
        this.type = type;
    }


}
