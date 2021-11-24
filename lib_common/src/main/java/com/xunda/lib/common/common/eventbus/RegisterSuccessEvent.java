package com.xunda.lib.common.common.eventbus;

public class RegisterSuccessEvent {
    public final static int REGISTER_SUCCESS = 1;//注册成功

    //操作所属的模块类型(业务线)
    public int type;

    public RegisterSuccessEvent(int type) {
        this.type = type;
    }
}
