package com.xunda.lib.common.common.eventbus;

public class FinishEvent {

    public final static int CREATE_GROUP = 1; //创建完社群
    public final static int PUBLISH_QUESTION = 2; //发布完问答
    public final static int PUBLISH_TOPIC = 3; //发布完社群话题


    //操作所属的模块类型(业务线)
    public int type;

    public FinishEvent(int type) {
        this.type = type;
    }



}
