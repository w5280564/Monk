package com.xunda.lib.common.common.eventbus;

/**
 * 提醒购物车页面刷新数据
 */
public class NoticeCartEvent {

    public final static int FRESH_DATA = 1;//提醒购物车页面刷新数据

    //操作所属的模块类型(业务线)
    public int type;


    public NoticeCartEvent(int type) {
        this.type = type;
    }


}
