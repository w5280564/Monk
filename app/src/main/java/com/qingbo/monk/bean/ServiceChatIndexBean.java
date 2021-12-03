package com.qingbo.monk.bean;

import java.util.List;

public class ServiceChatIndexBean {


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
