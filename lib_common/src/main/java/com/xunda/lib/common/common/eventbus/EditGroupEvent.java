package com.xunda.lib.common.common.eventbus;

public class EditGroupEvent {

    public final static int EDIT_GROUP = 1; //编辑社群

    //操作所属的模块类型(业务线)
    public int type;
    public String value;

    public EditGroupEvent(int type) {
        this.type = type;
    }



}
