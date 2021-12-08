package com.xunda.lib.common.common.eventbus;

public class EditGroupEvent {

    public final static int EDIT_GROUP = 1; //编辑社群
    public final static int EDIT_GROUP_NAME = 2; //编辑社群群名称
    public final static int EDIT_GROUP_HEADER = 3; //编辑社群群头像

    //操作所属的模块类型(业务线)
    public int type;
    public String value;

    public EditGroupEvent(int type) {
        this.type = type;
    }


    public EditGroupEvent(int type,String value) {
        this.type = type;
        this.value = value;
    }

}
