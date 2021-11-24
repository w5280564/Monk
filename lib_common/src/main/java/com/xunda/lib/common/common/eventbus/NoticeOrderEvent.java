package com.xunda.lib.common.common.eventbus;

/**
 * 提醒订单页面刷新数据
 */
public class NoticeOrderEvent {

    public final static int FRESH_DATA = 1;//提醒订单页面刷新数据

    //操作所属的模块类型(业务线)
    public int type;


    public NoticeOrderEvent(int type) {
        this.type = type;
    }


}
