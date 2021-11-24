package com.xunda.lib.common.common.eventbus;

public class PeopleCustomerEvent {
    public final static int REQUEST_PEOPLE_CUSTOMER = 101;
    //操作所属的模块类型(业务线)
    public int type;

    public PeopleCustomerEvent(int type) {
        this.type = type;
    }
}
