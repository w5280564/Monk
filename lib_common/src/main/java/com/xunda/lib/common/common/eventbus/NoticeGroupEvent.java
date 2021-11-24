package com.xunda.lib.common.common.eventbus;

/**
 * 提醒群组列表刷新数据
 */
public class NoticeGroupEvent {

    public final static int FRESH_DATA = 1;//提醒群组列表刷新数据
    public final static int FRESH_DATA_BACK = 2;//提醒群组列表刷新数据,并且会话界面要finish掉
    public final static int FRESH_DATA_DETAIL = 3 ;//提醒刷新群主详情列表

    //操作所属的模块类型(业务线)
    public int type;
    public String title;


    public NoticeGroupEvent(int type) {
        this.type = type;
    }

    public NoticeGroupEvent(int type,String title) {
        this.title = title;
        this.type = type;
    }
}
