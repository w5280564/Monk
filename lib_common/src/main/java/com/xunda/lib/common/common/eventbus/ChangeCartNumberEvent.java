package com.xunda.lib.common.common.eventbus;

/**
 * 购物车数量
 */
public class ChangeCartNumberEvent {

    public final static int CHANGE_NUMBER = 2;//企事业单位注册第二步填写完资料，下一步

    //操作所属的模块类型(业务线)
    public int type;
    public int number;


    public ChangeCartNumberEvent(int type, int number) {
        this.type = type;
        this.number = number;
    }


}
