package com.qingbo.monk.bean;

import java.util.List;

/**
 * @文件描述：
 * @作者: ouyang
 * @日期: 2020/8/3
 */
public class ServiceChatIndexBean {


    /**
     * Keyword : 活动地点
     * Link : [{"title":"活动地点","title_url":"http://59.37.4.70:881/index/Booth/plan.html"}]
     * Content : 南丰国际会展中心（地址：广州市海珠区新港东路630-638号）
     */

    //智能客服会话类型
    public static final int CHAT_TYPE_SEND = 1;//发出的内容

    public static final int CHAT_TYPE_ANSWER = 2;//回答的内容

    private int type;
    private List<ServiceChatBean> list;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ServiceChatBean> getList() {
        return list;
    }

    public void setList(List<ServiceChatBean> list) {
        this.list = list;
    }
}
